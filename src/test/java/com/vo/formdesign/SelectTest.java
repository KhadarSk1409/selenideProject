package com.vo.formdesign;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static reusables.ReuseActions.elementLocators;
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
    public void precondition() throws IOException {
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
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $(elementLocators("TemplateList")).find(byText("Show More")).should(exist).click(); //Click on Show More
        $(elementLocators("SelectField")).should(appear).click();
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

            $(elementLocators("EditValuesPenIcon")).should(exist).click(); //Click on edit value pen icon
            $(elementLocators("ValuesEditor")).should(exist); //Value List Editor window

            //Deleting the existing rows:
            List<SelenideElement> delBtn =$$(elementLocators("DeleteButtonsAvailable"));
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
                $(labelSelector).should(exist).doubleClick();
                String labelValue = values[i - 1];
                $(elementLocators("LabelInputInEditor")).sendKeys(Keys.BACK_SPACE); //Clear the default value in label field
                $(elementLocators("LabelInputInEditor")).setValue(labelValue).sendKeys(Keys.ENTER);
                $(labelSelector).shouldHave(text(labelValue));

                if (preselected.contains(labelValue)) {
                    String checkboxSelector = "#myGrid .MuiDataGrid-row:nth-child("+i+") div:nth-child(1) span input";
                    $(checkboxSelector).should(exist).click();
                    $(checkboxSelector).shouldBe(checked);
                }
            }

            //Click on close button
            $(elementLocators("CloseValuesEditorBtn")).should(exist).click();

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
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown
            String checkBoxId = "#" + SelectTest.SelectIds.checkbox_multiple.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            //Now the Minimum and Maximum count field should be enabled:
            $(elementLocators("MinCountInputField")).should(exist).shouldBe(enabled);
            $(elementLocators("MaxCountInputField")).should(exist).shouldBe(enabled);
        }

        //Enter Minimum Value
        if (StringUtils.isNotEmpty(text_numberField_minCount)) {
            $(elementLocators("EditValuesPenIcon")).should(exist).click(); //Click on edit value pen icon
            $(elementLocators("ValuesEditor")).should(exist); //Value List Editor window

            List<SelenideElement> rowsInListEditor = $$(elementLocators("RowsInValuesEditor")); //fetch number of rows in List editor
            int rowsCount = rowsInListEditor.size();

            //Click on close button
            $(elementLocators("CloseValuesEditorBtn")).should(exist).click();



            $(By.id(SelectTest.SelectIds.numberField_minCount.name())).should(exist); //Verify that Minimum count field exists

            String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(SelectTest.SelectIds.numberField_minCount.name()))
                    .setValue(text_numberField_minCount).sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            $(elementLocators("MinCountInputField")).shouldHave(value(text_numberField_minCount)).should(appear, Duration.ofSeconds(5));

            int int_text_numberField_minCount = parseInt(text_numberField_minCount);

            //Verify that if the Min count is less than rowCount, then error should be shown
            if (int_text_numberField_minCount < rowsCount) {
                String errorMinCount1 = "The values count " + rowsCount + " is less than minimum count " + text_numberField_minCount;
                $(elementLocators("MinCountErrorHelperText")).should(exist).shouldHave(text(errorMinCount1));
            }
        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(text_numberField_maxCount)) {
            if (!($(By.id(SelectTest.SelectIds.numberField_maxCount.name())).isEnabled())) {
                $(elementLocators("AllowMultipleCheckBox")).should(exist).click();
            }

            String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(SelectTest.SelectIds.numberField_maxCount.name()))
                    .setValue(text_numberField_maxCount).sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            $(elementLocators("MaxCountInputField")).shouldHave(value(text_numberField_maxCount)).should(appear, Duration.ofSeconds(4));

            //Verify that if Max count is less than Min count, relevant errors should be shown:
            if (StringUtils.isNotEmpty(text_numberField_minCount)) {
                selectAndClear(By.id(SelectIds.numberField_minCount.name()))
                        .setValue(text_numberField_minCount).sendKeys(Keys.TAB); //Enter the value in Min count field

                int int_text_numberField_minCount = parseInt(text_numberField_minCount);
                int int_text_numberField_maxCount = parseInt(text_numberField_maxCount);
                if (int_text_numberField_minCount > int_text_numberField_maxCount) {
                    String errorMaxCount1 = "The maximum value " + text_numberField_maxCount + " is less than minimum value " + text_numberField_minCount;
                    $(elementLocators("MinCountErrorHelperText")).should(exist).shouldHave(text(errorMaxCount1));

                    String errorMaxCount2 = "The maximum value " + text_numberField_maxCount + " is less than minimum value " + text_numberField_minCount;
                    $(elementLocators("MinCountErrorHelperText")).should(exist).shouldHave(text(errorMaxCount2));

                    //After verifying the error message, the max count value will be changed so that it is valid and errors will not appear
                    selectAndClear(By.id(SelectTest.SelectIds.numberField_maxCount.name()))
                            .setValue(String.valueOf(int_text_numberField_minCount)).sendKeys(Keys.TAB);
                    $(elementLocators("MaxCountInputField")).shouldHave(Condition.value(String.valueOf(int_text_numberField_minCount)));
                }
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Publish and open the FormPage")
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
        String popupOpener = blockStr + " .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator";
        String cleanUpSelectedValuesInFillform = blockStr + " button.MuiAutocomplete-clearIndicator";

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
            //Open select popover,
            $(popupOpener).should(exist).click();
            //Popover dialog should appear
            $(elementLocators("Popover")).should(appear);
            //verify all options are listened in dropdown
            Arrays.asList(strEditValues).forEach(s -> {
                $$(elementLocators("ListOfOptions")).shouldHave(itemWithText(s));
            });
            $(valuesInFillForm).should(exist);
        }

        //Preselection values
        if (StringUtils.isNotEmpty(preselection_value) && StringUtils.isEmpty(checkbox_allow_multiple)) {
            System.out.println("Verifying Preselection value: " + preselection_value);
            $(blockStr + " input").shouldHave(value(preselection_value));
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verifying required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        //Allow multiple checkbox
        if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {
            System.out.println("Verifying Allow multiple: " + checkbox_allow_multiple);
            $("body").click();
            $(labelInFillForm).should(exist).click();

            //Open select popover
            $(popupOpener).should(exist).click();
            //Popover dialog should appear
            $(elementLocators("Popover")).should(appear);
            List<String> options = $$(elementLocators("ListOfOptions")).texts();
            List<String> twoOptionsToSelect = options.stream().limit(2).collect(Collectors.toList());

            //select two options
            twoOptionsToSelect.forEach(s -> {
                $$(elementLocators("ListOfOptions")).findBy(text(s)).should(exist).click();

                //Click on input field inorder to select multiple options
                $(popupOpener).should(exist).click();
            });

            //verify two options are appearing as chips
            twoOptionsToSelect.forEach(s -> {
                $$(blockStr + " .MuiChip-root .MuiChip-label").findBy(text(s)).should(exist);
            });
            //Should cleanup the selected values
            $(cleanUpSelectedValuesInFillform).should(exist).click();
        }

        //Min count
        if (StringUtils.isNotEmpty(text_numberField_minCount) && (StringUtils.isNotEmpty(checkbox_allow_multiple))) {
            System.out.println("Verifying minimum count: " + text_numberField_minCount);

            //checking scenario: less than min -> error should appear
            List<String> listOfOptions = $$(elementLocators("ListOfOptions")).texts();
            $(popupOpener).should(exist).click(); //Open select popover
            List<String> OptionsToSelect = listOfOptions.stream().limit(1).collect(Collectors.toList());

            $(popupOpener).should(exist).click(); //Open select popover
            //select options
            OptionsToSelect.forEach(s -> {
                $$(elementLocators("ListOfOptions")).findBy(text(s)).click();

                //If count is less then error message should appear
                String strErrorMessage1 = "The count must be greater than " + text_numberField_minCount;
                $(elementLocators("ErrorHelperText")).should(exist).shouldHave(text(strErrorMessage1));

            });

            //Should cleanup the selected values
            $(cleanUpSelectedValuesInFillform).should(exist).click();

            //Select valid options so that error message will disappear
            List<String> allOptionsToSelect = listOfOptions.stream().limit(listOfOptions.size()).collect(Collectors.toList());
            $(popupOpener).should(exist).click(); //Open select popover
            allOptionsToSelect.forEach(s -> {
                $$(elementLocators("ListOfOptions")).findBy(text(s)).click();
                $(popupOpener).should(exist).click(); //Open select popover
            });

            //verify all options are appearing as chips
            allOptionsToSelect.forEach(s -> $$(blockStr + " .MuiChip-root .MuiChip-label").findBy(text(s)).should(exist));
            $(elementLocators("ErrorHelperText")).shouldNot(exist); //Error message should not appear
        }

            //Max count
            if (StringUtils.isNotEmpty(text_numberField_maxCount)&& (StringUtils.isNotEmpty(checkbox_allow_multiple))) {
                System.out.println("Verifying maximum count: " + text_numberField_maxCount);

                List<String> listOfOptions = $$(elementLocators("ListOfOptions")).texts();

                $(popupOpener).should(exist).click(); //Open select popover

                List<String> OptionsToSelect = listOfOptions.stream().limit(listOfOptions.size()).collect(Collectors.toList());

                $(popupOpener).should(exist).click(); //Open select popover
                //select options
                OptionsToSelect.forEach(s -> {
                    $$(elementLocators("ListOfOptions")).findBy(text(s)).click();
                    $(popupOpener).should(exist).click(); //Open select popover
                });
                //If count is more then error message should appear
                String strErrorMessage1 = "The count must be less than " + text_numberField_maxCount;
                $(elementLocators("ErrorHelperText")).should(exist).shouldHave(text(strErrorMessage1));

                //Should cleanup the selected values
                $(cleanUpSelectedValuesInFillform).should(exist).click();

                //Select valid options so that error message will disappear
                List<String> allOptionsToSelect = listOfOptions.stream().limit((listOfOptions.size())-1).collect(Collectors.toList());
                $(popupOpener).should(exist).doubleClick(); //Open select popover
                allOptionsToSelect.forEach(s -> {
                    $$(elementLocators("ListOfOptions")).findBy(text(s)).click();
                    $(popupOpener).should(exist).click(); //Open select popover
                });

                //verify all options are appearing as chips
                allOptionsToSelect.forEach(s -> $$(blockStr + " .MuiChip-root .MuiChip-label").findBy(text(s)).should(exist));
                $(elementLocators("ErrorHelperText")).shouldNot(exist); //Error message should not appear

        }
            $(elementLocators("Body")).click();
            $(elementLocators("ListOfOptions")).shouldNot(appear);

    }
}