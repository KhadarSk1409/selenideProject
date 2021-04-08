package com.vo;

import com.codeborne.selenide.*;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import java.util.List;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User Settings Tests")
public class UserSettingsTest extends BaseTest {

    @Test
    @Order(1)
    @DisplayName("Appearance should change to Dark Theme")
    public void appearanceShouldChangeToDarkTheme() {

        $("#user").should(exist).click(); //Click on user
        $("#myPreferences").should(appear).click(); //Click on preferences
        $("#appearance").should(exist).click(); //Click on Appearance
        SelenideElement body = $("body");

        if ($("#appearanceThemeSwitch input").is(checked)) //If Dark Theme is checked
        {
            System.out.println("Dark theme is checked");//Verify the background colour
            String backGroundColorBefore = body.getCssValue("background-color");
            System.out.println("When Dark Theme is checked, the backGroundColorBefore is: "+backGroundColorBefore);

            $("#ckbDarkTheme").shouldBe(enabled);  //Verify that Dark Theme should be checked
            $("#ckbDarkTheme").click(); //Now change the theme value from theme check box in appearance
            $("#ckbDarkTheme").shouldNotBe(checked);  //Verify that Dark Theme should be checked
            $("#appearanceThemeSwitch input").shouldNotBe(checked);

            String backGroundColorAfter = body.getCssValue("background-color");
            System.out.println("When Dark Theme is checked,the backGroundColorAfter is: "+backGroundColorAfter);

            assertTrue(!(backGroundColorBefore.equalsIgnoreCase(backGroundColorAfter))); //Since the color change from Dark to White does not save, this assertion doesn't work

            //Now change the theme from GUI tester menu:
            $("#appearanceThemeSwitch input").click();
            $("#ckbDarkTheme").shouldBe(enabled);
            $("#btnSave").click(); //Click on Save button

        } else if (!($("#appearanceThemeSwitch input").is(checked))) {
            System.out.println("Light theme is checked");

            //Verify the background colour
            String backGroundColorBefore = body.getCssValue("background-color");
            System.out.println("When Light Theme is checked, the backGroundColorBefore is: "+backGroundColorBefore);

            $("#ckbDarkTheme").shouldNotBe(checked);  //Verify that Dark Theme should be checked
            $("#ckbDarkTheme").click(); //Now change the theme value from theme check box in appearance
            $("#ckbDarkTheme").waitUntil(visible, 3000);  //Verify that Dark Theme should be checked
            $("#ckbDarkTheme").is(checked);
            $("#appearanceThemeSwitch input").shouldBe(checked);

            //Issue: Light to Dark theme works but Dark to Light does not reflects unless page refreshed
            String backGroundColorAfter = body.getCssValue("background-color");
            System.out.println("When Light Theme is checked, the backGroundColorAfter is: "+backGroundColorBefore);

            assertTrue(!(backGroundColorBefore.equalsIgnoreCase(backGroundColorAfter))); //-Since the color change from Dark to White does not save, this assertion doesn't work

            //Now change the theme from GUI tester menu:
            $("#appearanceThemeSwitch input").click();
            $("#ckbDarkTheme").shouldNotBe(checked);
            $("#btnSave").click(); //Click on Save button

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
    public void verifySwitchDateTimeFormat() {
        $(" #appearance").should(exist).click(); //Click on Appearance
        $("#dateTimeFormat").should(exist).click(); //Click on Date & Time format

        List<SelenideElement> listDateTimeFormat = $$("#dateTimeFormatSelectMenu li"); //Store all the elements in list

        int dateTimtFormDdCount = listDateTimeFormat.size();

        for (int i = 0; i < dateTimtFormDdCount; i++) {
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
