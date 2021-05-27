package com.vo.formdesign;

import com.vo.BaseTest;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import com.codeborne.selenide.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selectors.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TextField Creation Tests")
public class TextField  extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "TextField Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    enum TextFieldOptionsIds {
        textfield_label,
        textfield_help,
        textfield_prefix,
        textfield_suffix,
        textfield_defaultValue,
        property_toggle_button_group_caps, property_toggle_button_normal, property_toggle_button_uppercase, property_toggle_button_lowercase,
        checkbox_disableLabel,
        checkbox_required,
        property_minMaxLength_value,
        property_onlyAlphabets_onlyAlphabets,
        property_alphabetsAndNumerics_alphabetsAndNumerics,
        property_allCharacters_allCharacters;
    }


    @Test
    @Order(0)
    @Disabled
    @DisplayName("precondition")
    public void precondition () {
        $(byTitle("Neues Formular")).shouldBe(visible).click();
        String formNewDesignerUrl = "/designer";
        if(!WebDriverRunner.url().contains(formNewDesignerUrl))
            open(formNewDesignerUrl);

        String blockId = "#block-loc_de-DE-r_1-c_1";
        $(blockId).shouldBe(visible).click();
        $("#li-template-Textfield-01").should(appear).click();
        $("#formelement_properties_card").should(appear);

        //options for text field should exist:
        Arrays.asList(TextFieldOptionsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#btnClosePropertiesForm").shouldBe(visible).click();
        $("#li-template-Textfield-01").should(disappear);
    }

    @Order(3)
    @Disabled
    @DisplayName("createNewFormularDesignForTextfields")
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

        String blockId = "#block-loc_de-DE-r_"+row+"-c_"+col;

        //create new block, if not exist
        if(!$(blockId).exists()) {
            String prevBlockId = "#block-loc_de-DE-r_" + (row-1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
            //now check and click on new block
        }
        $(blockId).shouldBe(visible).click();
        $("#li-template-Textfield-01").should(appear).click();
        $("#formelement_properties_card").should(appear);

        if(colSpan != null && colSpan > 1) {
            int prevWidth = $(blockId).getRect().getWidth();
            IntStream.range(1, colSpan).forEach(c -> $("#blockButtonExpand").shouldBe(visible).click());
            int currWidth = $(blockId).getRect().getWidth();
            Assertions.assertEquals(colSpan, currWidth/prevWidth, "block column span should be " + colSpan);
        }

        //Label
        if(StringUtils.isNotEmpty(textfield_label)) {
            selectAndClear(By.id(TextFieldOptionsIds.textfield_label.name()))
                    .setValue(textfield_label).sendKeys(Keys.TAB);
            $(blockId + " .formdesign-label-input input").shouldHave(value(textfield_label));
        }

        //Help
        if(StringUtils.isNotEmpty(textfield_help)) {
            selectAndClear(By.id(TextFieldOptionsIds.textfield_help.name()))
                    .setValue(textfield_help).sendKeys(Keys.TAB);
            $(blockId + " .formdesign-help-input input").shouldHave(value(textfield_help));
        }

        //Prefix
        if(StringUtils.isNotEmpty(textfield_prefix)) {
            selectAndClear(By.id(TextFieldOptionsIds.textfield_prefix.name()))
                    .setValue(textfield_prefix).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            //$(blockId + " .formdesign-prefix-input input").shouldHave(value(textfield_prefix));
        }

        //Suffix
        if(StringUtils.isNotEmpty(textfield_suffix)) {
            selectAndClear(By.id(TextFieldOptionsIds.textfield_suffix.name()))
                    .setValue(textfield_suffix).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            //$(blockId + " .formdesign-suffix-input input").shouldHave(value(textfield_suffix));
        }

        //Default Value
        if(StringUtils.isNotEmpty(textfield_defaultValue)) {
            selectAndClear(By.id(TextFieldOptionsIds.textfield_defaultValue.name()))
                    .setValue(textfield_defaultValue).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            //$(blockId + " .formdesign-defaultvalue-input input").shouldHave(value(textfield_defaultValue));
        }

        //chars normal
        if(StringUtils.isNotEmpty(property_toggle_button_normal)) {
            $(By.id(TextFieldOptionsIds.property_toggle_button_normal.name())).shouldBe(visible).click();
            $(By.id(TextFieldOptionsIds.property_toggle_button_normal.name())).shouldHave(attribute("aria-pressed", "true"));
        }

        //chars caps/uppercase
        if(StringUtils.isNotEmpty(property_toggle_button_uppercase)) {
            $(By.id(TextFieldOptionsIds.property_toggle_button_uppercase.name())).shouldBe(visible).click();
            $(By.id(TextFieldOptionsIds.property_toggle_button_uppercase.name())).shouldHave(attribute("aria-pressed", "true"));
        }

        //chars small/lowercase
        if(StringUtils.isNotEmpty(property_toggle_button_lowercase)) {
            $(By.id(TextFieldOptionsIds.property_toggle_button_lowercase.name())).shouldBe(visible).click();
            $(By.id(TextFieldOptionsIds.property_toggle_button_lowercase.name())).shouldHave(attribute("aria-pressed", "true"));
        }

        //disable Label
        if(StringUtils.isNotEmpty(checkbox_disableLabel)) {
            String checkBoxId = "#" + TextFieldOptionsIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $(checkBoxId + " input").shouldBe(selected);
        }

        //required
        if(StringUtils.isNotEmpty(checkbox_required)) {
            String checkBoxId = "#" + TextFieldOptionsIds.checkbox_required.name();
            $(checkBoxId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $(checkBoxId + " input").shouldBe(selected);
        }

        //only Alphabets
        if(StringUtils.isNotEmpty(property_onlyAlphabets_onlyAlphabets)) {
            String radioBtnId = "#" + TextFieldOptionsIds.property_onlyAlphabets_onlyAlphabets.name();
            $(radioBtnId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Alphabets and numerics
        if(StringUtils.isNotEmpty(property_alphabetsAndNumerics_alphabetsAndNumerics)) {
            String radioBtnId = "#" + TextFieldOptionsIds.property_alphabetsAndNumerics_alphabetsAndNumerics.name();
            $(radioBtnId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $(radioBtnId + " input").shouldBe(selected);
        }

        //All chars
        if(StringUtils.isNotEmpty(property_allCharacters_allCharacters)) {
            String radioBtnId = "#" + TextFieldOptionsIds.property_allCharacters_allCharacters.name();
            $(radioBtnId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $(radioBtnId + " input").shouldBe(selected);
        }

        if(minLength != null && minLength > 0) {
            String sliderId = "#property_minMaxLength_formcontrol";
            String minInputSel = sliderId + " .hidden_slider_inputs .minValue input";
            executeJavaScript("document.querySelector('#property_minMaxLength_formcontrol .hidden_slider_inputs').hidden = false;");
            selectAndClear(By.cssSelector(minInputSel)).setValue(minLength.toString()).sendKeys(Keys.TAB);
            executeJavaScript("document.querySelector('#property_minMaxLength_formcontrol .hidden_slider_inputs').hidden = true;");

            String minValue = $(sliderId + " input").getValue().split(",")[0];
            Assertions.assertEquals(minLength.toString(), minValue, "min value should be " + minLength);
        }

        if(maxLength != null && maxLength > 0) {
            String sliderId = "#property_minMaxLength_formcontrol";
            String minInputSel = sliderId + " .hidden_slider_inputs .maxValue input";
            executeJavaScript("document.querySelector('#property_minMaxLength_formcontrol .hidden_slider_inputs').hidden = false;");
            selectAndClear(By.cssSelector(minInputSel)).setValue(maxLength.toString()).sendKeys(Keys.TAB);
            executeJavaScript("document.querySelector('#property_minMaxLength_formcontrol .hidden_slider_inputs').hidden = true;");

            String maxValue = $(sliderId + " input").getValue().split(",")[1];
            Assertions.assertEquals(maxLength.toString(), maxValue, "max value should be " + maxLength);
        }

    }


    @Test
    @Order(10)
    @Disabled
    @DisplayName("Should Save And Publish Form")
    public void saveAndPublishForm() {
        $("#filled-name").setValue(formName.get()).sendKeys(Keys.TAB);
        $("#btnFormDesignSave").click();
        $(".snackbars .designerformmsgsavedSuccesfully").should(appear);

        $("#btnFormDesignPublish").click();
        $(".snackbars .designerformmsgpublishSuccesfully").should(appear);
    }


    @Test
    @Order(11)
    @Disabled
    @DisplayName("Should Find And Delete TestForm")
    public void findAndDeleteForm() {
        open("/dashboard");
        $("#formListCard").shouldBe(visible);
        $$("#formListCard .ag-row").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));

        $$("#formListCard .ag-floating-filter-input").get(0).setValue(formName.get()).sendKeys(Keys.TAB);
        $$("#formListCard .ag-pinned-left-cols-container .ag-row-first").shouldHave(CollectionCondition.size(1));

        $("#formListCard .ag-pinned-left-cols-container .ag-row-first .ag-selection-checkbox").click();
        $("#formListCard .ag-pinned-left-cols-container .ag-row-first .ag-selection-checkbox .ag-icon").shouldHave(cssClass("ag-icon-checkbox-checked"));
        $("#btnDeleteSelectedForms").should(appear).click();
        $("#btnDeleteSelectedForms").shouldBe(disabled).waitUntil(disappear, 120000); //wait max 2 minutes for deletion
    }
}
