package com.vo;

import com.codeborne.selenide.*;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Array;
import java.util.Arrays;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selectors.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User Settings Tests")
public class UserSettingsTest extends BaseTest {


//    @BeforeAll
//    public static void setAppLanguageToEnglishSettings() {
//        $("#user").waitUntil(visible, 5000).click(); //Wait until the 'User' element is visible on Dashboard and click on it
//        $("#myPreferences").click(); //Click on preferences
//        $("#account_settings.MuiListItem-button").shouldBe(visible).click(); //Account Settings
//        String existingAppLanguage = $("#defaultLocale").should(exist).getText(); //Check the existing value of Language selected
//
//        if (existingAppLanguage.contains("German")) {
//            $("#defaultLocale").click();
//            $("#defaultLocaleSelectMenu").should(appear);
//            $$("#defaultLocaleSelectMenu li").shouldHave(texts("German - Germany", "English - Great Britain"));
//            $$("#defaultLocaleSelectMenu li").findBy(text("English - Great Britain")).click();
//            $("#btnSave").shouldBe(visible).click(); //Save the changes
//
//            $("#toDashboard").click(); //Click on Home button
//            $("#btnCreateForm").should(exist).click(); //Verify that user is on Dashboard page and click on Create form
//        }
//    }

//    @BeforeEach
//    public void openDashboard() {
////        open("/userSettings");
////        $("#userSettingsView").should(appear);
//
//    }

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
     //   $("#appearanceThemeSwitch").shouldBe(visible);

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
            $("#btnSave").shouldHave(text("SPEICHERN")); //Verify that the app language is changed to German

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
   @Disabled
   @Order(3)
  @DisplayName("As a User, I should be able to switch my Date & Time Format")
  public void switchDataTimeFormat()
   {
       $(" #appearance").should(exist).click(); //Click on Appearance
       $("#dateTimeFormat").should(exist).click(); //Click on Date & Time format

       //Verify Appearance Date & Time Format dropdown menu is shown . Issue: id not found, dropdown menu collection need to be brought in for dynamic date time
       //Post that:
       //Create collection for dropdown menu items
       //For each menu item : Select menu item from Dd collection, get text from Dd, get text from result date & time,
       // split based on " " and verify that the value on left side is same.
       //Do this for all the menu items.


       String dateTimeFormat = $("#dateTimeFormat").should(exist).getText(); //Result Date & Time field value
       String [] dateTimeFormatArr = dateTimeFormat.split(" "); //Create String array for resultDateTime split

       if(!(dateTimeFormatArr.length > 2))
       {
            String resultDateTime = $("#dateTimeFormatResultExample").should(exist).getText();
           String [] resultDateTimeArr = resultDateTime.split(" ");

           assertTrue((dateTimeFormatArr[0].equals(resultDateTimeArr[0])),"The date in Date & Time Format Dropdown is "+dateTimeFormatArr[0]+" and in Result Date & Time is: "+resultDateTimeArr[0]);
       }



    }


}
