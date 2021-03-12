package utils;

import com.codeborne.selenide.logevents.SimpleReport;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.engine.execution.InvocationInterceptorChain;

import java.lang.reflect.Method;

public class SelenideLogReport implements BeforeEachCallback, AfterEachCallback, InvocationInterceptor {
    SimpleReport simpleReport = new SimpleReport();
    SelenideStdOutSimpleReport stdOutSimpleReport = new SelenideStdOutSimpleReport();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        //simpleReport.start();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        //simpleReport.finish(context.getTestClass().get().getSimpleName() + " " + context.getDisplayName());
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                     ExtensionContext context) throws Throwable {
        stdOutSimpleReport.start();
        try {
            invocation.proceed();
        } finally {
            stdOutSimpleReport.finish(context.getTestClass().get().getSimpleName() + " " + context.getDisplayName());
        }
    }
}
