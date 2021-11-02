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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify Unod and Redo")
public class UndoRedoTest extends BaseTest {

    @Test
    @DisplayName("Create a new form and verify undo redo functions")
    public void verifyUndoRedo(){

        createNewForm(); //Create new form
        $("#wizard-createFormButton").should(exist).shouldBe(enabled).click(); //Click on Create Form
        $("#formDashboardHeaderLeft").should(appear);

        //Verifying undo function
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $("#block-loc_en-GB-r_1-c_1").should(exist).click();
        $("#template_card").should(appear).$("#li-template-Textfield-05").click(); //Add one field
        $("#block-loc_en-GB-r_1-c_1 .fa-pen").should(exist).click();
        $("#textfield_label").sendKeys(" 01 ");
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that previous version should increase
        $("#btnUndo").should(exist).click(); //Click on Undo button
        $("#block-loc_en-GB-r_1-c_1").should(exist).shouldNotHave(Condition.text("Text field 01")); //Text field should not exist

        //Verifying both undo and redo functions
        String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $("#block-loc_en-GB-r_1-c_3").should(exist).click();
        $("#template_card").should(appear).$("#li-template-TextareaField-06").click(); //Add 2nd field
        $("#block-loc_en-GB-r_1-c_3 .fa-pen").should(exist).click();
        $("#textfield_label").sendKeys(" 02 ");
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that previous version should increase
        $("#btnUndo").should(exist).click(); //Click on Undo button
        $("#block-loc_en-GB-r_1-c_3").should(exist).shouldNotHave(Condition.text("Textarea field 02")); //Textarea field should not exist
        $("#block-loc_en-GB-r_1-c_3").should(exist);
        $("#btnRedo").should(exist).click(); //Click on Undo button
        $("#block-loc_en-GB-r_1-c_3").should(exist).shouldHave(Condition.text("Textarea field 02")); //Textarea field should not exist
        String initialVerNumStr3 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $("#block-loc_en-GB-r_3-c_2").should(exist).click();
        $("#template_card").should(appear).$("#li-template-CheckboxGroupField-04").click(); //Add 3rd field
        $("#block-loc_en-GB-r_3-c_2 .fa-pen").should(exist).click();
        $("#formelement_properties_card .editForm").should(exist).click();
        $("#form-value-list-card-dialog_actions").should(appear);

        //Preselect a value
        String checkboxSelector = "div.ag-pinned-left-cols-container .ag-row:nth-child(2) input";
        $(checkboxSelector).should(exist).click();
        $(checkboxSelector).shouldBe(checked);
        $("#form-value-list-card-dialog_actions .fa-times").should(exist).click();
        $("#btnUndo").should(exist).click(); //Click on Undo button
        $("#block-loc_en-GB-r_3-c_2 .Mui-disabled:nth-child(2)").should(not(checked)); //Preselected value should be unchecked
        $(".fa-undo").should(exist).click(); //Click on Undo button
        $("#formMinorversion").shouldHave(text(initialVerNumStr3)); //Verify that previous version should increase
        $("#block-loc_en-GB-r_3-c_2").should(exist).shouldNotHave(Condition.text("Checkbox group")); //Checkboxgroup field should not exist
        $("#btnRedo").should(exist).click(); //Click on Redo button
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr3)); //Verify that previous version should increase
        $("#block-loc_en-GB-r_3-c_2").should(exist).shouldHave(Condition.text("Checkbox group")); //Checkboxgroup field  field should not exist
        $(".fa-redo").should(exist).click(); //Click on Redo button
        $("#block-loc_en-GB-r_3-c_2 .MuiFormControlLabel-root:nth-child(2) input").shouldBe(checked); //Preselected value should be checked

    }
}
