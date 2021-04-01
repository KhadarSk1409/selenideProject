package com.vo;

import com.codeborne.selenide.Config;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SimpleReport;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import utils.SelenideLogReport;



@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith({SelenideLogReport.class})
public abstract class BaseTest {

    protected static String TEST_BASE_URL = null;
    protected static String TEST_USER_EMAIL = null;
    protected static String TEST_USER_PASSWORD = null;
    public static String SAUCE_USERNAME = null;
    public static String SAUCE_ACCESS_KEY = null;
    public static ThreadLocal<String> BROWSER_CONFIG = ThreadLocal.withInitial(() -> "local");
    public static ThreadLocal<String> SAUCE_SESSION_ID = new ThreadLocal<>();
    public static ThreadLocal<RemoteWebDriver> WEB_DRIVER = new ThreadLocal<>();
    public static ThreadLocal<Boolean> ALREADY_LOGGED_IN = ThreadLocal.withInitial(() -> Boolean.FALSE);
    public static ThreadLocal<Boolean> IGNORE_BEFORE_AND_AFTER_LIFECYCLE = ThreadLocal.withInitial(() -> Boolean.FALSE);

    @BeforeAll
    public static void setup() {
        if (IGNORE_BEFORE_AND_AFTER_LIFECYCLE.get()) {
            System.out.println(Thread.currentThread().getName() + " ignoring junit lifecycle setup");
            return;
        }
        try {
            String browserConfig = BROWSER_CONFIG.get();
            System.out.println("init webdriver with browserConfig " + browserConfig);

            TEST_USER_EMAIL = System.getenv("TEST_USER_EMAIL");
            TEST_USER_PASSWORD = System.getenv("TEST_USER_PASSWORD");
            String buildId = Optional.ofNullable(System.getenv("BUILD_ID")).orElse("NO_BUILD_ID");

            String[] configOptions = browserConfig.split(":");
            String target = configOptions[0];

            TEST_BASE_URL = System.getenv("TEST_BASE_URL");
            Configuration.baseUrl = Optional.ofNullable(TEST_BASE_URL).orElse("https://fireo.net");
            //Configuration.baseUrl = "http://localhost:3000";
            Configuration.timeout = 20000;
            //Configuration.fastSetValue = true;
            //Configuration.clickViaJs = true;
            //Configuration.headless = true;

            if ("sauce".equals(target) && WEB_DRIVER.get() == null) {
                String platform = Optional.of(configOptions[1]).orElse("Windows 10");
                String browser = Optional.of(configOptions[2]).orElse("chrome");
                String version = Optional.of(configOptions[3]).orElse("");

                SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
                SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
                String SAUCE_URL = "https://" + SAUCE_USERNAME + ":" + SAUCE_ACCESS_KEY + "@ondemand.eu-central-1.saucelabs.com:443/wd/hub";

                System.out.println("using sauce labs url: " + SAUCE_URL);


                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("platformName", platform);
                caps.setCapability("browserName", browser);
                caps.setCapability("browserVersion", version);
                caps.setCapability("sauce:options", new LinkedHashMap() {{
                    put("username", SAUCE_USERNAME);
                    put("accessKey", SAUCE_ACCESS_KEY);
                    put("seleniumVersion", "");
                    put("build", buildId);
                    put("name", browserConfig);
                    put("maxDuration", 3600);
                    put("idleTimeout", 1000);
                }});

                RemoteWebDriver driver = new RemoteWebDriver(new URL(SAUCE_URL), caps);
                WebDriverRunner.setWebDriver(driver);
                WEB_DRIVER.set(driver);
            }

            shouldLogin();
            setAppLanguageToEnglish(); //Newly added
        } catch (Throwable t) {
            System.out.println("error on test setup: ");
            t.printStackTrace();
        }
    }

    // we close browser manually
    @AfterAll
    public static void tearDown() {
        if (IGNORE_BEFORE_AND_AFTER_LIFECYCLE.get()) {
            System.out.println(Thread.currentThread().getName() + " ignoring junit lifecycle tearDown");
            return;
        }
        try {
            System.out.println("tearing down test!!!, closing the webdriver");
            List<String> webDriverLogs = getWebDriverLogs(LogType.BROWSER);
            System.out.println(StringUtils.join(webDriverLogs, "\n"));
        } catch (Throwable ignore) {
            System.out.println("unable to retrieve logs from web driver: " + ignore.getMessage());
        } finally {
            close();
            ALREADY_LOGGED_IN.set(Boolean.FALSE);
        }
    }

    public static void shouldLogin() {
        if (Boolean.FALSE.equals(ALREADY_LOGGED_IN.get())) {
            open("");
            setSauceJobId();
            $(By.name("loginfmt")).should(appear).setValue(TEST_USER_EMAIL);
            $(".button.primary").shouldBe(visible).click();
            $("#displayName").shouldHave(text(TEST_USER_EMAIL));

            $(By.name("passwd")).should(appear).setValue(TEST_USER_PASSWORD);
            $(".button.primary").shouldBe(visible).click();

            //stay signed in?
            $(".button.primary").shouldBe(visible).click();
             $("#loginHeader").shouldBe(visible);
            $(byText(TEST_USER_EMAIL)).shouldBe(visible).click();

//            boolean presenceOfPickAnAccount = $("#loginHeader").is(exist);
//            if (presenceOfPickAnAccount) {
//                // String valueToBeClicked = "//small[contains(text(),"+"'"+TEST_USER_EMAIL+"')]";
//                $(byText(TEST_USER_EMAIL)).shouldBe(visible).click();
//            }

            //  assertEquals(title(), "VisualOrbit App");
            assertEquals(title(), "VisualOrbit OriginOne");
            ALREADY_LOGGED_IN.set(Boolean.TRUE);
        }

    }


    public static void setAppLanguageToEnglish() {
        $("#user").should(exist).click(); //Wait until the 'User' element is visible on Dashboard and click on it
        $("#myPreferences").click(); //Click on preferences
        $("#account_settings.MuiListItem-button").shouldBe(visible).click(); //Account Settings
        String existingAppLanguage = $("#defaultLocale").should(exist).getText(); //Check the existing value of Language selected
        if (existingAppLanguage.contains("German")) {
            $("#defaultLocale").click();
            $("#defaultLocaleSelectMenu").should(appear);
            $$("#defaultLocaleSelectMenu li").shouldHave(texts("German - Germany", "English - Great Britain"));
            $$("#defaultLocaleSelectMenu li").findBy(text("English - Great Britain")).click();
            $("#btnSave").shouldBe(visible).click(); //Save the changes
            $("#toDashboard").click(); //Click on Home button
            $("#btnCreateForm").should(exist).click(); //Verify that user is on Dashboard page and click on Create form
        }
        $("#user").should(exist).click(); //Click on Use icon and close the menu preferences
        $("#toDashboard").should(exist).click(); //Click on Launchpad
    }


    private static void setSauceJobId() {
        WebDriver webDriver = WebDriverRunner.getWebDriver();
        if(webDriver instanceof RemoteWebDriver) {
            SessionId sessionId = ((RemoteWebDriver) webDriver).getSessionId();
            SAUCE_SESSION_ID.set(sessionId.toString());
        }
    }


    protected static SelenideElement selectAndClear(String cssSelector) {
        return selectAndClear(By.cssSelector(cssSelector));
    }

    protected static SelenideElement selectAndClear(By selector) {
        /*
        SelenideElement se = $(selector);
        String currentValue = se.getValue();
        if(currentValue != null && currentValue.length() > 0) {
            Arrays.asList(currentValue.split("")).forEach(s -> se.sendKeys(Keys.BACK_SPACE));
        }
        */
        //alternative faster way to delete values with Ctrl + a and delete
        $(selector).sendKeys(Keys.chord(Keys.CONTROL, Keys.COMMAND,"a"));
        $(selector).sendKeys(Keys.chord(Keys.DELETE));
        return $(selector).shouldBe(empty);
    }
}
