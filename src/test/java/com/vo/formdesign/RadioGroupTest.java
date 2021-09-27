package com.vo.formdesign;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
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
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Radiogroup Tests")
public class RadioGroupTest extends BaseTest {

    public enum RadiogroupIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        property_select_direction,
        checkbox_other;
    }

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() {
        navigateToFormDesign(FormField.RADIO_GROUP);

    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForRadiogroupfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/radiogroup_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String text_label,
                             String text_help,
                             String edit_values,
                             String preselection_value,
                             String disableLabel,
                             String checkbox_required,
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
        $("#li-template-RadioGroupField-04").should(appear).click();
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
            assertEquals(colSpan, currWidth / prevWidth, "block column span should be " + colSpan);
        }

        //Label
        if (StringUtils.isNotEmpty(text_label)) {
            labelVerificationOnFormDesign(blockId,text_label);
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

            for (int i = 1; i <= values.length; i++) {
                //Click on label option
                String labelSelector = "div.ag-body-viewport .ag-center-cols-viewport .ag-row:nth-child(" + i + ") .ag-cell:nth-child(2)";
                $(labelSelector).should(exist).doubleClick();
                String labelValue = values[i - 1];
                $("div.ag-popup input.ag-input-field-input").sendKeys(Keys.BACK_SPACE); //Clear the default value in label field
                $("div.ag-popup input.ag-input-field-input").setValue(labelValue).sendKeys(Keys.ENTER); //Set label value
                $(labelSelector).shouldHave(text(labelValue));

                if (StringUtils.isNotEmpty(preselection_value))
                    if (preselection_value.contains(labelValue)) {
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

                if (StringUtils.isNotEmpty(preselection_value))
                    if (preselection_value.contains(labelValue)) {
                        $(blockId).find("fieldset label:nth-child(" + i + ") input").shouldBe(checked);
                    } else {
                        $(blockId).find("fieldset label:nth-child(" + i + ") input").shouldNotBe(checked);
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
            $(By.id(RadioGroupTest.RadiogroupIds.property_select_direction.name())).should(exist).click();
            $(By.id(RadioGroupTest.RadiogroupIds.property_select_direction.name())).selectOptionByValue(dropdown_direction);
            $(By.id(RadioGroupTest.RadiogroupIds.property_select_direction.name())).shouldHave(value(dropdown_direction));
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
    @CsvFileSource(resources = "/radiogroup_field_test_data.csv", numLinesToSkip = 1)
    public void verifyFieldsOnForm(Integer row, Integer col, Integer colSpan,
                                   String text_label,
                                   String text_help,
                                   String edit_values,
                                   String preselection_value,
                                   String disableLabel,
                                   String checkbox_required,
                                   String checkbox_other_values,
                                   String dropdown_direction) {

        {
            String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
            String labelInFillForm = blockStr + " .MuiFormLabel-root";
            String helpInFillForm = blockStr + " .MuiFormHelperText-root";
            String valuesInFillForm = blockStr + " .MuiFormGroup-row";
            String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";
            String otherValuesInFillForm = blockStr + " .MuiFormControlLabel-root:nth-of-type(4)";
            String otherOptionsInFillForm = blockStr + " .MuiFormControlLabel-root:nth-of-type(1)";
            String dropDownDirectionInFillForm = blockStr + " .MuiFormGroup-root";
            String expandedBlock = ".MuiFormGroup-row";
            String inputField = blockStr + " .MuiInputBase-input";

            //Label
            if (StringUtils.isNotEmpty(text_label)) {
                System.out.println("Verifying label: " + text_label);
                if (StringUtils.isNotEmpty(disableLabel)) {
                    $(labelInFillForm).shouldNotHave(text(text_label)); //Verify that Label should not appear on the form - hide label
                } else {
                    $(labelInFillForm).shouldHave(text(text_label)); //Verify that Label appears on the form
                }
            }

            //required
            if (StringUtils.isNotEmpty(checkbox_required)) {
                System.out.println("Verifying required: *");
                $(requiredFieldInFillForm).shouldHave(text("*"));
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
                    Arrays.asList(strEditValues).forEach(s ->$(valuesInFillForm).shouldHave(text(s)));
                }

            //Preselection values
            if (StringUtils.isNotEmpty(preselection_value)) {
                System.out.println("Verifying Preselection value: " + preselection_value);
                String[] preSelectedValues = edit_values.split(",");
                int i=(Arrays.asList(preSelectedValues).indexOf(preselection_value))+1;
                String selectedValue = blockStr + " .MuiFormGroup-row label:nth-child("+i+") input";
                Arrays.asList(preSelectedValues).forEach(s ->$(selectedValue).shouldBe(checked));
            }

            //Other values:
            String randomText = (RandomStringUtils.randomAlphanumeric(10));
            if (StringUtils.isNotEmpty(checkbox_other_values)) {
                System.out.println("Verifying other values: " + checkbox_other_values);
                $(otherValuesInFillForm).should(exist).click(); //Other values should be checked and input field should be enabled
                $(inputField).setValue(randomText).pressTab(); //Enter some random text into the input field
                $(inputField).shouldHave(value(randomText));

                //if other options are checked, input field should be disabled and user should not be able to enter the text
                $(otherOptionsInFillForm).should(exist).click(); //Click on Option 1
                $(inputField).shouldBe(disabled);
            }

            //Dropdown direction
            if (StringUtils.isNotEmpty(dropdown_direction)) {
                System.out.println("Verifying direction: " + dropdown_direction);
                if (dropdown_direction.equals("vertical"))
                    $(dropDownDirectionInFillForm).$(byClassName(expandedBlock)).shouldNot(exist);
            }
        }
    }
}


