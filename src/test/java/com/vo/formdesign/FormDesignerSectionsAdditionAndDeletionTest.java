package com.vo.formdesign;

import com.vo.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the sections addition and deletion")
public class FormDesignerSectionsAdditionAndDeletionTest extends BaseTest {

    @Test
    @DisplayName("Verify the sections addition and deletion")
    public void addAndRemoveSections(){

        createNewForm();
        $(elementLocators("CreateFormButton")).should(exist).click();
        $(elementLocators("PublishButton")).should(exist);
        $(elementLocators("DesignerContent")).should(exist);
        $(elementLocators("Section1")).should(exist);
        $(elementLocators("Section1PenIcon")).click();
        $(elementLocators("TextFieldLabel")).should(exist).setValue(" 01 ");//Add name to the section
        $(elementLocators("BlockR1C1")).should(exist).click();
        $(elementLocators("TextField")).should(appear).click();
        $(elementLocators("PlusIconToAddSection")).should(exist).click(); //Click on + icon to add new section
        //#designer_formCardContent span div:nth-child(1) :: Previous ID to add new section
        $(elementLocators("SectionR5C1")).should(exist); //New section should be visible
        $(elementLocators("SectionR5C1PlusIcon")).should(exist).click(); //Click on + to add another section
        $(elementLocators("SectionR9C1")).should(exist); //New Section added
        $(elementLocators("TextFieldLabel")).should(exist).setValue(" 02 ");//Add name to the section
        $(elementLocators("SectionR9C1")).$(elementLocators("BlockR1C1")).should(exist).click();
        $(elementLocators("TextField")).should(appear).click();

        //Deletion of added sections
        $(elementLocators("Section1")).should(exist);
        $(elementLocators("Section1PenIcon")).should(exist).click(); //Click on edit
        $(elementLocators("DeleteBlockBtn")).should(exist).click(); //Click on Delete button

        $(elementLocators("SectionR5C1")).should(exist);
        $(elementLocators("SectionR5CPenIcon")).should(exist).click(); //Click on edit
        $(elementLocators("DeleteBlockBtn")).should(exist).click(); //Click on Delete button

        $(elementLocators("Section1")).should(exist); //Section 1 should exist
        $(elementLocators("BlockR1C1")).should(exist).click();

    }
}
