package com.vo;


import com.codeborne.selenide.*;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;

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

        if(darkIsChecked) {
            $("#appearanceThemeSwitch input").shouldBe(not(checked));
        } else {
            $("#appearanceThemeSwitch input").shouldBe(checked);
        }

        body.should(new Condition("change back- and foreground color") {
            @Override
            public boolean apply(Driver driver, WebElement webElement) {
                String backGroundColorAfter = webElement.getCssValue("background-color");
                String colorAfter = webElement.getCssValue("color");

                return backGroundColorAfter != backGroundColorBefore && colorAfter != colorBefore;
            }
        });
    }
}
