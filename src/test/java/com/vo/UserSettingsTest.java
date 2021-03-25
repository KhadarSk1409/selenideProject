package com.vo;


import com.codeborne.selenide.*;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selectors.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User Settings Tests")
public class UserSettingsTest extends BaseTest {


    @BeforeEach
    public void openDashboard() {
        open("/userSettings");
        $("#userSettingsView").should(appear);
    }

    @Test
    @Order(1)
    @DisplayName("Appearance should change to Dark Theme")
    public void appearanceShouldChangeToDarkTheme() {
        SelenideElement body = $("body");
        String backGroundColorBefore = body.getCssValue("background-color");
        String colorBefore = body.getCssValue("color");
        $("#appearance.MuiListItem-button").shouldBe(visible).click();

        $("#user").shouldBe(visible).click();
        $("#appearanceThemeSwitch").shouldBe(visible);

        SelenideElement ckbDarkTheme = $("#ckbDarkTheme").shouldBe(visible);
        boolean darkIsChecked = $("#ckbDarkTheme input").is(checked);
        $("#ckbDarkTheme").shouldBe(visible).click();

        if (darkIsChecked) {
            $("#appearanceThemeSwitch input").shouldBe(not(checked));
        } else {
            $("#appearanceThemeSwitch input").shouldBe(checked);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Should change users default language")
    public void shouldChangeUsersDefaultLanguage() {

        $("#user").waitUntil(visible, 5000).click(); //Wait until the 'User' element is visible on Dashboard and click on it
        $("#myPreferences").click(); //Click on preferences

        $("#account_settings.MuiListItem-button").shouldBe(visible).click(); //Account Settings

        String existingAppLanguage = $("#defaultLocale").should(exist).getText(); //Check the existing value of Language selected

        if (existingAppLanguage.contains("English")) {
            $("#defaultLocale").click();
            $("#defaultLocaleSelectMenu").should(appear);
            $$("#defaultLocaleSelectMenu li").shouldHave(texts("German - Germany", "English - Great Britain"));
            $$("#defaultLocaleSelectMenu li").findBy(text("German - Germany")).click();

        } else if (existingAppLanguage.contains("German")) {
            $("#defaultLocale").click();
            $("#defaultLocaleSelectMenu").should(appear);
            $$("#defaultLocaleSelectMenu li").shouldHave(texts("German - Germany", "English - Great Britain"));
            $$("#defaultLocaleSelectMenu li").findBy(text("English - Great Britain")).click();
        }

        $("#btnSave").shouldBe(visible).click(); //Save the changes

    }
}
