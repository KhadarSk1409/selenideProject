package com.vo.createformdialog;

import com.codeborne.selenide.Configuration;
import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.Keys.TAB;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Create Form Without Additional Options")
public class CreateFormWithoutAdditionalOptionsTest extends BaseTest {

    @Test
    @DisplayName("Should open Create Form Wizard Dialog")
    @Order(1)
    public void createNewFormBtnFunctionality() {
        $("#btnCreateForm").should(exist).click();
        $("#wizardFormDlg").should(appear);
    }

    @Test
    @DisplayName("Verify main form properties")
    @Order(3)
    public void verifyTheFieldsInCreateFormWizard() {
        $("#wizard-formTitle").should(exist, focused); //Title field
        $("#wizard-formHelp").should(exist); //Description field
        $("#wizard-formId").should(exist); //ID field
        $("#selectFormIcon svg").should(have(attribute("data-src", "/images/noun/visualOrbit.svg")));
        $("#selectFormIcon").click();
        $("#selectFormIcon_dialog_content").should(appear); //Verify ICON PICKER pop up is available
        $$("#selectFormIcon_dialog_content span").find(attribute("title", "noun_Business Man_919296")).click();
        $$("#selectFormIcon").contains(attribute("data-src", "/images/noun/noun_Business Man_919296.svg"));
        $("#wizard-formUrl").should(exist); //Direct link to form Dashboard field
        $("#wizard-addlOptionsButton").should(exist).shouldBe(disabled); //Additional options button
        $("#wizard-cancelButton").should(exist).shouldBe(enabled); //Cancel button should be enabled
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button and it should be disabled
        $("#wizard-backButton").shouldBe(disabled); //Back button and it should be disabled

        $("#wizard-formId").shouldNotBe(empty); //Validate that ID field should not be empty
        String idText = $("#wizard-formId").getValue();

        $("#wizard-formUrl").shouldNotBe(empty); //Url field should not be empty

        String expectedUrl = Configuration.baseUrl + "/Dashboard/" + idText;
        String actualUrl = $("#wizard-formUrl").getValue();

        System.out.println("The Direct link to form dashboard is: " + actualUrl);

        $("#wizard-formUrl").shouldHave(value(expectedUrl)); //The url which should be there in url field

        applyLabelForTestForms();
    }

    @Test
    @DisplayName("Verify Cancel")
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
        $("#wizard-formTitle-helper-text").should(appear).shouldHave(text("Provide a title for the new form, you can always change this")); //Verify the Error shown below Title field - "Please insert the form title"
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

        String formprefix = "test-gu-";
        String string80characters = formprefix+RandomStringUtils.randomAlphanumeric(72);
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
        applyLabelForTestForms();
        $("#wizard-createFormButton").should(exist).click(); //Click on create form btn in Wizard
        String formUrl = $("#formtree_card").should(exist).getWrappedDriver().getCurrentUrl();
        System.out.println("The url for Create form is: " + formUrl);

        String expectedUrl = Configuration.baseUrl + "/designer/" + idText;
        $("#formtree_card").getText().contains(expectedUrl); //Verify that user has navigated to the form creation page
        $("#toDashboard").click(); //Go back to Dashboard

        $("#btnCreateForm").should(exist).click(); //Click on Create Form button
        $("#wizardFormDlg").should(appear); //Create Form wizard appears
        $("#wizard-formId-helper-text").should(exist);
        selectAndClear("#wizard-formId").setValue(idText).sendKeys(TAB); //Set the id which was there for previous form

        $("#wizard-formId-helper-text").should(exist).shouldHave(text("The Id exists already")); //Error should be shown

    }


}