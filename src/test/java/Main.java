import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherConstants;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.reporting.legacy.xml.LegacyXmlReportGeneratingListener;
import utils.VOTestEngine;
import utils.xmlreport.XmlReportGeneratingListener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    @Test
    public void mainForMavenExecution() {
        Main.main(null);
    }

    public static void main(String[] args) {

        List<String> browserConfigs = Arrays.asList("sauce:Windows 10:chrome:latest"
                                                    //,
                                                    //"sauce:Windows 10:firefox:latest",
                                                    //"sauce:Windows 10:MicrosoftEdge:latest"
                                                    );


        List<String> execResult = browserConfigs.parallelStream().map(config -> {
            Thread.currentThread().setName("Test-Executor_"+config);
            BaseTest.BROWSER_CONFIG.set(config);
            BaseTest.setup();
            System.out.println("initializing base test in test runner");
            BaseTest.setup();
            BaseTest.IGNORE_BEFORE_AND_AFTER_LIFECYCLE.set(Boolean.TRUE);

            JupiterTestEngine jupiterTestEngine = new JupiterTestEngine();
            VOTestEngine myTestEngine = new VOTestEngine(jupiterTestEngine, "VO-UI-Acceptance_" + config + "_");

            SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();

            /*
            BiConsumer<Throwable, Supplier<String>> logger = (t, msg) -> {
                System.out.println(" --- " + msg.get());
                if(t != null) {
                    t.printStackTrace();
                }
            };
            LoggingListener loggingListener = LoggingListener.forBiConsumer(logger);
            */

            Path reportsDir = Paths.get("./testReports");
            LauncherConfig launcherConfig = LauncherConfig.builder()
                    .enableTestEngineAutoRegistration(false)
                    //.enableTestExecutionListenerAutoRegistration(false)
                    .addTestEngines(myTestEngine)
                    //.addTestExecutionListeners(new LegacyXmlReportGeneratingListener(reportsDir, new PrintWriter(System.out)))
                    .addTestExecutionListeners(new XmlReportGeneratingListener(reportsDir, new PrintWriter(System.out)))

                    .addTestExecutionListeners(summaryListener)
                    //.addTestExecutionListeners(loggingListener)
                    .build();
            Launcher launcher = LauncherFactory.create(launcherConfig);

            // Register a listener of your choice
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(
                            DiscoverySelectors.selectPackage("com.vo.createformdialog")
                    )
                    .configurationParameter(LauncherConstants.CAPTURE_STDOUT_PROPERTY_NAME, "true")
                    .configurationParameter(LauncherConstants.CAPTURE_STDERR_PROPERTY_NAME, "true")
                    .configurationParameter(LauncherConstants.CAPTURE_MAX_BUFFER_PROPERTY_NAME, "" + Integer.MAX_VALUE)
                    .build();

            launcher.execute(request);

            BaseTest.IGNORE_BEFORE_AND_AFTER_LIFECYCLE.set(Boolean.FALSE);
            BaseTest.tearDown();

            TestExecutionSummary summary = summaryListener.getSummary();
            reportSauceJobStatus(summary);

            StringWriter stringWriter = new StringWriter();
            summary.printTo(new PrintWriter(stringWriter));

            System.out.println(config+"\n"+stringWriter.toString());
            return config + "-> done";
        }).collect(Collectors.toList());

        System.out.println("execution result: " + StringUtils.join(execResult, ","));
    }

    private static void reportSauceJobStatus(TestExecutionSummary summary) {
        try {
            long totalFailureCount = summary.getTotalFailureCount();

            SauceREST sauce = new SauceREST(BaseTest.SAUCE_USERNAME, BaseTest.SAUCE_ACCESS_KEY, DataCenter.EU);
            String sauceJobId = BaseTest.SAUCE_SESSION_ID.get();
            if(totalFailureCount == 0) {
                sauce.jobPassed(sauceJobId);
            } else {
                sauce.jobFailed(sauceJobId);
            }
        } catch (Throwable t) {

            System.out.println("error on sauce job status reporting ");
            t.printStackTrace();
        }

    }
}
