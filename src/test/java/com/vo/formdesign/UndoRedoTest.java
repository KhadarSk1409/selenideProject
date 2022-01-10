package com.vo.formdesign;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify Unod and Redo")
public class UndoRedoTest extends BaseTest {

    @Test
    @DisplayName("Create a new form and verify undo redo functions")
    public void verifyUndoRedo(){

        createNewForm(); //Create new form
        $(elementLocators("CreateFormButton")).should(exist).shouldBe(enabled).click(); //Click on Create Form
        $(elementLocators("LeftFormDashboardHeader")).should(appear);

        //Verifying undo function
        String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(elementLocators("BlockR1C1")).should(exist).click();
        $(elementLocators("TemplateCard")).should(appear).$(elementLocators("TextField")).click(); //Add one field
        $(elementLocators("BlockR1C1PenIcon")).should(exist).click();
        $(elementLocators("TextFieldLabel")).sendKeys(" 01 ");
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that previous version should increase
        $(elementLocators("UndoButton")).should(exist).click(); //Click on Undo button
        $(elementLocators("BlockR1C1")).should(exist).shouldNotHave(Condition.text("Text field 01")); //Text field should not exist

        //Verifying both undo and redo functions once
        String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(elementLocators("BlockR1C3")).should(exist).click();
        $(elementLocators("TemplateCard")).should(appear).$(elementLocators("TextAreaField")).click(); //Add 2nd field
        $(elementLocators("BlockR1C3PenIcon")).should(exist).click();
        $(elementLocators("TextFieldLabel")).sendKeys(" 02 ");
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that previous version should increase
        $(elementLocators("UndoButton")).should(exist).click(); //Click on Undo button
        $(elementLocators("BlockR1C3")).should(exist).shouldNotHave(Condition.text("Textarea field 02")); //Textarea field should not exist
        $(elementLocators("BlockR1C3")).should(exist);
        $(elementLocators("RedoButton")).should(exist).click(); //Click on Undo button
        $(elementLocators("BlockR1C3")).should(exist).shouldHave(Condition.text("Textarea field 02")); //Textarea field should not exist

        //Verifying both undo and redo functions twice
        String initialVerNumStr3 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(elementLocators("BlockR3C2")).should(exist).click();
        $(elementLocators("TemplateCard")).should(appear).$("#li-template-CheckboxGroupField-04").click(); //Add 3rd field
        $(elementLocators("BlockR3C2PenIcon")).should(exist).click();
        $(elementLocators("EditValuesPenIcon")).should(exist).click();
        $(elementLocators("ValuesListEditor")).should(appear);
        //Preselect a value
        String checkboxSelector = "#myGrid .MuiDataGrid-row:nth-child(2) div:nth-child(1) span input";
        $(checkboxSelector).should(exist).click();
        $(checkboxSelector).shouldBe(checked);
        $(elementLocators("CloseValuesEditorBtn")).should(exist).click();
        $(elementLocators("UndoButton")).should(exist).click(); //Click on Undo button
        $(elementLocators("SecondCheckBox")).should(not(checked)); //Preselected value should be unchecked
        $(elementLocators("UndoButton")).should(exist).click(); //Click on Undo button
        $(elementLocators("InitialVersion")).shouldHave(text(initialVerNumStr3)); //Verify that previous version should increase
        $(elementLocators("BlockR3C2")).should(exist).shouldNotHave(Condition.text("Checkbox group")); //Checkboxgroup field should not exist
        $(elementLocators("RedoButton")).should(exist).click(); //Click on Redo button
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr3)); //Verify that previous version should increase
        $(elementLocators("BlockR3C2")).should(exist).shouldHave(Condition.text("Checkbox group")); //Checkboxgroup field  field should not exist
        $(elementLocators("RedoButton")).should(exist).click(); //Click on Redo button
        $(elementLocators("SecondCheckBox")).shouldBe(checked); //Preselected value should be checked

    }
}
