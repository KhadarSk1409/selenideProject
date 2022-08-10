package com.vo.formdesign;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TextField Creation Tests")
public class TextAreaFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "TextField Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    public enum TextAreaFieldOptionsIds {
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
    public void precondition() throws IOException {
        navigateToFormDesign(FormField.TEXTAREA_FIELD);
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
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $(elementLocators("TextAreaField")).should(appear).click();
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $(elementLocators("FormPropertiesCard")).should(appear);

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
        if (StringUtils.isNotEmpty(textfield_label)) {
            labelVerificationOnFormDesign(blockId,textfield_label);
        }

        //disable Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, textfield_label);
        }

        //Help
        if (StringUtils.isNotEmpty(textfield_help)) {
            helpVerificationOnFormDesign(blockId, textfield_help);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        //Default Value
        if (StringUtils.isNotEmpty(textfield_defaultValue)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TextAreaFieldOptionsIds.textfield_defaultValue.name()))
                    .setValue(textfield_defaultValue).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            $(By.id(TextAreaFieldOptionsIds.textfield_defaultValue.name())).shouldHave(value(textfield_defaultValue));
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        }


        //only Alphabets
        if (StringUtils.isNotEmpty(property_onlyAlphabets)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextAreaFieldTest.TextAreaFieldOptionsIds.prop_onlyAlphabets_onlyAlphabets.name();
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            $(radioBtnId + " input").shouldBe(selected);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        }

        //Alphabets and numerics
        if (StringUtils.isNotEmpty(property_alphabetsAndNumerics)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextAreaFieldTest.TextAreaFieldOptionsIds.prop_alphabetsAndNumerics_alphabetsAndNumerics.name();
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //All chars
        if (StringUtils.isNotEmpty(property_allCharacters)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextAreaFieldTest.TextAreaFieldOptionsIds.prop_allCharacters_allCharacters.name();
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            if(! $(radioBtnId + " input").isSelected()) {
                $(radioBtnId).shouldBe(visible).click();
                $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
                //$(checkBoxId + " input").shouldHave(value("true"));
                $(radioBtnId + " input").shouldBe(selected);
            }
        }

        if (minLength != null && minLength > 0) {
            String sliderId = "#prop_minMaxLength_formcontrol";
            String minInputSel = sliderId + " .hidden_slider_inputs .minValue input";
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            executeJavaScript("document.querySelector('#prop_minMaxLength_formcontrol .hidden_slider_inputs').hidden = false;");
            selectAndClear(By.cssSelector(minInputSel)).setValue(minLength.toString()).sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased

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

            executeJavaScript("document.querySelector('#prop_minMaxLength_formcontrol .hidden_slider_inputs').hidden = true;");
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased

            String maxValue = $(sliderId + " input").getValue().split(",")[0];
            Assertions.assertEquals(maxLength.toString(), maxValue, "max value should be " + maxLength);
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
        String textareaInput = inputFieldInFillForm + " textarea:nth-of-type(1)";

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

        //AlphaNumeric
        if (StringUtils.isNotEmpty(property_alphabetsAndNumerics)) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(400));
            selectAndClear(textareaInput);
            $(textareaInput).setValue(Str).pressTab();
            $(textareaInput).shouldHave(value(Str));

            //Negative scenario:
            String Str1 = (RandomStringUtils.randomAlphanumeric(401));
            selectAndClear(textareaInput);
            $(textareaInput).shouldNotHave(text(Str));
            $(textareaInput).setValue(Str1).pressTab();
            $(textareaInput).shouldHave(value(Str1.substring(0, 400))); //the value in field should be cutted by max allowed length
            $(helpInFillForm).shouldHave(text("The length must be in the range 0 - 400")); //Verify the error shown
        }

        //All Characters
        if (StringUtils.isNotEmpty(property_allCharacters)) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(390) + "!@#$%^&*()");
            selectAndClear(textareaInput);
            $(textareaInput).setValue(Str).pressTab();
            $(textareaInput).shouldHave(value(Str));

            //Negative scenario:
            String Str1 = (RandomStringUtils.randomAlphanumeric(391) + "!@#$%^&*()");
            selectAndClear(textareaInput);
            $(textareaInput).shouldNotHave(value(Str));
            $(textareaInput).setValue(Str1).pressTab();
            $(textareaInput).shouldHave(value(Str1.substring(0, 400))); //the value in field should be cutted by max allowed length
            $(helpInFillForm).shouldHave(text("The length must be in the range 0 - 400")); //Verify the error shown
        }


        // Only Alphabets
        if (StringUtils.isNotEmpty(property_onlyAlphabets)) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphabetic(400));
            $(textareaInput).setValue(Str).pressTab();
            $(textareaInput).shouldHave(value(Str));

            //Negative scenario:
            String Str1 = (RandomStringUtils.randomAlphabetic(401));
            selectAndClear(textareaInput);
            $(textareaInput).clear();
            $(textareaInput).shouldNotHave(value(Str1));
            $(textareaInput).setValue(Str1).pressTab();
            //"The length must be in the range  0 - 400"
            $(textareaInput).shouldHave(value(Str1.substring(0, 400))); //the value in field should be cutted by max allowed length
            $(helpInFillForm).shouldHave(text("The length must be in the range 0 - 400")); //Verify the error shown

            selectAndClear(textareaInput);
            $(textareaInput).shouldNotHave(value(Str));
            $(textareaInput).shouldNotHave(value(Str1));

            String integerSeq = "1234567890";
            $(textareaInput).setValue(integerSeq);
            $(textareaInput).shouldHave(value("")); //Field should be empty - integer not accepted
        }


        //Min Length
        if (minLength != null && minLength > 0) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(minLength));
            $(textareaInput).setValue(Str).pressTab();
            $(textareaInput).shouldHave(value(Str));

            //Negative scenario:
            int lessThanMinLength = minLength - 1; //Less than min length
            String errorStr = "The length must be in the range " + minLength + " - " + maxLength;
            String Str1 = (RandomStringUtils.randomAlphanumeric(lessThanMinLength));
            selectAndClear(textareaInput);
            $(textareaInput).shouldNotHave(value(Str)); //Verify that field is cleared
            $(textareaInput).setValue(Str1).pressTab();

            $(helpInFillForm).shouldHave(text(errorStr)); //Error should be shown
        }

        if (maxLength != null && maxLength > 0) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(maxLength));
            //  String textareaInput = inputFieldInFillForm+" textarea:nth-of-type(1)";
            selectAndClear(textareaInput);
            $(textareaInput).setValue(Str).pressTab();
            $(textareaInput).shouldHave(value(Str));

            //Negative scenario:
            int moreThanMaxLength = maxLength + 1; //More than Max length
            String Str1 = (RandomStringUtils.randomAlphanumeric(moreThanMaxLength));
            selectAndClear(textareaInput);
            $(textareaInput).shouldNotHave(value(Str)); //Verify that field is cleared
            $(textareaInput).setValue(Str1).pressTab();

            $(textareaInput).shouldHave(value(Str1.substring(0, maxLength))); //the value in field should be cutted by max allowed length
        }
    }

}
