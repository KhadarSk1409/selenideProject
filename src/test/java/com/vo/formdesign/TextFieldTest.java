package com.vo.formdesign;

import com.codeborne.selenide.CollectionCondition;
import com.vo.BaseTest;
import jdk.javadoc.doclet.Reporter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.platform.engine.reporting.ReportEntry;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import javax.print.DocFlavor;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TextField Creation Tests")
public class TextFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "TextField Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    public enum TextFieldOptionsIds {
        textfield_label,
        textfield_help,
        textfield_prefix,
        textfield_suffix,
        textfield_defaultValue,
        prop_toggle_button_group_caps, prop_toggle_button_normal, prop_toggle_button_uppercase, prop_toggle_button_lowercase,
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
        navigateToFormDesign(FormField.TEXTFIELD);

    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForTextfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/text_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String textfield_label,
                             String checkbox_disableLabel,
                             String textfield_help,
                             String textfield_prefix,
                             String textfield_suffix,
                             String textfield_defaultValue,
                             String property_toggle_button_normal, String property_toggle_button_uppercase, String property_toggle_button_lowercase,
                             String checkbox_required,
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
        $("#li-template-Textfield-05").should(appear).click();
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
        if (StringUtils.isNotEmpty(textfield_label)) {
            labelVerificationOnFormDesign(blockId,textfield_label);
        }

        //disable Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, textfield_label);
        }

        //Help
        if (StringUtils.isNotEmpty(textfield_help)) {
            helpVerificationOnFormDesign(blockId, textfield_label);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        //Prefix
        if (StringUtils.isNotEmpty(textfield_prefix)) {
            //   $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TextFieldOptionsIds.textfield_prefix.name()))
                    .setValue(textfield_prefix).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(By.id(TextFieldOptionsIds.textfield_prefix.name())).shouldHave(value(textfield_prefix));
        }

        //Suffix
        if (StringUtils.isNotEmpty(textfield_suffix)) {
            //     $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TextFieldOptionsIds.textfield_suffix.name()))
                    .setValue(textfield_suffix).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(By.id(TextFieldOptionsIds.textfield_suffix.name())).shouldHave(value(textfield_suffix));
        }

        //Default Value
        if (StringUtils.isNotEmpty(textfield_defaultValue)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TextFieldOptionsIds.textfield_defaultValue.name()))
                    .setValue(textfield_defaultValue).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(By.id(TextFieldOptionsIds.textfield_defaultValue.name())).shouldHave(value(textfield_defaultValue));
        }

        //chars normal
        if (StringUtils.isNotEmpty(property_toggle_button_normal)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(By.id(TextFieldOptionsIds.prop_toggle_button_normal.name())).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(By.id(TextFieldOptionsIds.prop_toggle_button_normal.name())).shouldHave(attribute("aria-pressed", "true"));
        }

        //chars caps/uppercase
        if (StringUtils.isNotEmpty(property_toggle_button_uppercase)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(By.id(TextFieldOptionsIds.prop_toggle_button_uppercase.name())).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(By.id(TextFieldOptionsIds.prop_toggle_button_uppercase.name())).shouldHave(attribute("aria-pressed", "true"));
        }

        //chars small/lowercase
        if (StringUtils.isNotEmpty(property_toggle_button_lowercase)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(By.id(TextFieldOptionsIds.prop_toggle_button_lowercase.name())).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(By.id(TextFieldOptionsIds.prop_toggle_button_lowercase.name())).shouldHave(attribute("aria-pressed", "true"));
        }

        //only Alphabets
        if (StringUtils.isNotEmpty(property_onlyAlphabets)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextFieldOptionsIds.prop_onlyAlphabets_onlyAlphabets.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Alphabets and numerics
        if (StringUtils.isNotEmpty(property_alphabetsAndNumerics)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextFieldOptionsIds.prop_alphabetsAndNumerics_alphabetsAndNumerics.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //All chars
        if (StringUtils.isNotEmpty(property_allCharacters)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextFieldOptionsIds.prop_allCharacters_allCharacters.name();
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
    @CsvFileSource(resources = "/text_field_test_data.csv", numLinesToSkip = 1)
    public void verifyFieldsOnForm(Integer row, Integer col, Integer colSpan,
                                   String textfield_label,
                                   String checkbox_disableLabel,
                                   String textfield_help,
                                   String textfield_prefix,
                                   String textfield_suffix,
                                   String textfield_defaultValue,
                                   String property_toggle_button_normal, String property_toggle_button_uppercase, String property_toggle_button_lowercase,
                                   String checkbox_required,
                                   String property_onlyAlphabets,
                                   String property_alphabetsAndNumerics,
                                   String property_allCharacters,
                                   Integer minLength,
                                   Integer maxLength
    ) {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";
        String helpInFillForm = blockStr + " .MuiFormHelperText-root";
        String prefixSuffixInFillForm = blockStr + " .MuiInputBase-root";
        String prefixInFillForm1 = blockStr + " .MuiInputAdornment-positionStart";
        String suffixInFillForm1 = blockStr + " .MuiInputAdornment-positionEnd";
        String prefixSuffixText = blockStr + " .MuiInputAdornment-root";
        String defaultValueInFillForm = blockStr + " .MuiInputBase-input";
        String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";

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

        //Prefix
        if (StringUtils.isNotEmpty(textfield_prefix)) {
            System.out.println("Verifying prefix: " + textfield_prefix);
            $(prefixSuffixInFillForm).shouldHave(text(textfield_prefix));
            $(prefixInFillForm1).shouldHave(text(textfield_prefix));
        }

        //Suffix
        if (StringUtils.isNotEmpty(textfield_suffix)) {
            System.out.println("Verifying suffix: " + textfield_suffix);
            $(prefixSuffixInFillForm).shouldHave(text(textfield_suffix));
            $(suffixInFillForm1).shouldHave(text(textfield_suffix));
        }

        //Default value
        if (StringUtils.isNotEmpty(textfield_defaultValue)) {
            System.out.println("Verifying default value: " + textfield_defaultValue);
            $(defaultValueInFillForm).shouldHave(value(textfield_defaultValue));
        }

        //Lower case
        if (StringUtils.isNotEmpty(property_toggle_button_lowercase)) {
            String upperCaseStr = textfield_label.toUpperCase();
            $(blockStr + " input").setValue(upperCaseStr).pressTab();
            $(blockStr + " input").shouldHave(value(upperCaseStr.toLowerCase()));

            //Verify that user can enter 14 characters:
            String upperCaseStr14 = (RandomStringUtils.randomAlphanumeric(14)).toUpperCase();
            selectAndClear(blockStr + " input");
            $(blockStr + " input").setValue(upperCaseStr14).pressTab();
            $(blockStr + " input").shouldHave(value(upperCaseStr14.toLowerCase()));

            // Negative scenario:
            String bothCasesStr1 = (RandomStringUtils.randomAlphabetic(15));
            selectAndClear(blockStr + " input");
            $(blockStr + " input").setValue(bothCasesStr1).pressTab();
            $(helpInFillForm).shouldHave(text("The length must be in the range 0 - 14")); //Verify the error shown
        }

        //Upper case
        if (StringUtils.isNotEmpty(property_toggle_button_uppercase)) {
            String lowerCaseStr = textfield_label.toLowerCase();
            $(blockStr + " input").setValue(lowerCaseStr).pressTab();
            $(blockStr + " input").shouldHave(value(lowerCaseStr.toLowerCase()));

            //Verify that user can enter 14 characters:
            String lowerCaseStr14 = (RandomStringUtils.randomAlphanumeric(14)).toLowerCase();
            selectAndClear(blockStr + " input");
            $(blockStr + " input").setValue(lowerCaseStr14).pressTab();
            $(blockStr + " input").shouldHave(value(lowerCaseStr14.toUpperCase()));

        }

        //Alphabets Both cases
        if (StringUtils.isNotEmpty(property_toggle_button_normal)) {
            //Positive scenario:
            String bothCasesStr = (RandomStringUtils.randomAlphabetic(14));
            $(blockStr + " input").setValue(bothCasesStr).pressTab();
            $(blockStr + " input").shouldHave(value(bothCasesStr));

            // Negative scenario:
            String bothCasesStr1 = (RandomStringUtils.randomAlphabetic(15));
            selectAndClear(blockStr + " input");
            $(blockStr + " input").setValue(bothCasesStr1).pressTab();
            $(helpInFillForm).shouldHave(text("The length must be in the range 0 - 14")); //Verify the error shown
        }


        //Only Alphabets
        if (StringUtils.isNotEmpty(property_onlyAlphabets)) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphabetic(14));
            $(blockStr + " input").setValue(Str).pressTab();
            $(blockStr + " input").shouldHave(value(Str));

            //Negative scenario:
            String Str1 = (RandomStringUtils.randomAlphabetic(15));
            selectAndClear(blockStr + " input");
            $(blockStr + " input").clear();
            $(blockStr + " input").shouldNotHave(value(Str1));
            $(blockStr + " input").setValue(Str1).pressTab();
            $(helpInFillForm).shouldHave(text("The length must be in the range 0 - 14")); //Verify the error shown

            selectAndClear(blockStr + " input");
            $(blockStr + " input").shouldNotHave(value(Str));
            $(blockStr + " input").shouldNotHave(value(Str1));

            String integerSeq = "1234567890";
            $(blockStr + " input").setValue(integerSeq);
            $(blockStr + " input").shouldHave(value("")); //Field should be empty - integer not accepted
        }

        //AlphaNumeric
        if (StringUtils.isNotEmpty(property_alphabetsAndNumerics)) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(14));
            selectAndClear(blockStr + " input");
            $(blockStr + " input").setValue(Str).pressTab();
            $(blockStr + " input").shouldHave(value(Str));

            //Negative scenario:
            String Str1 = (RandomStringUtils.randomAlphanumeric(15));
            selectAndClear(blockStr + " input");
            $(blockStr + " input").shouldNotHave(text(Str));
            $(blockStr + " input").setValue(Str1).pressTab();
            $(helpInFillForm).shouldHave(text("The length must be in the range 0 - 14")); //Verify the error shown
        }

        //All Characters
        if (StringUtils.isNotEmpty(property_allCharacters)) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(4)+"!@#$%^&*()");
            selectAndClear(blockStr + " input");
            $(blockStr + " input").setValue(Str).pressTab();
            $(blockStr + " input").shouldHave(value(Str));

            //Negative scenario:
            String Str1 = (RandomStringUtils.randomAlphanumeric(5)+"!@#$%^&*()");
            selectAndClear(blockStr + " input");
            $(blockStr + " input").shouldNotHave(value(Str));
            $(blockStr + " input").setValue(Str1).pressTab();
            $(helpInFillForm).shouldHave(text("The length must be in the range 0 - 14")); //Verify the error shown
        }

        //Min Length
        if (minLength != null && minLength > 0) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(minLength));
            $(blockStr + " input").setValue(Str).pressTab();
            $(blockStr + " input").shouldHave(value(Str));

            //Negative scenario:
            int lessThanMinLength = minLength - 1; //Less than min length
            String errorStr = "The length must be in the range " + minLength + " - " + maxLength;
            String Str1 = (RandomStringUtils.randomAlphanumeric(lessThanMinLength));
            selectAndClear(blockStr + " input");
            $(blockStr + " input").shouldNotHave(value(Str)); //Verify that field is cleared
            $(blockStr + " input").setValue(Str1).pressTab();

            $(helpInFillForm).shouldHave(text(errorStr)); //Error should be shown
        }

        if (maxLength != null && maxLength > 0) {
            //Positive scenario:
            String Str = (RandomStringUtils.randomAlphanumeric(maxLength));
            selectAndClear(blockStr + " input");
            $(blockStr + " input").setValue(Str).pressTab();
            $(blockStr + " input").shouldHave(value(Str));

            //Negative scenario:
            int moreThanMaxLength = maxLength + 1; //More than Max length
            String errorStr = "The length must be in the range " + minLength + " - " + maxLength;
            String Str1 = (RandomStringUtils.randomAlphanumeric(moreThanMaxLength));
            selectAndClear(blockStr + " input");
            $(blockStr + " input").shouldNotHave(value(Str)); //Verify that field is cleared
            $(blockStr + " input").setValue(Str1).pressTab();

            $(blockStr + " input").shouldHave(value(Str1.substring(0, maxLength))); //the value in field should be cutted by max allowed length
        }
    }

}


