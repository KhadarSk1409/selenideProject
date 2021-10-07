package com.vo;

import com.codeborne.selenide.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import utils.SelenideLogReport;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith({SelenideLogReport.class})
public abstract class BaseTest {

    protected static String TEST_BASE_URL = null;
    public static String SAUCE_USERNAME = null;
    public static String SAUCE_ACCESS_KEY = null;
    public static ThreadLocal<String> BROWSER_CONFIG = ThreadLocal.withInitial(() -> "local");
    public static ThreadLocal<String> SAUCE_SESSION_ID = new ThreadLocal<>();
    public static ThreadLocal<RemoteWebDriver> WEB_DRIVER = new ThreadLocal<>();
    public static ThreadLocal<Boolean> ALREADY_LOGGED_IN = ThreadLocal.withInitial(() -> Boolean.FALSE);
    public static ThreadLocal<UserType> CURRENT_USER = ThreadLocal.withInitial(() -> UserType.MAIN_TEST_USER);
    public static ThreadLocal<Boolean> IGNORE_BEFORE_AND_AFTER_LIFECYCLE = ThreadLocal.withInitial(() -> Boolean.FALSE);

    public enum UserType {
        MAIN_TEST_USER("TEST_USER_EMAIL", "TEST_USER_PASSWORD"),
        USER_01("TEST_USER_EMAIL_01", "TEST_USER_PASSWORD_01"),
        USER_02("TEST_USER_EMAIL_02", "TEST_USER_PASSWORD_02"),
        USER_03("TEST_USER_EMAIL_03", "TEST_USER_PASSWORD_03");

        String envUserEmail, envUserPassword;

        UserType(String envUserEmail, String envUserPassword) {
            this.envUserEmail = envUserEmail;
            this.envUserPassword = envUserPassword;
        }

        public String userEmail() {
            return System.getenv(this.envUserEmail);
        }

        public String password() {
            return System.getenv(this.envUserPassword);
        }
    }

    @BeforeAll
    public static void setup() {
        if (IGNORE_BEFORE_AND_AFTER_LIFECYCLE.get()) {
            System.out.println(Thread.currentThread().getName() + " ignoring junit lifecycle setup");
            return;
        }
        try {
            String browserConfig = BROWSER_CONFIG.get();
            System.out.println("init webdriver with browserConfig " + browserConfig);
            Configuration.startMaximized = true;

            String buildId = Optional.ofNullable(System.getenv("BUILD_ID")).orElse("NO_BUILD_ID");

            String[] configOptions = browserConfig.split(":");
            String target = configOptions[0];

            TEST_BASE_URL = System.getenv("TEST_BASE_URL");
            Configuration.baseUrl = Optional.ofNullable(TEST_BASE_URL).orElse("https://visualorbit.fireo.net");
            //Configuration.baseUrl = "http://localhost:3000";
            Configuration.timeout = 30000;
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
                    put("screenResolution", "1920x1200");
                }});
                //profile.content_settings.exceptions.clipboard
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("prefs", new LinkedHashMap(){{
                    //0 is default , 1 is enable and 2 is disable
                    put("profile.content_settings.exceptions.clipboard", getClipBoardSettingsMap(1));
                }});
                caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

                RemoteWebDriver driver = new RemoteWebDriver(new URL(SAUCE_URL), caps);
                WebDriverRunner.setWebDriver(driver);
                WEB_DRIVER.set(driver);
            }

            shouldLogin(UserType.MAIN_TEST_USER);


            setAppLanguageToEnglish(); //Newly added
        } catch (Throwable t) {
            System.out.println("error on test setup: ");
            t.printStackTrace();
        }
    }

    private static Map<String,Object> getClipBoardSettingsMap(int settingValue) {
        Map cbPreference = new LinkedHashMap() {{
            put("[*.],*", new LinkedHashMap() {{
                put("last_modified",String.valueOf(System.currentTimeMillis()));
                put("setting", settingValue);
            }});
        }};
        return cbPreference;
    }

    public String getClipboardContent() {
        if (BROWSER_CONFIG.get().startsWith("local")) {
            return Selenide.clipboard().getText();
        }
        executeJavaScript("async function getCBContents() { " +
                "try { " +
                "  window.cb = await navigator.clipboard.readText(); " +
                "  console.log(\"Pasted content: \", window.cb); " +
                "} catch (err) { " +
                "  console.error(\"Failed to read clipboard contents: \", err); " +
                "  window.cb = \"Error : \" + err; " +
                "} } " +
                "getCBContents();");
        Object result = Optional.ofNullable(executeJavaScript("return window.cb;")).orElse("null");
        return result.toString();
    }

    // we close browser manually
    @AfterAll
    public static void tearDown() {
        if (IGNORE_BEFORE_AND_AFTER_LIFECYCLE.get()) {
            System.out.println(Thread.currentThread().getName() + " ignoring junit lifecycle tearDown");
            return;
        }
        try {
            deleteForm();
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

    private static boolean appHeaderAppear() {
        boolean result = false;
        try {
            $("header.MuiAppBar-root").waitUntil(appears, 15000);
            result = true;
        } catch (Throwable t) {
            System.out.println("App Header is not presented ");
            t.printStackTrace();
        }
        return result;
    }

    public static void shouldLogin(UserType targetUser) {
        if(Boolean.TRUE.equals(ALREADY_LOGGED_IN.get()) && targetUser != CURRENT_USER.get()) {
            //logging out Current User
            $("#btnUserSettingsPopover").click(); //Click on User Settings Popover
            $("#userLogout").should(exist).click(); //Click on Logout
            $("#btnYesLogout").should(appear).click(); //Logout Confirmation
            boolean presenceOfPickAnAccount = $("#loginHeader").is(exist);
            if (presenceOfPickAnAccount) {
                $(byText(CURRENT_USER.get().userEmail())).shouldBe(visible).click();
                Wait().until((input) -> WebDriverRunner.url().endsWith("logoutsession"));
            }
            ALREADY_LOGGED_IN.set(Boolean.FALSE);
        }

        if (Boolean.FALSE.equals(ALREADY_LOGGED_IN.get())) {
            open("");
            setSauceJobId();
            Wait().until((input) -> WebDriverRunner.url().contains("oauth2/v2.0/authorize"));
            boolean presenceUseOtherAccount = $("#otherTileText").is(exist);
            if(presenceUseOtherAccount) {
                $("#otherTileText").shouldBe(visible).click();
            }
            $(By.name("loginfmt")).should(appear).setValue(targetUser.userEmail());
            $(".button.primary").shouldBe(visible).click();
            $("#displayName").shouldHave(text(targetUser.userEmail()));

            $(By.name("passwd")).should(appear).setValue(targetUser.password());
            $(".button.primary").shouldBe(visible).click();

            //stay signed in?
            boolean presenceOfStaySignedIn = $(".button.primary").is(exist);
            if(presenceOfStaySignedIn) {
                $(".button.primary").shouldBe(visible).click();
            }

            appHeaderAppear();
            boolean presenceOfPickAnAccount = $("#loginHeader").is(exist);
            if (presenceOfPickAnAccount) {
                // String valueToBeClicked = "//small[contains(text(),"+"'"+TEST_USER_EMAIL+"')]";
                $(byText(targetUser.userEmail())).shouldBe(visible).click();
            }

            //  assertEquals(title(), "VisualOrbit App");
            assertTrue(title().contains("VisualOrbit"));
            ALREADY_LOGGED_IN.set(Boolean.TRUE);
            CURRENT_USER.set(targetUser);
        }

    }


    public static void setAppLanguageToEnglish() {
        $("#toDashboard").shouldBe(visible);
        //if already in english -> skip
        if ($("#toDashboard").has(text("Launchpad"))) {
            return;
        }

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
            $("#btnSave").shouldHave(text("Save"));
            $("#toDashboard").click(); //Click on Home button
            $("#btnCreateForm").should(exist).click(); //Verify that user is on Dashboard page and click on Create form
        }
        $("#user").should(exist).click(); //Click on Use icon and close the menu preferences
        $("#toDashboard").should(exist).click(); //Click on Launchpad
    }

    private static void setSauceJobId() {
        WebDriver webDriver = WebDriverRunner.getWebDriver();
        if (webDriver instanceof RemoteWebDriver) {
            SessionId sessionId = ((RemoteWebDriver) webDriver).getSessionId();
            SAUCE_SESSION_ID.set(sessionId.toString());
        }
    }

    protected static void applyLabelForTestForms() {
        $("#dlgFormFormWizard #selFormLabelsControl").shouldBe(visible);
        if (!$("#dlgFormFormWizard #selFormLabelsControl .MuiChip-label").has(text("guitest"))) {
            $("#dlgFormFormWizard #selLabel ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
            $(".MuiAutocomplete-popper").should(appear);
            try {
                $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("guitest"), 7000);
                $$(".MuiAutocomplete-popper li").findBy(text("guitest")).click();
            } catch (Throwable t) {
                $("#dlgFormFormWizard #selLabel").setValue("guitest");
                $(".MuiAutocomplete-popper").should(appear);
                $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Add \"guitest\""));
                $$(".MuiAutocomplete-popper li").findBy(text("Add \"guitest\"")).click();
            }

            $("#dlgFormFormWizard #selFormLabelsControl .MuiChip-label").shouldHave(text("guitest"));
        }
    }

    protected static boolean applySearchForTestForms() {
        boolean hasGuiTestLabel = true;
        //selectAndClear("#formRelatedTabs .mtable_toolbar input.MuiInputBase-input").setValue("test-gu-");
        $("#btnMoreFilter").should(exist).click(); //Click on filter icon
        $("#pMoreFilterPopoverContent").should(appear);
        $("#selFormLabelsControl").shouldBe(visible); //Label dropdown
        if (!$("#selFormLabelsControl .MuiChip-label").has(text("guitest"))) {
            $("#selLabel ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
            $(".MuiAutocomplete-popper").should(appear);
            try {
                $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("guitest"), 5000);
                $$(".MuiAutocomplete-popper li").findBy(text("guitest")).click();
                hasGuiTestLabel = true;
            } catch (Throwable t) {
                hasGuiTestLabel = false;
            }
            $("body").click();
            $("#pMoreFilterPopoverContent").should(disappear);
        }

        if (hasGuiTestLabel) {
            $("#formListTable .MuiDataGrid-row").shouldBe(visible);
            try {
                $("#formListTable .MuiDataGrid-row").waitUntil(not(text("No records to display")), 5000);
            } catch (Throwable t) {
                hasGuiTestLabel = false;
            }
        }
        System.out.println("applySearchForTestForms " + hasGuiTestLabel);
        return hasGuiTestLabel;
    }

    public static void deleteForm() {
        open("/dashboard");
        $("#navLibrary").should(exist).hover().click(); //Hover and click on Library to navigate to formlist table
        if (!applySearchForTestForms()) {
            System.out.println("applySearchForTestForms returned false, exiting deletion");
            return;
        }
        int tableRows = $$("#formListTable .MuiDataGrid-row").size();
        System.out.println("found rows to delete: " + tableRows);
        for (int i = 0; i < tableRows; i++) {
            $("#formListTable .MuiDataGrid-row").shouldBe(visible);
            if ($("#formListTable").has(text("No records to display"))) {
                return;
            }

            $("#formListTable .btnExpandRow").should(exist).click();
            $("#formListTable .fa-trash-alt").should(exist).click();
            $("#confirm-deletion-dialog #confirmation-dialog-content").should(appear);
            $("#confirm-deletion-dialog #btnConfirm").should(exist).click();
            $(".fa-trash-alt").should(disappear);
            System.out.println("deleted row " + i);
            if (!applySearchForTestForms()) {
                return;
            }
        }
    }


    protected static SelenideElement selectAndClear(String cssSelector) {
        return selectAndClear(By.cssSelector(cssSelector));
    }

    public static SelenideElement selectAndClear(By selector) {
        $(selector).sendKeys(Keys.chord(Keys.CONTROL, Keys.COMMAND, "a"));
        $(selector).sendKeys(Keys.chord(Keys.DELETE));
        return $(selector).shouldBe(empty);
    }
}
