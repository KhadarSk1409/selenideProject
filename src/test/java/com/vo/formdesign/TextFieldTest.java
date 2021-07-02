package com.vo.formdesign;

import com.codeborne.selenide.CollectionCondition;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import java.util.Arrays;
import java.util.stream.IntStream;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TextField Creation Tests")
public class TextFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "TextField Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    enum TextFieldOptionsIds {
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
        createNewForm();
        $("#wizard-createFormButton").should(exist).click();
        $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design

        String blockId = "#block-loc_en-GB-r_1-c_1"; //Need to change later as of now _1 is returning two results
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Initial version
        $(blockId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased
        $("#li-template-Textfield-04").should(appear).click();
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(appear);

        $("#panel2a-header").should(exist).click(); //Advanced section dropdown

        //options for text field should exist:
        Arrays.asList(TextFieldOptionsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-Textfield-04").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForTextfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/text_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan, String textfield_label,
                             String textfield_help,
                             String textfield_prefix,
                             String textfield_suffix,
                             String textfield_defaultValue,
                             String property_toggle_button_normal, String property_toggle_button_uppercase, String property_toggle_button_lowercase,
                             String checkbox_disableLabel,
                             String checkbox_required,
                             String property_onlyAlphabets_onlyAlphabets,
                             String property_alphabetsAndNumerics_alphabetsAndNumerics,
                             String property_allCharacters_allCharacters,
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
        $("#li-template-Textfield-04").should(appear).click();
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
            selectAndClear(By.id(TextFieldOptionsIds.textfield_label.name()))
                    .setValue(textfield_label).sendKeys(Keys.TAB);

            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(textfield_label));
        }

        //Help
        if (StringUtils.isNotEmpty(textfield_help)) {
            // $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TextFieldOptionsIds.textfield_help.name()))
                    .setValue(textfield_help).sendKeys(Keys.TAB);

            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(textfield_help));
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

        //disable Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + TextFieldOptionsIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String checkBoxId = "#" + TextFieldOptionsIds.checkbox_required.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //only Alphabets
        if (StringUtils.isNotEmpty(property_onlyAlphabets_onlyAlphabets)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextFieldOptionsIds.prop_onlyAlphabets_onlyAlphabets.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Alphabets and numerics
        if (StringUtils.isNotEmpty(property_alphabetsAndNumerics_alphabetsAndNumerics)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextFieldOptionsIds.prop_alphabetsAndNumerics_alphabetsAndNumerics.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //All chars
        if (StringUtils.isNotEmpty(property_allCharacters_allCharacters)) {
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

}
