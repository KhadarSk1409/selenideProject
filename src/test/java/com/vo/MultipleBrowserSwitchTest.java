package com.vo;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Optional;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.vo.BaseTest.UserType;
import static com.vo.BaseTest.shouldLogin;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(("Multiple Browser Switch"))
public class MultipleBrowserSwitchTest {


    @Test
    @DisplayName("Verify the multiple user login logout functions in two browser windows")
    @Order(1)
    public void openTwoBrowsers(){

        String TEST_BASE_URL = System.getenv("TEST_BASE_URL");
        Configuration.baseUrl = Optional.ofNullable(TEST_BASE_URL).orElse("https://visualorbit.fireo.net");
        //Configuration.baseUrl = "http://localhost:3000";
        Configuration.timeout = 30000;

        ChromeOptions handlingSSL = new ChromeOptions();
        handlingSSL.setAcceptInsecureCerts(true); //Handles the insecure certificate issues during opening the browser

        WebDriverManager.chromedriver().setup();
        WebDriver browser1 = new ChromeDriver(handlingSSL);
        WebDriver browser2 = new ChromeDriver(handlingSSL);

        using(browser1, () -> {
            open("/");
            shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        });

        using(browser2, () -> {
            open(TEST_BASE_URL);
            shouldLogin(UserType.USER_01);
        });

        using(browser1, () -> {
            $(elementLocators("UserSettingsPopover")).click();
            $(elementLocators("User")).shouldHave(text("GUI Tester"));
        });

        using(browser2,() ->{
            $(elementLocators("UserSettingsPopover")).click();
            $(elementLocators("User")).shouldHave(text("GUI Tester 01"));
        });

        browser1.close();
        browser2.close();
    }
}
