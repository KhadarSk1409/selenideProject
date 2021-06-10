package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Number Field Creation Tests")
public class NumberFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Number Field Test Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    enum NumberFieldOptionsIds {
        textfield_label,
        textfield_help,
        checkbox_disableLabel,
        checkbox_required,

        numberField_defaultValueNumber,
        numberField_decimalScale,
        numberField_minValue,
        numberField_maxValue,

        checkbox_readOnly,
        checkbox_applyFormatter,
        checkbox_thousandSeparator,
        checkbox_allowNegative,
        checkbox_allowLeadingZeros,
        checkbox_onlyInteger;

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
        $("#li-template-NumberField-04").should(appear).click();
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(appear);

        $("#panel2a-header").should(exist).click(); //Advanced section dropdown

        //options for text field should exist:
        Arrays.asList(NumberFieldOptionsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-NumberField-04").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForNumberFields")
    @ParameterizedTest
    @CsvFileSource(resources = "/number_field_test_data.csv", numLinesToSkip = 1)
    public void allNumberField(Integer row, Integer col, Integer colSpan,
                               String textfield_label,
                               String textfield_help,
                               String checkbox_disableLabel,
                               String checkbox_required,

                               String text_numberField_defaultValueNumber,
                               String textfield_decimalScale,
                               String textfield_minValue,
                               String textfield_maxValue,

                               String checkbox_readOnly,
                               String checkbox_applyFormatter,
                               String checkbox_thousandSeparator,
                               String checkbox_allowNegative,
                               String checkbox_allowLeadingZeros,
                               String checkbox_onlyInteger
    )  {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $("#li-template-NumberField-04").should(appear).click();
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
            selectAndClear(By.id(NumberFieldOptionsIds.textfield_label.name()))
                    .setValue(textfield_label).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(textfield_label)).waitUntil(appears, 4000);
        }

        //Help
        if (StringUtils.isNotEmpty(textfield_help)) {
            // $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.textfield_help.name()))
                    .setValue(textfield_help).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(textfield_help)).waitUntil(appears, 4000);
        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version

            String checkBoxId = "#" + NumberFieldOptionsIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldNotHave(value(textfield_label)).waitUntil(appears, 4000);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_required.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Enter Default value
        if (StringUtils.isNotEmpty(text_numberField_defaultValueNumber)) {
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(text_numberField_defaultValueNumber).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_defaultValueNumber").shouldHave(value(text_numberField_defaultValueNumber)).waitUntil(appears, 4000);
        }


        //Read only checkbox
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            if (StringUtils.isEmpty(text_numberField_defaultValueNumber)) {
                //When you don't have any value in Default value edit box and click on Read only checkbox it should show error
                $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text("Must be set, if read only"));
            }

        }

        //Apply user format checkbox check
        if (StringUtils.isNotEmpty(checkbox_applyFormatter)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_applyFormatter.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Thousand Separator checkbox check
        if (StringUtils.isNotEmpty(checkbox_thousandSeparator)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(text_numberField_defaultValueNumber).sendKeys(Keys.TAB); //Enter value in Default chekbox
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_thousandSeparator.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

        }

        //Allow Negative checkbox
        if (StringUtils.isNotEmpty(checkbox_allowNegative)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_allowNegative.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Allow leading zeros
        if (StringUtils.isNotEmpty(checkbox_allowLeadingZeros)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_allowLeadingZeros.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Only integer
        if (StringUtils.isNotEmpty(checkbox_onlyInteger)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_onlyInteger.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            //here also check that after this is checked the Decimal places textfield should be disabled
            $("#numberField_decimalScale").shouldBe(disabled); //Decimal places
        }

        //Enter Decimal Places
        if (StringUtils.isNotEmpty(textfield_decimalScale)) {
            //    $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_decimalScale.name()))
                    .setValue(textfield_decimalScale).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_decimalScale").shouldHave(value(textfield_decimalScale)).waitUntil(appears, 4000);
        }

        //Enter Minimum Value
        if (StringUtils.isNotEmpty(textfield_minValue)) {
            //    $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_minValue.name()))
                    .setValue(textfield_minValue).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_minValue").shouldHave(value(textfield_minValue)).waitUntil(appears, 4000);
        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(textfield_maxValue)) {
            //    $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_maxValue.name()))
                    .setValue(textfield_maxValue).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_maxValue").shouldHave(value(textfield_maxValue)).waitUntil(appears, 4000);
        }

    }
}
