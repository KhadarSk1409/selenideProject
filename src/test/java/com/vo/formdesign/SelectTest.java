package com.vo.formdesign;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.commands.PressEnter;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Select Tests")
public class SelectTest extends BaseTest {

    public enum SelectIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        numberField_minCount,
        numberField_maxCount,
        checkbox_multiple;
    }

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() {
        navigateToFormDesign(FormField.SELECT_TEST);

    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForSelect")
    @ParameterizedTest
    @CsvFileSource(resources = "/select_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String text_label,
                             String text_help,
                             String disableLabel,
                             String edit_values,
                             String preselection_value,
                             String checkbox_required,
                             String checkbox_allow_multiple,
                             String text_numberField_minCount,
                             String text_numberField_maxCount


    ) {
        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#li-template-SelectField-05").should(appear).click();
        $("#formelement_properties_card").should(appear);
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased

        if (colSpan != null && colSpan > 1) {
            int prevWidth = $(blockId).getRect().getWidth();
            IntStream.range(1, colSpan).forEach(c -> {
                String initialVerNumStr1 = $("#formMinorversion").should(exist).getText();
                $("#blockButtonExpand").shouldBe(visible).click();
                $("#formMinorversion").shouldNotHave(text(initialVerNumStr1));
            });
            int currWidth = $(blockId).getRect().getWidth();
            Assertions.assertEquals(colSpan, currWidth / prevWidth, "block column span should be " + colSpan);
        }

        //Label
        if (StringUtils.isNotEmpty(text_label)) {
            labelVerificationOnFormDesign(blockId, text_label);
        }


        //Help
        if (StringUtils.isNotEmpty(text_help)) {
            helpVerificationOnFormDesign(blockId, text_help);
        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, text_label);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        //Values
        if (StringUtils.isNotEmpty(edit_values)) {

            String[] values = edit_values.split(",");

            $("#formelement_properties_card .editForm").should(exist).click(); //Click on edit value pen icon
            $("#form-value-list-card-dialog_content").should(exist); //Value List Editor window

            //Deleting the existing rows:
            List<SelenideElement> delBtn = $$("div.ag-pinned-right-cols-container .ag-row .fa-trash-alt");
            int countDelBtn = $$("div.ag-pinned-right-cols-container .ag-row .fa-trash-alt").size();
            for (int n = countDelBtn; n >= 1; n--) {
                String strDeleteBtn = ".ag-row:nth-child(" + n + ") .fa-trash-alt"; //Delete the n th row
                $(strDeleteBtn).click();
                $(strDeleteBtn).waitUntil(disappear, 10000);
            }

            //Add rows in value list editor for the number of labels
            if (!$("div.ag-pinned-right-cols-container .ag-row").exists()) {
                for (int x = 0; x < values.length; x++) {
                    $("#value_list_values button .fa-plus").should(exist).click();
                }
            }

            List<String> preselected = new ArrayList<>();
            if (StringUtils.isNotEmpty(preselection_value)) {
                preselected = Arrays.asList(preselection_value.split(","));
            }

            for (int i = 1; i <= values.length; i++) {
                //Click on label option
                String labelSelector = "div.ag-body-viewport .ag-center-cols-viewport .ag-row:nth-child(" + i + ") .ag-cell:nth-child(2)";
                $(labelSelector).should(exist).doubleClick();
                String labelValue = values[i - 1];
                $("div.ag-popup input.ag-input-field-input").sendKeys(Keys.BACK_SPACE); //Clear the default value in label field
                $("div.ag-popup input.ag-input-field-input").setValue(labelValue).sendKeys(Keys.ENTER);
                $(labelSelector).shouldHave(text(labelValue));

                if (preselected.contains(labelValue)) {
                    String checkboxSelector = "div.ag-pinned-left-cols-container .ag-row:nth-child(" + i + ") input";
                    $(checkboxSelector).should(exist).click();
                    $(checkboxSelector).shouldBe(checked);
                }
            }

            //Click on close button
            $("#form-value-list-card-dialog_actions #btnClosePropertiesForm").should(exist).click();

            //verify preselection on designer surface
            $(blockId).should(exist);
            for (int i = 1; i <= values.length; i++) {
                String labelValue = values[i - 1];
                if (preselected.contains(labelValue)) {
                    $(blockId).find(".MuiInputBase-root").shouldHave(text(labelValue));
                } else {
                    $(blockId).find(".MuiInputBase-root").shouldNotHave(text(labelValue));
                }
            }
        }

        //Allow Multiple:
        if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + SelectTest.SelectIds.checkbox_multiple.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            //Now the Minimum and Maximum count field should be enabled:
            $("#numberField_minCount").should(exist).shouldBe(enabled);
            $("#numberField_maxCount").should(exist).shouldBe(enabled);
        }

        //Enter Minimum Value
        if (StringUtils.isNotEmpty(text_numberField_minCount)) {
            $("#formelement_properties_card .editForm").should(exist).click(); //Click on edit value pen icon
            $("#form-value-list-card-dialog_content").should(exist); //Value List Editor window

            List<SelenideElement> rowsInListEditor = $$("#myGrid div.ag-center-cols-container div.ag-row"); //fetch number of rows in List editor
            int rowsCount = rowsInListEditor.size();

            //Click on close button
            $("#form-value-list-card-dialog_actions #btnClosePropertiesForm").should(exist).click();

            //Click on Allow multiple checkbox:
            String checkBoxId = "#" + SelectTest.SelectIds.checkbox_multiple.name();
            $(checkBoxId).shouldBe(visible).click();

            $(By.id(SelectTest.SelectIds.numberField_minCount.name())).should(exist); //Verify that Minimum count field exists

            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(SelectTest.SelectIds.numberField_minCount.name()))
                    .setValue(text_numberField_minCount).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            $("#numberField_minCount").shouldHave(value(text_numberField_minCount)).waitUntil(appears, 4000);

            int int_text_numberField_minCount = parseInt(text_numberField_minCount);

            //Verify that if the Min count is less than rowCount, then error should be shown
            if (int_text_numberField_minCount < rowsCount) {
                String errorMinCount1 = "The values count " + rowsCount + " is less than minimum count " + text_numberField_minCount;
                $("#panel1a-content div:nth-child(5) p.Mui-error").should(exist).shouldHave(text(errorMinCount1));
            }
        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(text_numberField_maxCount)) {
            if (!($(By.id(SelectTest.SelectIds.numberField_maxCount.name())).isEnabled())) {
                $("#checkbox_multiple").click();
            }

            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(SelectTest.SelectIds.numberField_maxCount.name()))
                    .setValue(text_numberField_maxCount).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            $("#numberField_maxCount").shouldHave(value(text_numberField_maxCount)).waitUntil(appears, 4000);

            //Verify that if Max count is less than Min count, relevant errors should be shown:
            if (StringUtils.isNotEmpty(text_numberField_minCount)) {
                selectAndClear(By.id(SelectIds.numberField_minCount.name()))
                        .setValue(text_numberField_minCount).sendKeys(Keys.TAB); //Enter the value in Min count field

                int int_text_numberField_minCount = parseInt(text_numberField_minCount);
                int int_text_numberField_maxCount = parseInt(text_numberField_maxCount);
                if (int_text_numberField_minCount > int_text_numberField_maxCount) {
                    String errorMaxCount1 = "The maximum value " + text_numberField_maxCount + " is less than minimum value " + text_numberField_minCount;
                    $("#panel1a-content div:nth-child(5) p.Mui-error").should(exist).shouldHave(text(errorMaxCount1));

                    String errorMaxCount2 = "The maximum value " + text_numberField_maxCount + " is less than minimum value " + text_numberField_minCount;
                    $("#panel1a-content div:nth-child(5) p.Mui-error").should(exist).shouldHave(text(errorMaxCount2));

                    //After verifying the error message, the max count value will be changed so that it is valid and errors will not appear
                    selectAndClear(By.id(SelectTest.SelectIds.numberField_maxCount.name()))
                            .setValue(String.valueOf(int_text_numberField_minCount)).sendKeys(Keys.TAB);
                    $("#numberField_maxCount").shouldHave(Condition.value(String.valueOf(int_text_numberField_minCount)));
                }
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Publish and open the FormPage")
    public void publishAndOpenFormPage() {
        //Click on publish button, wait until form dashboard opens and click on fill form
        $("#btnFormDesignPublish").should(exist).click();
        $("#form-publish-dialog .MuiPaper-root").should(appear); //Publish confirmation dialog appears
        $("#form-publish-dialog  #btnConfirm").should(exist).click(); //Click on Confirm button
        $("#btnCreateNewData").should(exist).click(); //Fill form button on Launch screen
        $("#dataContainer").should(appear); //Verify that the form details screen appears
    }

    @Order(4)
    @DisplayName("Verify fields on the form")
    @ParameterizedTest
    @CsvFileSource(resources = "/select_field_test_data.csv", numLinesToSkip = 1)
    public void verifyFieldsOnForm(Integer row, Integer col, Integer colSpan,
                                   String text_label,
                                   String text_help,
                                   String disableLabel,
                                   String edit_values,
                                   String preselection_value,
                                   String checkbox_required,
                                   String checkbox_allow_multiple,
                                   String text_numberField_minCount,
                                   String text_numberField_maxCount) {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";
        String helpInFillForm = blockStr + " .MuiFormHelperText-root";
        String valuesInFillForm = blockStr + " .MuiTextField-root input";
        String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";
        String ClickOnDropDownInFillForm = blockStr + " .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator";
        String inputInFillForm = blockStr + " input";

        //Label
        if (StringUtils.isNotEmpty(text_label)) {
            System.out.println("Verifying label: " + text_label);
            if (StringUtils.isNotEmpty(disableLabel)) {
                $(labelInFillForm).shouldNotHave(text(text_label)); //Verify that Label should not appear on the form - hide label
            } else {
                $(labelInFillForm).shouldHave(text(text_label)); //Verify that Label appears on the form
            }
        }

        //Help
        if (StringUtils.isNotEmpty(text_help)) {
            System.out.println("Verifying help: " + text_help);
            $(helpInFillForm).shouldHave(text(text_help));
        }

        //Values
        if (StringUtils.isNotEmpty(edit_values)) {
            System.out.println("Verifying edited values: " + edit_values);
            String[] strEditValues = edit_values.split(",");
            Arrays.asList(strEditValues).forEach(s -> $(ClickOnDropDownInFillForm).should(exist).click());
            $(valuesInFillForm).should(exist);

        }

        //Preselection values
        if (StringUtils.isNotEmpty(preselection_value)) {
            System.out.println("Verifying Preselection value: " + preselection_value);
            String[] preSelectedValues = edit_values.split(",");
            int i = (Arrays.asList(preSelectedValues).indexOf(preselection_value));
            String selectedValue = blockStr + " .MuiTextField-root input:nth-child(" + (i + 2) + ")";
            Arrays.asList(preSelectedValues).forEach(s -> $(selectedValue).should(exist));
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verifying required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        //Allow multiple checkbox
        if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {
            System.out.println("Verifying Allow multiple: " + checkbox_allow_multiple);

            int k = (Arrays.asList(valuesInFillForm).indexOf(edit_values));
            String inputField = blockStr + " input:nth-child("+(k+2)+")";
            $(inputField).should(exist).click();

            List<SelenideElement> valuesAvailableInInput = $$(blockStr + " .MuiAutocomplete-option"); //Fetch the options available
             System.out.println(valuesAvailableInInput);
            for (int i = 1; i <= valuesAvailableInInput.size(); i++) {

                $(blockStr).find(" input:nth-child(" + i + ") .MuiAutocomplete-listbox .MuiAutocomplete-option").should(exist).click();
            }

            //Min count
            if (StringUtils.isNotEmpty(text_numberField_minCount)) {
                System.out.println("Verifying Minimum Count: " + text_numberField_minCount);
                int x = (Arrays.asList(valuesInFillForm)).indexOf(edit_values);
                String inputField2 = blockStr + " input:nth-child(" + (x + 2) + ")";
                $(inputInFillForm).click();
                Arrays.asList(inputField2).forEach(s -> {
                    $$(" .MuiAutocomplete-listbox .MuiAutocomplete-option").findBy(text(s)).click();
                });
                String strErrorMessage1 = "The count is less than " + text_numberField_minCount ;
                $("p.Mui-error").should(exist).shouldHave(text(strErrorMessage1));

                $(inputInFillForm).should(exist).click();

                List<SelenideElement> valuesAvailableInInput1 = $$(blockStr + ".MuiAutocomplete-listbox .MuiAutocomplete-option"); //Fetch the options available
                for (int i = 1; i <= valuesAvailableInInput1.size(); i++) {

                    $(blockStr).find(" input:nth-child(" + i + ") .MuiAutocomplete-listbox .MuiAutocomplete-option").should(exist).click();
                }
            }

            //Max count
            if (StringUtils.isNotEmpty(text_numberField_maxCount)) {
                System.out.println("Verifying Maximum Count: " + text_numberField_maxCount);
                int x = (Arrays.asList(valuesInFillForm)).indexOf(edit_values);
                String inputField1 = blockStr + " input:nth-child(" + (x + 2) + ")";
                $(inputInFillForm).click();
                Arrays.asList(inputField1).forEach(s -> {
                    $$(".MuiAutocomplete-popper li").findBy(cssClass("MuiAutocomplete-option")).click();
                });
                String strErrorMessage2 = "The count must be greater than " + text_numberField_maxCount ;
                $("p.Mui-error").should(exist).shouldHave(text(strErrorMessage2));
            }
        }
    }
}