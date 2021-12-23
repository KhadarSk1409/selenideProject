package com.vo.formdesign;

import com.codeborne.selenide.CollectionCondition;
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
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("FileUploadField Tests")
public class FileUploadFieldTest extends BaseTest {

    public enum FileUploadFieldIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        numberField_maxFileSize,
        numberField_minCount,
        numberField_maxCount;
    }

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() throws IOException {
        navigateToFormDesign(FormField.FILE_UPLOAD_FIELD);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForFileUploadfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/fileupload_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String text_label,
                             String checkbox_disableLabel,
                             String text_help,
                             String checkbox_required,
                             String text_max_file_size,
                             String text_numberField_minCount,
                             String text_numberField_maxCount,
                             String edit_values,
                             String preselection_value


    ) {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $(elementLocators("FileUploadField")).should(exist).click();
        $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version has increased


        //Label
        if (StringUtils.isNotEmpty(text_label)) {
            labelVerificationOnFormDesign(blockId,text_label);
        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, text_label);
        }

        //Help
        if (StringUtils.isNotEmpty(text_help)) {
            helpVerificationOnFormDesign(blockId, text_label);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        //Maximum file size
        if(StringUtils.isNotEmpty(text_max_file_size)){
            $(By.id(FileUploadFieldTest.FileUploadFieldIds.numberField_maxFileSize.name())).should(exist);
            selectAndClear(By.id(FileUploadFieldTest.FileUploadFieldIds.numberField_maxFileSize.name()))
                    .setValue(text_max_file_size).sendKeys(Keys.TAB);
            $(By.id(FileUploadFieldTest.FileUploadFieldIds.numberField_maxFileSize.name())).shouldHave(value(text_max_file_size)); //Verify that Max file size has entered value
        }


        //Enter Minimum Value
        if (StringUtils.isNotEmpty(text_numberField_minCount)) {

            $(By.id(FileUploadFieldTest.FileUploadFieldIds.numberField_minCount.name())).should(exist); //Verify that Minimum count field exists

            String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(FileUploadFieldTest.FileUploadFieldIds.numberField_minCount.name()))
                    .setValue(text_numberField_minCount).sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            $(By.id(FileUploadFieldTest.FileUploadFieldIds.numberField_minCount.name())).shouldHave(value(text_numberField_minCount));

        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(text_numberField_maxCount)) {
            String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(FileUploadFieldIds.numberField_maxCount.name()))
                    .setValue(text_numberField_maxCount).sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased

            $(elementLocators("MaxCountInputField")).shouldHave(value(text_numberField_maxCount)).should(appear, Duration.ofSeconds(5));

            //Verify that if Max count is less than Min count, relevant errors should be shown:
            if(StringUtils.isNotEmpty(text_numberField_minCount)) {
                int int_text_numberField_minCount = parseInt(text_numberField_minCount);
                int int_text_numberField_maxCount = parseInt(text_numberField_maxCount);
                if(int_text_numberField_minCount > int_text_numberField_maxCount){
                    String errorMaxCount1 = "The maximum value "+text_numberField_maxCount+" is less than minimum value "+text_numberField_minCount;
                    $(elementLocators("MaxCountErrorHelperText")).should(exist).shouldHave(text(errorMaxCount1));

                    //Reset max value to valid value inorder to publish the form
                    selectAndClear(By.id(FileUploadFieldTest.FileUploadFieldIds.numberField_maxCount.name()))
                            .setValue(text_numberField_minCount).sendKeys(Keys.TAB);
                    $(elementLocators("MaxCountInputField")).shouldHave(value(text_numberField_minCount));

                    $(elementLocators("MaxCountErrorHelperText")).shouldNot(exist);

                }
            }

        }

        //Values
        if (StringUtils.isNotEmpty(edit_values)) {

            String[] values = edit_values.split(",");

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
                    $(elementLocators("PlusIconToAddValues")).should(exist).click();
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
        }

    }

}
