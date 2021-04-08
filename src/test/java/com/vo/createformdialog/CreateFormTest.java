package com.vo.createformdialog;

import com.codeborne.selenide.Configuration;
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
    public void verifyTheFieldsInCreateFormWizard() {
        $("#wizard-formTitle").should(exist, focused); //Title field
        $("#wizard-formHelp").should(exist); //Description field
        $("#wizard-formId").should(exist); //ID field
        $("#selectFormIcon svg").should(have(attribute("data-src", "/images/noun/visualOrbit.svg")));
        $("#selectFormIcon").click();
        $("#selectFormIcon_dialog_content").should(appear); //Verify ICON PICKER pop up is available
        $$("#selectFormIcon_dialog_content button .material-icons").find(attribute("iconname", "fas fa-address-book")).click();
        $$("#selectFormIcon").contains(attribute("iconname", "fas fa-address-book"));
        $("#wizard-formUrl").should(exist); //Direct link to form Dashboard field
        $("#wizard-addlOptionsButton").should(exist).shouldBe(disabled); //Additional options button
        $("#wizard-cancelButton").should(exist).shouldBe(enabled); //Cancel button should be enabled
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button and it should be disabled
        $("#wizard-backButton").shouldBe(disabled); //Back button and it should be disabled

        String idText = $("#wizard-formId").getValue();
        assertTrue(!(idText.isEmpty()));

        String directLink = $("#wizard-formUrl").getValue();
        assertTrue(!(directLink.isEmpty()));

        String urlInID = Configuration.baseUrl + "/Dashboard/" + idText;
        System.out.println("The url in ID field is: " + urlInID);
        assertTrue(directLink.contains(urlInID));

    }

    @Test
    @DisplayName("Verify Cancel functionality on Create Form Wizard")
    @Order(4)
    public void verifyCancelButtonInCreateForm() {
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
    public void verifyMandatoryFieldsInFormWizard() {

        $("#btnCreateForm").should(exist).click(); //Click on Create form button on Dashboard page
        $("#wizard-formHelp").should(exist).setValue("xyz1"); //Enter text in description field
        $("#wizard-formTitle-helper-text").should(appear).shouldHave(text("Please insert the form title")); //Verify the Error shown below Title field - "Please insert the form title"
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button should be disabled since Title field is blank
        selectAndClear("#wizard-formHelp");
        selectAndClear("#wizard-formTitle").setValue("This is the form Title"); //Enter value in Title field
        selectAndClear("#wizard-formId").should(exist).shouldBe(empty);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button should be disabled
        $("#wizard-formId").setValue("UniqueId1"); //Enter value in ID field
        $("#wizard-createFormButton").shouldBe(enabled); //Create Form button should be enabled

    }


    @Test
    @DisplayName("Verify the character limit for Title field is 80 characters and for Description field is 150 characters")
    @Order(6)
    public void verifyTitleFieldCharacterLimit() {

        $("#wizard-cancelButton").shouldBe(enabled).click(); //Cancel button
        $("#confirmation-dialog-title").should(exist);
        $("#btnConfirm").shouldBe(enabled).click(); //Confirm the cancellation
        $("#btnCreateForm").shouldBe(enabled).click();  //Verify user is on dashboard page where Create Form button is visible
        $("#wizardFormDlg").should(appear); //Create form wizard should exist

        String string80characters = RandomStringUtils.randomAlphanumeric(80);
        String newString = string80characters + "s";

        selectAndClear("#wizard-formTitle").setValue(newString); //Try to set the new string with 81 characters in Title field
        $("#wizard-formTitle").shouldNotHave(exactValue(newString)); //New string with 81st character should not be present
        $("#wizard-formTitle").shouldHave(exactValue(string80characters)); //Only the string with 80 characters should be there

        String string150characters = RandomStringUtils.randomAlphanumeric(150);
        newString = string150characters + "s";

        selectAndClear("#wizard-formHelp").setValue(newString); //Try to set 151 characters in the Description field
        $("#wizard-formHelp").shouldNotHave(exactValue(newString)); //New string with 151st character should not be present
        $("#wizard-formHelp").shouldHave(exactValue(string150characters)); //Only the string with 150 characters should be there

    }

    @Test
    @DisplayName("Verify Form Creation from Create Form Wizard")
    @Order(7)
    public void validateCreateFormFunctionality() {

        String idText = $("#wizard-formId").getValue();
        System.out.println("The ID for first form is: " + idText); //ID value for first form

        $("#wizard-createFormButton").should(exist).click(); //Click on create form btn in Wizard
        String formUrl = $("#formtree_card").should(exist).getWrappedDriver().getCurrentUrl();
        System.out.println("The url for Create form is: " + formUrl);

        String expectedUrl = Configuration.baseUrl + "/designer/";
        assertTrue(formUrl.contains(expectedUrl)); //Verify that user has navigated to the form creation page
        $("#toDashboard").click(); //Go back to Dashboard
        $("#btnCreateForm").should(exist).click(); //Click on Create Form button
        $("#wizardFormDlg").should(appear); //Create Form wizard appears
        $("#wizard-formId-helper-text").should(exist);
        selectAndClear("#wizard-formId"); //#wizard-formId
        selectAndClear("#wizard-formId").setValue(idText); //Set the id which was there for previous form

        $("#wizard-formId-helper-text").should(exist).shouldHave(text("The Id exists")); //Error should be shown
        // System.out.println("The error shown when tried to enter used Form Id is: "+$("#wizard-formId-helper-text").should(exist).getText());
    }

    //Qn: When the Create form button on Create form wizard is clicked, the further screens part needs to be discussed.

}