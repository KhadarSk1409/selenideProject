package com.vo.checklist;

import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify checklist creation by guitest user")
public class CreateNewChecklistTest extends BaseTest {

    @Test
    @DisplayName("Create checklist from library")
    public void NewChecklistCreation(){
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("CreateNewChecklistButton")).should(exist).click();
        String ChecklistTitle = RandomStringUtils.randomAlphanumeric(5);
        $(elementLocators("FormTitleInputField")).setValue(ChecklistTitle); //Set Title
        String ChecklistID = RandomStringUtils.randomAlphanumeric(5);
        $(elementLocators("IdInputField")).setValue(ChecklistID);

        $(elementLocators("createChecklistTemplateBtn")).should(exist).click();
        $(byText("Checklist Flow")).should(appear);
        //Move the Form Element from components to checklist flow and create a new form
        $(elementLocators("SourceFormElement")).should(exist).doubleClick();

        $(byText(elementLocators("SelectAForm"))).should(appear); // Form selection wizard will appear
        $(byText(elementLocators("CreateANewForm"))).click(); //Click on Create a New Form
        $(elementLocators("FormCreationWizard")).should(appear); //New form creation wizard will appear
        String FormName = RandomStringUtils.randomAlphanumeric(5);
        $(elementLocators("FormTitleInputField")).should(exist).setValue(FormName); //Enter Form Title
        String FormDescription = RandomStringUtils.randomAlphanumeric(5);
        $(elementLocators("DescriptionInputField")).setValue(FormDescription); //Enter Form Description
        $(elementLocators("CreateNewFormButton")).should(exist).shouldBe(enabled).click(); //Click on Create Form
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(FormName)); //Verify whether the created form is available in the Checklist flow or not

        //Drag and Drop LABEL field to Checklist flow
        $(elementLocators("SourceLabelElement")).should(exist).doubleClick();


    }
}
