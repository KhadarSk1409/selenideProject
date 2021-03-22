package com.vo.createformdialog;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify New Form Creation Functionality")
public class TestCreateForm extends BaseTest {

    @Test
    @DisplayName("Verify the presence of Create New Form Btn")
    @Order(1)
    public void createNewFormBtnFunctionality() {
        $("#btnCreateForm").should(exist);
    }

    @Test
    @DisplayName("Click on createNewFormBtn and verify that Create new form wizard is opened")
    @Order(2)
    public void clickOnCreateFormBtnAndVerifyCreateFormWizard() {
       $("#btnCreateForm").should(exist).click();

        $("#wizardFormDlg").should(appear);
    }

    @Test
    @DisplayName("Verify which all fields and buttons are there in the form template")
    @Order(3)
    public void verifyTheFieldsInCreateFormWizard()
    {
        $("#wizard-formTitle").should(exist); //Title field
        $("#wizard-formHelp").should(exist); //Description field
        $("#wizard-formId").should(exist); //ID field
        $("#wizard-formUrl").should(exist); //Direct link to form Dashboard field
        $("#wizard-addlOptionsButton").should(exist); //Additional options button
        $("#wizard-cancelButton").should(exist); //Cancel button
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button and it should be disabled
        $("#wizard-backButton").shouldBe(disabled); //Back button and it should be disabled
    }

    @Test
    @DisplayName("Verify that Title and ID fields are mandatory in Create form wizard")
    @Order(4)
    public void verifyMandatoryFieldsInFormWizard()  {
        $("#wizard-formHelp").should(exist).setValue("xyz1"); //Enter text in description field
      //  $("#wizard-formUrl").should(exist).setValue("https://fireo.net/Dashboard/"); //Enter Direct link in Dashboard field

        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button should be disabled

        $("#wizard-formHelp").should(exist).setValue(""); //Clear text in description field
      //  $("#wizard-formUrl").should(exist).setValue(""); //Clear Direct link in Dashboard field

        $("#wizard-formTitle").should(exist).click();
        $("#wizard-formTitle").setValue("This is the form Title"); //Enter value in Title

        $("#wizard-formId").should(exist).doubleClick().sendKeys(Keys.BACK_SPACE); //Clearing the value in ID field

        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button should be disabled

        $("#wizard-formId").should(exist).click();
        $("#wizard-formId").setValue("UniqueId1"); //ID field

        $("#wizard-createFormButton").shouldBe(enabled); //Create Form button should be enabled

    }

    @Test
    @DisplayName("Verify Cancel functionality on Create Form Wizard")
    @Order(5)
    public void verifyCancelButtonInCreateForm()
    {
        $("#wizard-cancelButton").shouldBe(enabled).click(); //Cancel button

        $("#confirmation-dialog-title").should(exist);
        $("#btnCancel").shouldBe(enabled).click(); //Cancel the Cancel button on Confirmation
        $("#wizard-createFormButton").shouldBe(enabled); //User is on Create form Wizard

        $("#wizard-cancelButton").shouldBe(enabled).click(); //Cancel button
        $("#confirmation-dialog-title").should(exist);
        $("#btnCancel").shouldBe(enabled).click(); //Confirm the cancellation
        $("#btnCreateForm").should(exist); //Verify user is on dashboard page where Create Form button is visible

    }



    //Verify that error is shown if user tries to create form without entering mandatory fields

    //Verify that the fields in the form template is empty

    //Verify that user cannot enter more than 80 characters in the Title field, else error is shown

    //Verify that user cannot enter more than 150 characters in the Description field or else error is shown

    //Qn: Is there any character limit for the ID field ?

    //Qn: When the Create form button on Create form wizard is clicked, the further screens part needs to be discussed.

}