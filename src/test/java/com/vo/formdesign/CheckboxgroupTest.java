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
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static reusables.ReuseActions.*;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Checkboxgroup Tests")
public class CheckboxgroupTest extends BaseTest {

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() throws IOException {
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
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $(elementLocators("CheckboxGroupField")).should(appear).click();
        $(elementLocators("FormPropertiesCard")).should(appear);
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version has increased

        if (colSpan != null && colSpan > 1) {
            int prevWidth = $(blockId).getRect().getWidth();
            IntStream.range(1, colSpan).forEach(c -> {
                String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText();
                $(elementLocators("ExpandBlockBtn")).shouldBe(visible).click();
                $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1));
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
            String currentVersion = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch current version

            $(elementLocators("EditValuesPenIcon")).should(exist).click(); //Click on edit value pen icon
            $(elementLocators("ValuesEditor")).should(exist); //Value List Editor window

            //Deleting the existing rows:
            List<SelenideElement> delBtn = $$(elementLocators("DeleteButtonsAvailable"));
            int countDelBtn = $$(elementLocators("DeleteButtonsAvailable")).size();
            for (int n = countDelBtn; n >= 1; n--) {
                String strDeleteBtn = "#myGrid .MuiDataGrid-row:nth-child(" + n + ") .fa-trash"; //Delete the n th row
                $(strDeleteBtn).click();
                $(strDeleteBtn).should(disappear, Duration.ofSeconds(10));
            }

            //Add rows in value list editor for the number of labels
            if (!$(elementLocators("RowsInValuesEditor")).exists()) {
                for (int x = 0; x < values.length; x++) {
                    $(elementLocators("PlusIconToAddRows")).should(exist).click();
                }
            }

            List<String> preselected = new ArrayList<>();
            if (StringUtils.isNotEmpty(preselection_value)) {
                preselected = Arrays.asList(preselection_value.split(","));
            }

            for (int i = 1; i <= values.length; i++) {
                //Click on label option
                String labelSelector = "#myGrid .MuiDataGrid-row:nth-child("+i+") div:nth-child(3)";
                currentVersion = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
                $(labelSelector).should(exist).doubleClick();
                String labelValue = values[i - 1];
                $(elementLocators("LabelInputInEditor")).sendKeys(Keys.CONTROL, Keys.COMMAND, "a", Keys.CLEAR); //Clear the default value in label field
                $(elementLocators("LabelInputInEditor")).setValue(labelValue).sendKeys(Keys.ENTER);
                $(elementLocators("InitialVersion")).shouldNotHave(text(currentVersion));
                $(labelSelector).shouldHave(text(labelValue));

                if (preselected.contains(labelValue)) {
                    String checkboxSelector = "#myGrid .MuiDataGrid-row:nth-child("+i+") div:nth-child(1) span input";
                    $(checkboxSelector).should(exist).click();
                    $(checkboxSelector).shouldBe(checked);
                }
            }

            //Click on close button
            $(elementLocators("CloseValuesEditorBtn")).should(exist).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(currentVersion));

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
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown
            String checkBoxId = "#" + CheckboxgroupTest.CheckboxgroupIds.checkbox_globalSelection.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }


        //Enter Minimum Value
        if (StringUtils.isNotEmpty(text_numberField_minCount)) {
            $(elementLocators("EditValuesPenIcon")).should(exist).click(); //Click on edit value pen icon
            $(elementLocators("ValuesEditor")).should(exist); //Value List Editor window

            List<SelenideElement> rowsInListEditor = $$(elementLocators("RowsInValuesEditor")); //fetch number of rows in List editor
            int rowsCount = rowsInListEditor.size();

            //Click on close button
            $(elementLocators("CloseValuesEditorBtn")).should(exist).click();
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown
            $(By.id(CheckboxgroupTest.CheckboxgroupIds.numberField_minCount.name())).should(exist); //Verify that Minimum count field exists

            //Error verification if the Min count is more than rowCount, then error should be shown
            int minCountMoreThanRowCount = rowsCount +1;
            String strMinCountMoreThanRowCount = Integer.toString(minCountMoreThanRowCount);
            String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CheckboxgroupTest.CheckboxgroupIds.numberField_minCount.name()))
                    .setValue(strMinCountMoreThanRowCount).sendKeys(Keys.TAB);

            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            $(elementLocators("MinCountInputField")).shouldHave(value(strMinCountMoreThanRowCount)).should(appear, Duration.ofSeconds(5));

            String errorMinCount1 = "The values count " + rowsCount + " is less than minimum count " + strMinCountMoreThanRowCount;
                $(elementLocators("MinCountErrorHelperText")).should(exist).shouldHave(text(errorMinCount1));

                //Reset the minCount to valid value
                selectAndClear(By.id(CheckboxgroupTest.CheckboxgroupIds.numberField_minCount.name()))
                        .setValue(text_numberField_minCount).sendKeys(Keys.TAB);
            $(elementLocators("MinCountInputField")).shouldHave(value(text_numberField_minCount));

            int int_text_numberField_minCount = Integer.parseInt(text_numberField_minCount); //Valid min value

                //Set valid value for max field
                int validMaxValue = int_text_numberField_minCount+2;
                String strMaxValue = Integer.toString(validMaxValue);
                selectAndClear(By.id(CheckboxgroupIds.numberField_maxCount.name()))
                        .setValue(strMaxValue).sendKeys(Keys.TAB);
                $(elementLocators("MaxCountInputField")).shouldHave(value(strMaxValue));

        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(text_numberField_maxCount)) {
            String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CheckboxgroupIds.numberField_maxCount.name()))
                    .setValue(text_numberField_maxCount).sendKeys(Keys.TAB);

            $(elementLocators("MaxCountInputField")).shouldHave(value(text_numberField_maxCount)).should(appear, Duration.ofSeconds(5));
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            //Verify that if Max count is less than Min count, relevant errors should be shown:
            if (StringUtils.isNotEmpty(text_numberField_minCount)) {
                int int_text_numberField_maxCount = parseInt(text_numberField_maxCount);
                int invalidMinCount = int_text_numberField_maxCount + 1;
                String strInvalidMinCount = Integer.toString(invalidMinCount);

                //Insert rows in options box to have maximum size
                $(elementLocators("EditValuesPenIcon")).should(exist).click(); //Click on edit value pen icon
                $(elementLocators("ValuesEditor")).should(exist); //Value List Editor window


                List<SelenideElement> rowsInListEditor = $$(elementLocators("RowsInValuesEditor")); //fetch number of rows in List editor
                int rowsCount = rowsInListEditor.size();

                int intRowDiff = int_text_numberField_maxCount - rowsCount;


                //Create rows more than max count
                for (int i = 0; i < intRowDiff + 1; i++) {
                    $(elementLocators("PlusIconToAddRows")).should(exist).click(); //Create
                }


                //Click on close button
                $(elementLocators("CloseValuesEditorBtn")).should(exist).click();
                $(By.id(CheckboxgroupIds.numberField_maxCount.name())).should(exist); //Verify that Minimum count field exists


                //Error scenario: minCount is greater than maxcount:
                selectAndClear(By.id(CheckboxgroupIds.numberField_minCount.name()))
                        .setValue(strInvalidMinCount).sendKeys(Keys.TAB);
                String errorMaxCount1 = "The maximum value " + text_numberField_maxCount + " is less than minimum value " + strInvalidMinCount;
                $(elementLocators("MinCountErrorHelperText")).should(exist).shouldHave(text(errorMaxCount1));
                $(elementLocators("MaxCountErrorHelperText")).should(exist).shouldHave(text(errorMaxCount1));

                //Reset min value to valid value
                selectAndClear(By.id(CheckboxgroupIds.numberField_minCount.name()))
                        .setValue(text_numberField_minCount).sendKeys(Keys.TAB);
                $(elementLocators("MinCountInputField")).shouldHave(value(text_numberField_minCount));

            }
        }

        //Other values:
        if (StringUtils.isNotEmpty(checkbox_other_values)) {
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CheckboxgroupTest.CheckboxgroupIds.checkbox_other.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Direction
        //Click on Direction. Select Vertical
        if (StringUtils.isNotEmpty(dropdown_direction)) {
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown
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
        $(elementLocators("PublishButton")).should(exist).click();
        $(elementLocators("PublishConfirmationDialog")).should(appear); //Publish confirmation dialog appears
        $(elementLocators("ConfirmPublish")).should(exist).click(); //Click on Confirm button
        $(elementLocators("ConfirmationMessage")).should(appear).shouldHave(Condition.text("The form was published successfully"), Duration.ofSeconds(5));
        $(elementLocators("FillFormButton")).should(exist).click(); //Fill form button on Launch screen
        $(elementLocators("DataContainer")).should(appear); //Verify that the form details screen appears

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
        String dropDownDirectionInFillForm = blockStr + " fieldset";

        //  Label
        if (StringUtils.isNotEmpty(text_label)) {
            System.out.println("Verifying label: " + text_label);
            if (StringUtils.isNotEmpty(disableLabel)) {
                $(labelInFillForm).shouldNotHave(text(text_label)); //Verify that Label should not appear on the form - hide label
            } else {
                $(labelInFillForm).shouldHave(text(text_label)); //Verify that Label appears on the form
            }
        }

     //   Help
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
            String[] values1 = edit_values.split(",");
            for (int i = 1; i <= values1.length; i++) {

                if (!(StringUtils.isNotEmpty(preselection_value))) {

                    $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + i + ") .PrivateSwitchBase-input").shouldNotBe(checked);

                    //Check the checkbox
                    $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + i + ") .PrivateSwitchBase-input").click();
                    $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + i + ") .PrivateSwitchBase-input").shouldBe(checked);
                } else {

                    //preselection_value
                    $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + i + ") .PrivateSwitchBase-input").shouldBe(checked);

                    //Uncheck the checkbox:
                    $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + i + ") .PrivateSwitchBase-input").click();
                    $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + i + ") .PrivateSwitchBase-input").shouldNotBe(checked);
                }
            }
        }


        //checkbox_globalSelection
        if (StringUtils.isNotEmpty(checkbox_globalSelection)) {

            List<SelenideElement> checkBoxes = $$(blockStr + " .MuiCheckbox-root"); //fetch number of checkboxes

            for (int k = 1; k <= checkBoxes.size(); k++) {
                $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + k + ") .PrivateSwitchBase-input").shouldNotBe(checked);
            }

            //Verify switch for All select Deselect and Allow select/Deselect all
            $(blockStr).find(" .PrivateSwitchBase-input").shouldNotBe(checked);

            $(blockStr).find(" .PrivateSwitchBase-input").click();
            $(blockStr).find(" .PrivateSwitchBase-input").shouldBe(checked);

            //Verify that all check boxes get checked
            for (int k = 1; k <= checkBoxes.size(); k++) {
                $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + k + ") .PrivateSwitchBase-input").shouldBe(checked);
            }

            $(blockStr).find(" .PrivateSwitchBase-input").click();
            $(blockStr).find(" input[type='checkbox']").shouldNotBe(checked);

            //Verify that all check boxes get unchecked
            for (int k = 1; k <= checkBoxes.size(); k++) {
                $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + k + ") .PrivateSwitchBase-input").shouldNotBe(checked);
            }

        }

        if (StringUtils.isNotEmpty(checkbox_other_values)) {

            //Verify that Other values checkbox is not checked:
            $(elementLocators("OtherValues")).shouldNotBe(checked);

            //Now click the checkbox and enable the edit box
            $(elementLocators("OtherValues")).click();

            $(blockStr).find(" .MuiInputBase-root input").setValue(RandomStringUtils.randomAlphanumeric(6)).sendKeys(Keys.TAB);
        }

        //Dropdown direction
        if (StringUtils.isNotEmpty(dropdown_direction)) {
            System.out.println("Verifying direction: " + dropdown_direction);
            if (dropdown_direction.equals("horizontal")) {
                $(dropDownDirectionInFillForm).shouldHave(cssClass("vo-direction-horizontal"));
            } else {
                $(dropDownDirectionInFillForm).shouldNotHave(cssClass("vo-direction-vertical"));
            }
        }

        //Min Count validations
        if (StringUtils.isNotEmpty(text_numberField_minCount)) {
            System.out.println("Verifying direction: " + text_numberField_minCount);

            List<SelenideElement> checkBoxes = $$(blockStr + " .MuiCheckbox-root"); //fetch number of checkboxes
            int intMinCount = Integer.parseInt(text_numberField_minCount);
            //  int intMaxCount = Integer.parseInt(text_numberField_maxCount);
//                if (intMinCount > intMaxCount) {
            for (int k = 1; k < intMinCount; k++) {
                $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + k + ") .MuiCheckbox-root").shouldNotBe(checked);
                $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + k + ") .MuiCheckbox-root").click();
                $(blockStr).find("fieldset p span").shouldHave(text("The count must be greater than " + intMinCount));
            }
            $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + intMinCount + ") .MuiCheckbox-root").shouldNotBe(checked);
            $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + intMinCount + ") .MuiCheckbox-root").click();
            $(blockStr).find("fieldset p span").shouldNotHave(text("The count must be greater than " + intMinCount));


            for (int k = 1; k <= intMinCount; k++) {
                $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + k + ") .MuiCheckbox-root").click();
            }
        }

        //Max count validations
        if (StringUtils.isNotEmpty(text_numberField_maxCount)) {
            System.out.println("Verifying Max count: " + text_numberField_maxCount);

            List<SelenideElement> checkBoxes = $$(blockStr + " .MuiCheckbox-root"); //fetch number of checkboxes


                for (int k = 1; k <= checkBoxes.size(); k++) {
                    $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + k + ") .MuiCheckbox-root").shouldNotBe(checked);
                    $(blockStr).find(" .MuiFormControlLabel-root:nth-child(" + k + ") .MuiCheckbox-root").click();

                }
            $(blockStr).find("fieldset p span").shouldHave(text("The count must be less than " + text_numberField_maxCount));
        }
    }

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
}





