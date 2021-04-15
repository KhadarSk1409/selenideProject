package com.vo.createformdialog;

import com.codeborne.selenide.Configuration;
import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.Keys.TAB;

public class LanguageProperties extends BaseTest {

    //Fetch languages from 'preferred form creation locales'
    //Pre-requisite: User has selected English and German as Preferred languages in Preferences and Default as English
    @Test
    public void validateLanguageProperties()
    {
        $("#toDashboard").click(); //Go back to Dashboard
        $("#btnCreateForm").should(exist).click(); //Click on Create Form button
        $("#wizardFormDlg").should(appear); //Create Form wizard appears
        $("#wizard-formTitle").setValue(RandomStringUtils.randomAlphanumeric(4)); //Set Title name

        String idText = $("#wizard-formId").getValue();
        String expectedUrl = Configuration.baseUrl + "/Dashboard/" + idText;

        $("#btnCreateForm").should(exist).click(); //Click on Create Form button

        String actualUrl = $("#wizard-formUrl").getValue();
        System.out.println("The Direct link to form dashboard is: " + actualUrl);
        $("#wizard-formUrl").shouldHave(value(expectedUrl)); //The url which should be there in url field




    }


    //Create form->enter title, description
    //Click on additional options button
    //Verify that Form Title column has the title entered in Create form
    //Verify the Language column has the same language as selected in My Preferences, including Default Locale. If two languages, two rows are there
    //Verify that in column “Actions” the “delete” button should be deactivated for the default language
    //the buttons “BACK”, “CREATE FORM”, “NEXT” and “CANCEL” should all be enabled


}
