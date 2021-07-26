package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TextField Creation Tests")
public class TextAreaFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "TextField Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    enum TextAreaFieldOptionsIds {
        textfield_label,
        textfield_help,
        textfield_defaultValue,
        checkbox_disableLabel,
        checkbox_required,
        prop_minMaxLength_value,
        prop_onlyAlphabets_onlyAlphabets,
        prop_alphabetsAndNumerics_alphabetsAndNumerics,
        prop_allCharacters_allCharacters;
    }


    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() {
        createNewForm();
        $("#wizard-createFormButton").should(exist).click();
        $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design

        String blockId = "#block-loc_en-GB-r_1-c_1"; //Need to change later as of now _1 is returning two results
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Initial version
        $(blockId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased
        $("#li-template-TextareaField-05").should(exist).click();
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(exist);
        $("#panel2a-header").should(exist).click(); //Advanced section dropdown

        //options for text field area should exist:
        Arrays.asList(TextAreaFieldOptionsIds.values()).forEach(textAreaFieldId -> $(By.id(textAreaFieldId.name())).shouldBe(visible));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-TextareaField-05").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForTextfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/text_area_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String textfield_label,
                             String checkbox_disableLabel,
                             String textfield_help,
                             String checkbox_required,
                             String textfield_defaultValue,
                             String property_onlyAlphabets,
                             String property_alphabetsAndNumerics,
                             String property_allCharacters,
                             Integer minLength,
                             Integer maxLength
    ) {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $("#li-template-TextareaField-05").should(appear).click();
        $("#formelement_properties_card").should(appear);

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
        if (StringUtils.isNotEmpty(textfield_label)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TextAreaFieldOptionsIds.textfield_label.name()))
                    .setValue(textfield_label).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(textfield_label));
        }

        //disable Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + TextFieldTest.TextFieldOptionsIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldNotHave(text(textfield_label)); //Verify that the label is hidden for that block
        }

        //Help
        if (StringUtils.isNotEmpty(textfield_help)) {
            // $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TextAreaFieldOptionsIds.textfield_help.name()))
                    .setValue(textfield_help).sendKeys(Keys.TAB);

            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(textfield_help));
        }

        //Default Value
        if (StringUtils.isNotEmpty(textfield_defaultValue)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TextAreaFieldOptionsIds.textfield_defaultValue.name()))
                    .setValue(textfield_defaultValue).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(By.id(TextAreaFieldOptionsIds.textfield_defaultValue.name())).shouldHave(value(textfield_defaultValue));
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String checkBoxId = "#" + TextFieldTest.TextFieldOptionsIds.checkbox_required.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text("*"));
            $(checkBoxId + " input").shouldBe(selected);
        }

        //only Alphabets
        if (StringUtils.isNotEmpty(property_onlyAlphabets)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextAreaFieldOptionsIds.prop_onlyAlphabets_onlyAlphabets.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Alphabets and numerics
        if (StringUtils.isNotEmpty(property_alphabetsAndNumerics)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextAreaFieldOptionsIds.prop_alphabetsAndNumerics_alphabetsAndNumerics.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //All chars
        if (StringUtils.isNotEmpty(property_allCharacters)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextAreaFieldOptionsIds.prop_allCharacters_allCharacters.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            //$(checkBoxId + " input").shouldHave(value("true"));
            $(radioBtnId + " input").shouldBe(selected);
        }

        if (minLength != null && minLength > 0) {
            String sliderId = "#prop_minMaxLength_formcontrol";
            String minInputSel = sliderId + " .hidden_slider_inputs .minValue input";
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            executeJavaScript("document.querySelector('#prop_minMaxLength_formcontrol .hidden_slider_inputs').hidden = false;");
            selectAndClear(By.cssSelector(minInputSel)).setValue(minLength.toString()).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased

            executeJavaScript("document.querySelector('#prop_minMaxLength_formcontrol .hidden_slider_inputs').hidden = true;");

            String minValue = $(sliderId + " input").getValue().split(",")[0];
            Assertions.assertEquals(minLength.toString(), minValue, "min value should be " + minLength);
        }

        if (maxLength != null && maxLength > 0) {
            String sliderId = "#prop_minMaxLength_formcontrol";
            String minInputSel = sliderId + " .hidden_slider_inputs .maxValue input";
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            executeJavaScript("document.querySelector('#prop_minMaxLength_formcontrol .hidden_slider_inputs').hidden = false;");

            selectAndClear(By.cssSelector(minInputSel)).setValue(maxLength.toString()).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased

            executeJavaScript("document.querySelector('#prop_minMaxLength_formcontrol .hidden_slider_inputs').hidden = true;");

            String maxValue = $(sliderId + " input").getValue().split(",")[1];
            Assertions.assertEquals(maxLength.toString(), maxValue, "max value should be " + maxLength);
        }

    }

    @Test
    @Order(3)
    @DisplayName("publish and open FormPage")
    public void publishAndOpenFormPage() {
        //Click on publish button, wait until form dashboard opens and click on fill form
        $("#btnFormDesignPublish").should(exist).click();

        $("#form-publish-dialog .MuiPaper-root").should(appear); //Publish confirmation dialog appears
        $("#form-publish-dialog  #btnConfirm").should(exist).click(); //Click on Confirm button
        $("#btnCreateNewData").should(exist).click(); //Fill form button on Launch screen
        $("#dataContainer").should(appear); //Verify that the form details screen appears

    }

    @Order(4)
    @DisplayName("verify fields on form")
    @ParameterizedTest
    @CsvFileSource(resources = "/text_area_field_test_data.csv", numLinesToSkip = 1)
    public void verifyFieldsOnForm(Integer row, Integer col, Integer colSpan,
                                   String textfield_label,
                                   String checkbox_disableLabel,
                                   String textfield_help,
                                   String checkbox_required,
                                   String textfield_defaultValue,
                                   String property_onlyAlphabets,
                                   String property_alphabetsAndNumerics,
                                   String property_allCharacters,
                                   Integer minLength,
                                   Integer maxLength
    ) {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";
        String helpInFillForm = blockStr + " .MuiFormHelperText-root";
        String defaultValueInFillForm = blockStr + " .MuiInputBase-input";
        String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";
        String inputFieldInFillForm = blockStr + " .MuiOutlinedInput-root";

        //Label
        if (StringUtils.isNotEmpty(textfield_label)) {
            System.out.println("Verifying label: " + textfield_label);
            if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
                $(labelInFillForm).shouldNotHave(text(textfield_label)); //Verify that Label should not appear on the form - hide label
            } else {
                $(labelInFillForm).shouldHave(text(textfield_label)); //Verify that Label appears on the form
            }
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verifying required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        //Help
        if (StringUtils.isNotEmpty(textfield_help)) {
            System.out.println("Verifying help: " + textfield_help);
            $(helpInFillForm).shouldHave(text(textfield_help));
        }


        //Default value
        if (StringUtils.isNotEmpty(textfield_defaultValue)) {
            System.out.println("Verifying default value: " + textfield_defaultValue);
            $(defaultValueInFillForm).shouldHave(value(textfield_defaultValue));
        }


       // Only Alphabets
        if (StringUtils.isNotEmpty(property_onlyAlphabets)) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphabetic(400));
            String onlyAlphabetsInput = inputFieldInFillForm+" textarea:nth-of-type(1)";
            $(onlyAlphabetsInput).setValue(Str).pressTab();
            $(onlyAlphabetsInput).shouldHave(value(Str));

            //Negative scenario:
            String Str1 = (RandomStringUtils.randomAlphabetic(401));
            selectAndClear(onlyAlphabetsInput);
            $(onlyAlphabetsInput).clear();
            $(onlyAlphabetsInput).shouldNotHave(value(Str1));
            $(onlyAlphabetsInput).setValue(Str1).pressTab();
            //"The length must be in the range  0 - 400"
            $(onlyAlphabetsInput).shouldHave(value(Str1.substring(0, 400))); //the value in field should be cutted by max allowed length
            $(helpInFillForm).shouldHave(text("The length must be in the range 0 - 400")); //Verify the error shown

            selectAndClear(onlyAlphabetsInput);
            $(onlyAlphabetsInput).shouldNotHave(value(Str));
            $(onlyAlphabetsInput).shouldNotHave(value(Str1));

            String integerSeq = "1234567890";
            $(onlyAlphabetsInput).setValue(integerSeq);
            $(onlyAlphabetsInput).shouldHave(value("")); //Field should be empty - integer not accepted
        }


        //Min Length
        if (minLength != null && minLength > 0) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(minLength));
            String onlyAlphabetsInput = inputFieldInFillForm+" textarea:nth-of-type(1)";
            $(onlyAlphabetsInput).setValue(Str).pressTab();
            $(onlyAlphabetsInput).shouldHave(value(Str));

            //Negative scenario:
            int lessThanMinLength = minLength - 1; //Less than min length
            String errorStr = "The length must be in the range " + minLength + " - " + maxLength;
            String Str1 = (RandomStringUtils.randomAlphanumeric(lessThanMinLength));
            selectAndClear(onlyAlphabetsInput);
            $(onlyAlphabetsInput).shouldNotHave(value(Str)); //Verify that field is cleared
            $(onlyAlphabetsInput).setValue(Str1).pressTab();

            $(helpInFillForm).shouldHave(text(errorStr)); //Error should be shown
        }

        if (maxLength != null && maxLength > 0) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(maxLength));
            String onlyAlphabetsInput = inputFieldInFillForm+" textarea:nth-of-type(1)";
            selectAndClear(onlyAlphabetsInput);
            $(onlyAlphabetsInput).setValue(Str).pressTab();
            $(onlyAlphabetsInput).shouldHave(value(Str));

            //Negative scenario:
            int moreThanMaxLength = maxLength + 1; //More than Max length
            String Str1 = (RandomStringUtils.randomAlphanumeric(moreThanMaxLength));
            selectAndClear(onlyAlphabetsInput);
            $(onlyAlphabetsInput).shouldNotHave(value(Str)); //Verify that field is cleared
            $(onlyAlphabetsInput).setValue(Str1).pressTab();

            $(onlyAlphabetsInput).shouldHave(value(Str1.substring(0, maxLength))); //the value in field should be cutted by max allowed length
        }
    }

}
