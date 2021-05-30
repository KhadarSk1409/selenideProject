package com.vo.mainDashboard;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(("Multiple User Switch"))
public class MultipleUserSwitchTest extends BaseTest {
    String user_01 = System.getenv("USER_01");
    String user_02 = System.getenv("USER_02");
    String user_03 = System.getenv("USER_03");
    String USER_PASS = System.getenv("USER_PASSWORD");

    private static void setSauceJobId() {
        WebDriver webDriver = WebDriverRunner.getWebDriver();
        if (webDriver instanceof RemoteWebDriver) {
            SessionId sessionId = ((RemoteWebDriver) webDriver).getSessionId();
            SAUCE_SESSION_ID.set(sessionId.toString());
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

    @Test
    @DisplayName("Verify the multiple user login logout functions")
    @Order(1)
    public void verifyUserSwitch1() {

        //open("https://visualorbit.fireo.net");

        String users[] = {user_01};
        for (String user : users) {
            shouldLogin(user, USER_PASS);

        }
    }

    @Test
    @DisplayName("Verify the multiple user login logout functions")
    @Order(2)
    public void verifyUserSwitch2() {
        //open("https://visualorbit.fireo.net");

        String users[] = {user_02};
        for (String user : users) {
            shouldLogin(user, USER_PASS);

        }
    }

    public static void shouldLogin(String user, String USER_PASS) {
        if (Boolean.FALSE.equals(ALREADY_LOGGED_IN.get())) {
            open("");
            setSauceJobId();
            $(By.name("loginfmt")).should(appear).setValue(user);
            $(".button.primary").shouldBe(visible).click();
            $("#displayName").shouldHave(text(user));

            $(By.name("passwd")).should(appear).setValue(USER_PASS);
            $(".button.primary").shouldBe(visible).click();

            //stay signed in?
            $(".button.primary").shouldBe(visible).click();

            appHeaderAppear();
            boolean presenceOfPickAnAccount = $("#loginHeader").is(exist);
            if (presenceOfPickAnAccount) {
                // String valueToBeClicked = "//small[contains(text(),"+"'"+TEST_USER_EMAIL+"')]";
                $(byText(TEST_USER_EMAIL)).shouldBe(visible).click();
            }

            //  assertEquals(title(), "VisualOrbit App");
            assertTrue(title().contains("VisualOrbit"));
            ALREADY_LOGGED_IN.set(Boolean.TRUE);
           /* $("#user").click();
            $("#user p").click();
            $("#btnYesLogout").should(appear).click();*/
        }

    }

  }
