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

public class TimeFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Time Field Test Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    enum TimeFieldOptionsIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,

        prop_hourMinuteSecond_hourMinuteSecond,
        prop_hourMinute_hourMinute,
        prop_minuteSecond_minuteSecond,
        prop_hour_hour,

        time_defaultValueTime,
        checkbox_readOnly,
        checkbox_ampm;
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
        $("#li-template-TimeField-03").should(appear).click(); //li-template-TimeField-03
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(appear);

        $("#panel2a-header").should(exist).click(); //Advanced section dropdown

        //options for text field should exist:
        Arrays.asList(TimeFieldTest.TimeFieldOptionsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));
        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-TimeField-03").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForDateFields")
    @ParameterizedTest
    @CsvFileSource(resources = "/time_field_test_data.csv", numLinesToSkip = 1)
    public void allTimeField(Integer row, Integer col, Integer colSpan,
                             String timefield_label,
                             String timefield_help,
                             String checkbox_disableLabel,
                             String checkbox_required,

                             String radioBtn_hrMinSec_hrMinSec,
                             String radio_hrMin_hrMin,
                             String radio_hour_hour,
                             String time_defaultValueTime,

                             String checkbox_readOnly,
                             String checkbox_ampm

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
        $("#li-template-TimeField-03").should(appear).click();
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
        if (StringUtils.isNotEmpty(timefield_label)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TimeFieldTest.TimeFieldOptionsIds.textfield_label.name()))
                    .setValue(timefield_label).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(timefield_label)).waitUntil(appears, 4000);

        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + TimeFieldTest.TimeFieldOptionsIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldNotHave(value(timefield_label)).waitUntil(appears, 4000);
        }

        //Help
        if (StringUtils.isNotEmpty(timefield_help)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TimeFieldTest.TimeFieldOptionsIds.textfield_help.name()))
                    .setValue(timefield_help).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(timefield_help)).waitUntil(appears, 4000);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String checkBoxId = "#" + TimeFieldTest.TimeFieldOptionsIds.checkbox_required.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldHave(text("Required"));
        }

        //Hour Minute Second radioBtn
        if (StringUtils.isNotEmpty(radioBtn_hrMinSec_hrMinSec)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + TimeFieldTest.TimeFieldOptionsIds.prop_hourMinuteSecond_hourMinuteSecond.name();
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Hour Minute radioBtn
        if (StringUtils.isNotEmpty(radio_hrMin_hrMin)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + TimeFieldTest.TimeFieldOptionsIds.prop_hourMinute_hourMinute.name();
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Minute Second radioBtn
        if (StringUtils.isNotEmpty(radio_hour_hour)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + TimeFieldTest.TimeFieldOptionsIds.prop_minuteSecond_minuteSecond.name();
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }


        //Hour radioBtn
        if (StringUtils.isNotEmpty(radio_hour_hour)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + TimeFieldTest.TimeFieldOptionsIds.prop_hour_hour.name();
            $(radioBtnId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Enter Default value
        if (StringUtils.isNotEmpty(time_defaultValueTime)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TimeFieldTest.TimeFieldOptionsIds.time_defaultValueTime.name()))
                    .setValue(time_defaultValueTime).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $("#time_defaultValueTime").shouldHave(value(time_defaultValueTime));
        }

        //Read only checkbox check
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + TimeFieldTest.TimeFieldOptionsIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            if (StringUtils.isNotEmpty(time_defaultValueTime)) {
                $("#time_defaultValueTime-helper-text").should(exist).shouldHave(text("Must be set, if read only")); //Verify the error shown when read only checkbox is checked wihtout any value in default value field
            }
        }

        //Verify 12h/24h checkbox for hour selection
        if (StringUtils.isNotEmpty(checkbox_ampm)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + TimeFieldTest.TimeFieldOptionsIds.checkbox_ampm.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }
    }
}
