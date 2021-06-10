package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static reusables.ReuseActions.createNewForm;

public class DateFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Date Field Test Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    enum DateFieldOptionsIds {
        textfield_label,
        textfield_help,
        checkbox_disableLabel,
        checkbox_required,

        prop_yearMonthDay_yearMonthDay,
        prop_yearMonth_yearMonth,
        prop_year_year,

        date_minDate,
        date_maxDate,
        date_defaultValueDate, //#date_defaultValueDate ~.MuiInputAdornment-root button
        checkbox_readOnly,  //#date_minDate~.MuiInputAdornment-root button
        checkbox_disableFuture, //#date_maxDate~.MuiInputAdornment-root button
        checkbox_disablePast, //div.MuiPickersModal-dialogRoot .MuiPickersModal-dialog

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
        $("#li-template-DateField-03").should(appear).click(); //li-template-DateField-03
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(appear);

        $("#panel2a-header").should(exist).click(); //Advanced section dropdown

        //options for text field should exist:
        Arrays.asList(DateFieldTest.DateFieldOptionsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));
        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-DateField-03").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForDateFields")
    @ParameterizedTest
    @CsvFileSource(resources = "/date_field_test_data.csv", numLinesToSkip = 1)
    public void allDateField(Integer row, Integer col, Integer colSpan,
                             String textfield_label,
                             String textfield_help,
                             String checkbox_disableLabel,
                             String checkbox_required,
                             String text_numberField_defaultValueNumber,

                             String radioBtn_Date,
                             String radio_yearMonth,
                             String radio_year,
                             String textfield_minValue,
                             String textfield_maxValue,

                             String checkbox_readOnly,
                             String checkbox_disableFuture,
                             String checkbox_disablePast

    ) throws InterruptedException {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $("#li-template-DateField-03").should(appear).click();
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
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.textfield_label.name()))
                    .setValue(textfield_label).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(textfield_label)).waitUntil(appears, 4000);

        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + DateFieldTest.DateFieldOptionsIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldNotHave(value(textfield_label)).waitUntil(appears, 4000);
        }

        //Help
        if (StringUtils.isNotEmpty(textfield_help)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.textfield_help.name()))
                    .setValue(textfield_help).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(textfield_help)).waitUntil(appears, 4000);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String checkBoxId = "#" + DateFieldTest.DateFieldOptionsIds.checkbox_required.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldHave(text("Required"));
        }

        //Select Date radioBtn
        if (StringUtils.isNotEmpty(radioBtn_Date)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + DateFieldTest.DateFieldOptionsIds.prop_yearMonthDay_yearMonthDay.name();
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Select Year and Month radioBtn
        if (StringUtils.isNotEmpty(radio_yearMonth)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + DateFieldTest.DateFieldOptionsIds.prop_yearMonthDay_yearMonthDay.name();
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Select Year radioBtn
        if (StringUtils.isNotEmpty(radio_year)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + DateFieldTest.DateFieldOptionsIds.prop_yearMonthDay_yearMonthDay.name();
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Select Default
        if (StringUtils.isNotEmpty(text_numberField_defaultValueNumber)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.date_defaultValueDate.name()))
                    .setValue(text_numberField_defaultValueNumber).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $("#date_defaultValueDate").shouldHave(value(text_numberField_defaultValueNumber));
        }


        //Read only checkbox check
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + DateFieldTest.DateFieldOptionsIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $("#date_defaultValueDate-helper-text").should(exist).shouldHave(text("Must be set, if read only")); //Verify the error shown when read only checkbox is checked wihtout any value in default value field
        }

        //Disable future checkbox check
        if (StringUtils.isNotEmpty(checkbox_disableFuture)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + DateFieldTest.DateFieldOptionsIds.checkbox_disableFuture.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Disable past checkbox check
        if (StringUtils.isNotEmpty(checkbox_disablePast)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + DateFieldTest.DateFieldOptionsIds.checkbox_disablePast.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Enter Minimum Value
        if (StringUtils.isNotEmpty(textfield_minValue)) {
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.date_minDate.name()))
                    .setValue(textfield_minValue).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#date_minDate").shouldHave(value(textfield_minValue)).waitUntil(appears, 4000);
        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(textfield_maxValue)) {
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.date_maxDate.name()))
                    .setValue(textfield_maxValue).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#date_maxDate").shouldHave(value(textfield_maxValue)).waitUntil(appears, 4000);
        }

    }
}
