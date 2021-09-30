package com.vo.formdesign;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.commands.PressEnter;
import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.navigateToFormDashBoardFromFavoriteForms;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Checkboxgroup Tests")
public class CheckboxgroupTest extends BaseTest {

    public enum CheckboxgroupIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        property_select_direction,
        checkbox_globalSelection,
        numberField_minCount,
        numberField_maxCount,
        checkbox_other;
    }

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() {
        navigateToFormDesign(FormField.CHECKBOX_DISABLE_LABEL);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForCheckBoxGroupfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/checkboxgroup_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String text_label,
                             String text_help,
                             String edit_values,
                             String preselection_value,
                             String disableLabel,
                             String checkbox_required,
                             String checkbox_globalSelection,
                             String text_numberField_minCount,
                             String text_numberField_maxCount,
                             String checkbox_other_values,
                             String dropdown_direction


    ) {
        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#li-template-CheckboxGroupField-04").should(appear).click();
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
                $(blockId).find("fieldset label:nth-child(" + i + ")").shouldHave(text(labelValue));
                if (preselected.contains(labelValue)) {
                    $(blockId).find("fieldset label:nth-child(" + i + ") input").shouldBe(checked);
                } else {
                    $(blockId).find("fieldset label:nth-child(" + i + ") input").shouldNotBe(checked);
                }
            }
        }


        //Allow select:
        if (StringUtils.isNotEmpty(checkbox_globalSelection)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CheckboxgroupTest.CheckboxgroupIds.checkbox_globalSelection.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }


        //Enter Minimum Value
        if (StringUtils.isNotEmpty(text_numberField_minCount)) {
            $("#formelement_properties_card .editForm").should(exist).click(); //Click on edit value pen icon
            $("#form-value-list-card-dialog_content").should(exist); //Value List Editor window

            List<SelenideElement> rowsInListEditor = $$("#myGrid div.ag-center-cols-container div.ag-row"); //fetch number of rows in List editor
            int rowsCount = rowsInListEditor.size();

            //Click on close button
            $("#form-value-list-card-dialog_actions #btnClosePropertiesForm").should(exist).click();
            $(By.id(CheckboxgroupTest.CheckboxgroupIds.numberField_minCount.name())).should(exist); //Verify that Minimum count field exists

            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CheckboxgroupTest.CheckboxgroupIds.numberField_minCount.name()))
                    .setValue(text_numberField_minCount).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            $("#numberField_minCount").shouldHave(value(text_numberField_minCount)).waitUntil(appears, 4000);

            int int_text_numberField_minCount = parseInt(text_numberField_minCount);

            String errorMinCount1 = "The values count " + rowsCount + " is less than minimum count " + text_numberField_minCount;
            //$("#panel1a-content div:nth-child(5) p.Mui-error").should(exist).shouldNotHave(text(errorMinCount1));


            //Verify that if the Min count is less than rowCount, then error should be shown
            // if (int_text_numberField_minCount < rowsCount) {
            if (int_text_numberField_minCount > rowsCount) {
                // String errorMinCount1 = "The values count " + rowsCount + " is less than minimum count " + text_numberField_minCount;
                $("#panel1a-content div:nth-child(5) p.Mui-error").should(exist).shouldHave(text(errorMinCount1));

                String strRowsCount = Integer.toString(rowsCount);
                selectAndClear(By.id(CheckboxgroupTest.CheckboxgroupIds.numberField_minCount.name()))
                        .setValue(strRowsCount).sendKeys(Keys.TAB);
                $("#numberField_minCount").shouldHave(value(strRowsCount));

                //    $("#panel1a-content div:nth-child(5) p.Mui-error").should(exist).shouldNotHave(text(errorMinCount1));

            }

        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(text_numberField_maxCount)) {
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CheckboxgroupIds.numberField_maxCount.name()))
                    .setValue(text_numberField_maxCount).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            $("#numberField_maxCount").shouldHave(value(text_numberField_maxCount)).waitUntil(appears, 4000);

            //Verify that if Max count is less than Min count, relevant errors should be shown:
            if (StringUtils.isNotEmpty(text_numberField_minCount)) {
                int int_text_numberField_minCount = parseInt(text_numberField_minCount);
                int int_text_numberField_maxCount = parseInt(text_numberField_maxCount);
                if (int_text_numberField_minCount > int_text_numberField_maxCount) {
                    String errorMaxCount1 = "The maximum value " + text_numberField_maxCount + " is less than minimum value " + text_numberField_minCount;
                    $("#panel1a-content div:nth-child(5) p.Mui-error").should(exist).shouldHave(text(errorMaxCount1));

                    String errorMaxCount2 = "The maximum value " + text_numberField_maxCount + " is less than minimum value " + text_numberField_minCount;
                    $("#panel2a-content div:nth-child(5) p.Mui-error").should(exist).shouldHave(text(errorMaxCount2));

                    //Reset max value to valid value
                    int int_maxValue1 = int_text_numberField_minCount + 1;
                    String strMaxValue1 = Integer.toString(int_maxValue1);

                    selectAndClear(By.id(CheckboxgroupIds.numberField_maxCount.name()))
                            .setValue(strMaxValue1).sendKeys(Keys.TAB);
                    $("#numberField_maxCount").shouldHave(value(strMaxValue1));

                    //     $("#panel2a-content div:nth-child(5) p.Mui-error").should(exist).shouldNotHave(text(errorMaxCount2));

                }
            }

        }

        //Other values:
        if (StringUtils.isNotEmpty(checkbox_other_values)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CheckboxgroupTest.CheckboxgroupIds.checkbox_other.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Direction
        //Click on Direction. Select Vertical
        if (StringUtils.isNotEmpty(dropdown_direction)) {
            $(By.id(CheckboxgroupTest.CheckboxgroupIds.property_select_direction.name())).should(exist).click();
            $(By.id(CheckboxgroupTest.CheckboxgroupIds.property_select_direction.name())).selectOptionByValue(dropdown_direction);
            $(By.id(CheckboxgroupTest.CheckboxgroupIds.property_select_direction.name())).shouldHave(value(dropdown_direction));
        }

    }


    @Test
    @Order(3)
    @DisplayName("publish and open FormPage")
    public void publishAndOpenFormPage() {
        //Click on publish button, wait until form dashboard opens and click on fill form
        $("#btnFormDesignPublish").should(exist).click();

        $("#form-publish-dialog .MuiPaper-root").should(appear); //Publish confirmation dialog appears
        $("#form-publish-dialog #btnConfirm").should(exist).click(); //Click on Confirm button
        $("#btnCreateNewData").waitUntil(exist, 50000).click(); //Fill form button on Launch screen
        $("#dataContainer").should(appear); //Verify that the form details screen appears

    }

    @Order(4)
    @DisplayName("verify fill form for checkboxgroup fields")
    @ParameterizedTest
    @CsvFileSource(resources = "/checkboxgroup_field_test_data.csv", numLinesToSkip = 1)
    public void checkboxgroupFillFormField(Integer row, Integer col, Integer colSpan,
                                           String text_label,
                                           String text_help,
                                           String edit_values,
                                           String preselection_value,
                                           String disableLabel,
                                           String checkbox_required,
                                           String checkbox_globalSelection,
                                           String text_numberField_minCount,
                                           String text_numberField_maxCount,
                                           String checkbox_other_values,
                                           String dropdown_direction) {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";
        String helpInFillForm = blockStr + " .MuiFormHelperText-root";
        String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";
        String inputField = blockStr + " input";

        //  Label
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

        //  required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verifying required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        //edit_values
        if (StringUtils.isNotEmpty(edit_values)) {
//            String[] values1 = edit_values.split(",");
//            for (int i = 1; i <= values1.length; i++) {
//                // String strCheckbox = "#data_block-loc_en-GB-r_4-c_2 .MuiCheckbox-root:nth-child("+i+")";
//                // $(strCheckbox).shouldBe(checked);

                if (!(StringUtils.isNotEmpty(preselection_value))) {
                    String[] values1 = edit_values.split(",");
                    for (int i = 1; i <= values1.length; i++) {
                        // String strCheckbox = "#data_block-loc_en-GB-r_4-c_2 .MuiCheckbox-root:nth-child("+i+")";
                        // $(strCheckbox).shouldBe(checked);

                        $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + i + ") input").shouldNotBe(checked);

                        //Check the chekbox
                        $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + i + ") input").click();
                        $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + i + ") input").shouldBe(checked);
                    }
                }
                else {

                    //preselection_value
                    String[] values2 = preselection_value.split(",");
                    for (int j = 1; j <= values2.length; j++) {
                        $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + j + ") input").shouldBe(checked);

                        //Uncheck the checkbox:
                        $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + j + ") input").click();
                        $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + j + ") input").shouldNotBe(checked);
                    }
                }
            }



                //checkbox_globalSelection
                if (StringUtils.isNotEmpty(checkbox_globalSelection)) {

                    List<SelenideElement> checkBoxes = $$(blockStr+" .MuiCheckbox-root"); //fetch number of checkboxes


                    for (int k = 1; k <= checkBoxes.size(); k++) {
                        $(blockStr).find(" .MuiFormControlLabel-root:nth-child("+k+") .MuiCheckbox-root").shouldNotBe(checked);
                    }

                    //Verify switch for All select Deselect and Allow select/Deselect all
                    $(blockStr).find(" .MuiSwitch-input").shouldNotBe(checked);

                    $(blockStr).find(" .MuiSwitch-input").click();
                    $(blockStr).find(" .MuiSwitch-input").shouldBe(checked);

                    //Verify that all check boxes get checked
                    for (int k = 1; k <= checkBoxes.size(); k++) {
                        $(blockStr).find(" .MuiFormControlLabel-root:nth-child("+k+") .MuiCheckbox-root").shouldBe(checked);
                    }

                    $(blockStr).find(" .MuiSwitch-input").click();
                    $(blockStr).find(" .MuiSwitch-input").shouldBe(checked);

                    //Verify that all check boxes get unchecked
                    for (int k = 1; k <= checkBoxes.size(); k++) {
                        $(blockStr).find(" .MuiFormControlLabel-root:nth-child("+k+") .MuiCheckbox-root").shouldNotBe(checked);
                    }

                }

                if (StringUtils.isNotEmpty(checkbox_other_values)) {

                    //Verify that Other values checkbox is not checked:
                    $("#chwckboxgroup_other").shouldNotBe(checked);

                    //Now click the checkbox and enable the edit box
                    $("#chwckboxgroup_other").click();

                    $(blockStr).find(" .MuiInputBase-root").setValue(RandomStringUtils.randomAlphanumeric(6)).sendKeys(Keys.TAB);
                }


            }

        }





