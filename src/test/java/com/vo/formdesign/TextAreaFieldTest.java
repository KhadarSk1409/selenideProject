package com.vo.formdesign;

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
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
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
        $("#li-template-TextareaField-05").should(appear).click();
        //li-template-TextareaField-05
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(appear);
        $("#panel2a-header").should(exist).click(); //Advanced section dropdown

        //options for text field should exist:
        Arrays.asList(TextAreaFieldOptionsIds.values()).forEach(textAreaFieldId -> $(By.id(textAreaFieldId.name())).shouldBe(visible));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-TextareaField-05").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForTextfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/text_area_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan, String textfield_label,
                             String textfield_help,
                             String textfield_defaultValue,
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
        $("#li-template-TextareaField-05").should(appear).click();
        $("#formelement_properties_card").should(exist);

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
            String checkBoxId = "#" + TextAreaFieldOptionsIds.checkbox_required.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //only Alphabets
        if (StringUtils.isNotEmpty(property_onlyAlphabets_onlyAlphabets)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextAreaFieldOptionsIds.prop_onlyAlphabets_onlyAlphabets.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Alphabets and numerics
        if (StringUtils.isNotEmpty(property_alphabetsAndNumerics_alphabetsAndNumerics)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TextAreaFieldOptionsIds.prop_alphabetsAndNumerics_alphabetsAndNumerics.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //All chars
        if (StringUtils.isNotEmpty(property_allCharacters_allCharacters)) {
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

}
