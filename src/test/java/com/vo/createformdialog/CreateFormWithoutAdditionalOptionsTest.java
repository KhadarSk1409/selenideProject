package com.vo.createformdialog;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static reusables.ReuseActions.elementLocators;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Create Form Without Additional Options")
public class CreateFormWithoutAdditionalOptionsTest extends BaseTest {

    @Test
    @DisplayName("Should open Create Form Wizard Dialog")
    @Order(1)
    public void createNewFormBtnFunctionality() {
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("CreateNewFormButton")).should(exist).click();
        $(elementLocators("FormCreationWindow")).should(appear);
    }

    @Test
    @DisplayName("Verify main form properties")
    @Order(3)
    public void verifyTheFieldsInCreateFormWizard() {
        $(elementLocators("FormTitleInputField")).should(exist, focused); //Title field
        $(elementLocators("DescriptionInputField")).should(exist); //Description field
        $(elementLocators("IdInputField")).should(exist); //ID field
        $(elementLocators("FormUrl")).should(exist); //Direct link to form Dashboard field
        $(elementLocators("AdditionalOptionsButton")).should(exist).shouldBe(disabled); //Additional options button
        $(elementLocators("CancelButton")).should(exist).shouldBe(enabled); //Cancel button should be enabled
        $(elementLocators("CreateFormButton")).shouldBe(disabled); //Create Form button and it should be disabled
        $(elementLocators("BackButton")).shouldBe(disabled); //Back button and it should be disabled

        $(elementLocators("IdInputField")).shouldNotBe(empty); //Validate that ID field should not be empty
        String idText = $(elementLocators("IdInputField")).getValue();

        $(elementLocators("FormUrl")).shouldNotBe(empty); //Url field should not be empty

        String expectedUrl = Configuration.baseUrl + "/Dashboard/" + idText;
        String actualUrl = $(elementLocators("FormUrl")).getValue();

        System.out.println("The Direct link to form dashboard is: " + actualUrl);

        $(elementLocators("FormUrl")).shouldHave(value(expectedUrl)); //The url which should be there in url field

        applyLabelForTestForms();
    }

    @Test
    @DisplayName("Verify Cancel")
    @Order(4)
    public void verifyCancelButtonInCreateForm() {
        $(elementLocators("CancelButton")).shouldBe(enabled).click(); //Cancel button
        $(elementLocators("CancelConfirmationDialog")).should(appear);
        $(elementLocators("ButtonCancel")).shouldBe(enabled).click(); //Cancel the Cancel button on Confirmation
        $(elementLocators("CancelButton")).shouldBe(enabled).click(); //Cancel button
        $(elementLocators("CancelConfirmationDialog")).should(exist);
        $(elementLocators("ConfirmCancel")).shouldBe(enabled).click(); //Confirm the cancellation
        $(elementLocators("CreateNewFormButton")).shouldBe(enabled);  //Verify user is on dashboard page where Create Form button is visible

    }

    @Test
    @DisplayName("Verify that Title and ID fields are mandatory in Create form wizard")
    @Order(5)
    public void verifyMandatoryFieldsInFormWizard() {
        $(elementLocators("CreateNewFormButton")).should(exist).click(); //Click on Create form button on Dashboard page
        $(elementLocators("DescriptionInputField")).should(exist).setValue("xyz1"); //Enter text in description field
        $(elementLocators("TitleHelperText")).should(exist).shouldHave(text("Please insert the form title")); //Verify the Error shown below Title field - "Please insert the form title"
        $(elementLocators("CreateFormButton")).shouldBe(disabled); //Create Form button should be disabled since Title field is blank
        selectAndClear(elementLocators("DescriptionInputField"));
        selectAndClear(elementLocators("FormTitleInputField")).setValue("This is the form Title"); //Enter value in Title field
        selectAndClear(elementLocators("IdInputField")).should(exist).shouldBe(empty);
        $(elementLocators("CreateFormButton")).shouldBe(disabled); //Create Form button should be disabled
        $(elementLocators("IdInputField")).setValue("UniqueId1"); //Enter value in ID field
        $(elementLocators("CreateFormButton")).shouldBe(enabled); //Create Form button should be enabled

    }

    @Test
    @DisplayName("Verify the character limit for Title field is 80 characters and for Description field is 150 characters")
    @Order(6)
    public void verifyTitleFieldCharacterLimit() {
        $(elementLocators("CancelButton")).shouldBe(enabled).click(); //Cancel button
        $(elementLocators("CancelConfirmationDialog")).should(exist);
        $(elementLocators("ConfirmCancel")).shouldBe(enabled).click(); //Confirm the cancellation
        $(elementLocators("CreateNewFormButton")).shouldBe(enabled).click();  //Verify user is on dashboard page where Create Form button is visible
        $(elementLocators("FormCreationWindow")).should(appear); //Create form wizard should exist

        String formprefix = "test-gu-";
        String string80characters = formprefix+RandomStringUtils.randomAlphanumeric(72);
        String newString = string80characters + "s";

        selectAndClear(elementLocators("FormTitleInputField")).setValue(newString); //Try to set the new string with 81 characters in Title field
        $(elementLocators("FormTitleInputField")).shouldNotHave(exactValue(newString)); //New string with 81st character should not be present
        $(elementLocators("FormTitleInputField")).shouldHave(exactValue(string80characters)); //Only the string with 80 characters should be there

        String string150characters = RandomStringUtils.randomAlphanumeric(150);
        newString = string150characters + "s";

        selectAndClear(elementLocators("DescriptionInputField")).setValue(newString); //Try to set 151 characters in the Description field
        $(elementLocators("DescriptionInputField")).shouldNotHave(exactValue(newString)); //New string with 151st character should not be present
        $(elementLocators("DescriptionInputField")).shouldHave(exactValue(string150characters)); //Only the string with 150 characters should be there

    }

    @Test
    @DisplayName("Verify Form Creation from Create Form Wizard")
    @Order(7)
    public void validateCreateFormFunctionality() {
        String idText = $(elementLocators("IdInputField")).getValue();
        System.out.println("The ID for first form is: " + idText); //ID value for first form
        applyLabelForTestForms();
        $(elementLocators("CreateFormButton")).should(exist).click(); //Click on create form btn in Wizard
        String formUrl = $(elementLocators("FormStructure")).should(exist).getWrappedDriver().getCurrentUrl();
        System.out.println("The url for Create form is: " + formUrl);
        String expectedUrl = Configuration.baseUrl + "/designer/" + idText;
        $(elementLocators("FormStructure")).getText().contains(expectedUrl); //Verify that user has navigated to the form creation page
        $(elementLocators("Launchpad")).click(); //Go back to Dashboard
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("CreateNewFormButton")).should(exist).click(); //Click on Create Form button
        $(elementLocators("FormCreationWindow")).should(appear); //Create Form wizard appears
        $(elementLocators("IdHelperText")).should(exist);
        selectAndClear(elementLocators("IdInputField")).setValue(idText).pressTab(); //Set the id which was there for previous form
        $(elementLocators("IdHelperText")).shouldHave(Condition.text("The Id exists already")); //Error should be shown

    }
}