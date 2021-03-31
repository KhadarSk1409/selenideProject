package com.vo;

import com.codeborne.selenide.*;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selectors.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User Settings Tests")
public class UserSettingsTest extends BaseTest {

    @Test
    @Order(1)
    @DisplayName("Appearance should change to Dark Theme")
    public void appearanceShouldChangeToDarkTheme() {
       // setAppLanguageToEnglish(); //Set App language to English
        $("#user").should(exist).click();
        $("#myPreferences").waitUntil(visible, 3000).click(); //Click on preferences
        $(" #appearance").should(exist).click(); //Click on Appearance
        SelenideElement body = $("body");
        String backGroundColorBefore = body.getCssValue("background-color");
        String colorBefore = body.getCssValue("color");
        $("#appearance.MuiListItem-button").shouldBe(visible).click();
        $("#user").shouldBe(visible).click();
        SelenideElement ckbDarkTheme = $("#ckbDarkTheme").shouldBe(visible);
        boolean darkIsChecked = $("#ckbDarkTheme input").is(checked);
        $("#ckbDarkTheme").shouldBe(visible).click();
        $("#user").should(exist).click();

        if (darkIsChecked) {
            $("#appearanceThemeSwitch input").shouldNotBe(checked); //New change used shouldNotBe
        } else {
            $("#appearanceThemeSwitch input").shouldBe(checked);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Should change users default language")
    public void shouldChangeUsersDefaultLanguage() {

        $("#account_settings.MuiListItem-button").shouldBe(visible).click(); //Account Settings

        String existingAppLanguage = $("#defaultLocale").should(exist).getText(); //Check the existing value of Language selected

        if (existingAppLanguage.contains("English")) {
            $("#defaultLocale").click();
            $("#defaultLocaleSelectMenu").should(appear);
            $$("#defaultLocaleSelectMenu li").shouldHave(texts("German - Germany", "English - Great Britain"));
            $$("#defaultLocaleSelectMenu li").findBy(text("German - Germany")).click();
            $("#btnSave").shouldBe(visible).click(); //Save the changes
            $("#btnSave").shouldHave(text("Speichern")); //Verify that the app language is changed to German

        }
        String newexistingAppLanguage = $("#defaultLocale").should(exist).getText(); //Check the existing value of Language selected
        if (newexistingAppLanguage.contains("German")) {
            $("#defaultLocale").click();
            $("#defaultLocaleSelectMenu").should(appear);
            $$("#defaultLocaleSelectMenu li").shouldHave(texts("German - Germany", "English - Great Britain"));
            $$("#defaultLocaleSelectMenu li").findBy(text("English - Great Britain")).click();
            $("#btnSave").shouldBe(visible).click(); //Save the changes
            $("#btnSave").shouldHave(text("Save")); //Verify that the app language is changed to English
        }

    }

   @Test
   @Order(3)
  @DisplayName("As a User, I should be able to switch my Date & Time Format")
  public void verifySwitchDateTimeFormat()
   {
       $(" #appearance").should(exist).click(); //Click on Appearance
       $("#dateTimeFormat").should(exist).click(); //Click on Date & Time format

       List <SelenideElement> listDateTimeFormat = $$("#dateTimeFormatSelectMenu li"); //Store all the elements in list

       int dateTimtFormDdCount =  listDateTimeFormat.size();

       for(int i = 0; i < dateTimtFormDdCount; i++)
       {
           SelenideElement selenideElement = listDateTimeFormat.get(i); //Fetch i th element from Selenide elements list

           listDateTimeFormat.get(i).click(); //Click on i th element

           String dateTimeFormat = $("#dateTimeFormat").getText();

           String[] dateTimeFormatArr = dateTimeFormat.split(" "); //Create String array for Date & Time format split

           if (!(dateTimeFormatArr.length > 2)) {
               String resultDateTime = $("#dateTimeFormatResultExample").getAttribute("value"); //Get the date time populated in Result Date & Time
               String[] resultDateTimeArr = resultDateTime.split(" ");

              //Verify that the Date format selected in D&T format field is populated in Result D&T field
               assertTrue((dateTimeFormatArr[0].equals(resultDateTimeArr[0])), "The date in Date & Time Format Dropdown is " + dateTimeFormatArr[0] + " and in Result Date & Time is: " + resultDateTimeArr[0]);
           }
           $("#dateTimeFormat").should(exist).click(); //Click on Date & Time format

       }

    }


}
