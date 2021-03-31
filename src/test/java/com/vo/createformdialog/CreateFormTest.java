package com.vo.createformdialog;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.Keys.*;

import org.apache.commons.lang3.RandomStringUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify New Form Creation Functionality")
public class CreateFormTest extends BaseTest {

    @Test
    @DisplayName("Verify the presence of Create New Form Btn on Dashboard")
    @Order(1)
    public void createNewFormBtnFunctionality() {
        setAppLanguageToEnglish(); //New
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
    @DisplayName("Verify Cancel functionality on Create Form Wizard")
    @Order(4)
    public void verifyCancelButtonInCreateForm()
    {
        $("#wizard-cancelButton").shouldBe(enabled).click(); //Cancel button

        $("#confirmation-dialog-title").should(exist);
        $("#btnCancel").shouldBe(enabled).click(); //Cancel the Cancel button on Confirmation

        $("#wizard-cancelButton").shouldBe(enabled).click(); //Cancel button
        $("#confirmation-dialog-title").should(exist);
        $("#btnConfirm").shouldBe(enabled).click(); //Confirm the cancellation
        $("#btnCreateForm").shouldBe(enabled);  //Verify user is on dashboard page where Create Form button is visible

    }

    @Test
    @DisplayName("Verify that Title and ID fields are mandatory in Create form wizard")
    @Order(5)
    public void verifyMandatoryFieldsInFormWizard()  {

        $("#btnCreateForm").should(exist).click(); //Click on Create form button on Dashboard page
        $("#wizard-formHelp").should(exist).setValue("xyz1"); //Enter text in description field
        $("#wizard-formTitle-helper-text").should(appear).shouldHave(text("Please insert the form title")); //Verify the Error shown below Title field - "Please insert the form title"
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button should be disabled since Title field is blank
        $("#wizard-formHelp").should(exist).doubleClick().sendKeys(Keys.BACK_SPACE); //Clear text in description field
        $("#wizard-formTitle").should(exist).click();
        $("#wizard-formTitle").setValue("This is the form Title"); //Enter value in Title field
        $("#wizard-formId").should(exist).doubleClick();
        $("#wizard-formId").sendKeys(Keys.BACK_SPACE); //Clearing the value in ID field
        $("#wizard-formId").should(exist).doubleClick();
        $("#wizard-formId").sendKeys(Keys.BACK_SPACE); //Clearing the value in ID field
        $("#wizard-formId").should(exist).shouldHave(exactValue(""));
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button should be disabled
        $("#wizard-formId").should(exist).click();
        $("#wizard-formId").setValue("UniqueId1"); //Enter value in ID field
        $("#wizard-createFormButton").shouldBe(enabled); //Create Form button should be enabled
    }

    @Test
    @DisplayName("Verify the character limit for Title field is 80 characters and for Description field is 150 characters")
    @Order(6)
    public void verifyTitleFieldCharacterLimit()
    {

//        $("#wizard-cancelButton").shouldBe(enabled).click(); //Cancel button
//        $("#confirmation-dialog-title").should(exist);
//        $("#btnConfirm").shouldBe(enabled).click(); //Confirm the cancellation
//        $("#btnCreateForm").shouldBe(enabled).click();  //Verify user is on dashboard page where Create Form button is visible

        String string80characters = RandomStringUtils.randomAlphanumeric(80);
        String newString = string80characters + "s";

        String titleText = $("#wizard-formTitle").getValue();

        if (!(titleText.equals(""))) {
            $("#wizard-formTitle").should(exist).setValue("");
            $("#wizard-formTitle").shouldBe(empty).pressTab();
        }

        $("#wizard-formTitle").should(exist).setValue(newString); //Try to set the new string with 81 characters in Title field
        $("#wizard-formTitle").shouldNotHave(exactValue(newString)); //New string with 81st character should not be present
        $("#wizard-formTitle").shouldHave(exactValue(string80characters)); //Only the string with 80 characters should be there

        String string150characters = RandomStringUtils.randomAlphanumeric(150);
        newString = string150characters + "s";

        $("#wizard-formHelp").doubleClick().sendKeys(Keys.BACK_SPACE); //Clear the Description field
        $("#wizard-formHelp").should(exist).setValue(newString); //Try to set 151 characters in the Description field
        $("#wizard-formHelp").shouldNotHave(exactValue(newString)); //New string with 151st character should not be present
        $("#wizard-formHelp").shouldHave(exactValue(string150characters)); //Only the string with 150 characters should be there

    }

    @Test
    @DisplayName("Verify Form Creation from Create Form Wizard")
    @Order(7)
    public void validateCreateFormFunctionality()
    {
        $("#wizard-createFormButton").should(exist).click(); //Click on create form btn in Wizard
       String formUrl =  $("#formtree_card").should(exist).getWrappedDriver().getCurrentUrl();
       System.out.println("The url for Create form is: "+formUrl);
       assertTrue(formUrl.contains("visualorbit.fireo.net/designer/")); //Verify that user has navigated to the form creation page
    }

    //Qn: Is there any character limit for the ID field ?

    //Qn: When the Create form button on Create form wizard is clicked, the further screens part needs to be discussed.

}