package com.vo.formdesign;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import reusables.ReuseActionsFormCreation;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.*;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsFormCreation.*;

public class TimeFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Time Field Test Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    public enum TimeFieldOptionsIds {
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
    public void precondition() throws IOException {
        navigateToFormDesign(FormField.TIME_FIELD);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForTimeFields")
    @ParameterizedTest
    @CsvFileSource(resources = "/time_field_test_data.csv", numLinesToSkip = 1)
    public void allTimeField(Integer row, Integer col, Integer colSpan,
                             String timefield_label,
                             String timefield_help,
                             String checkbox_disableLabel,
                             String time_defaultValueTime,
                             String checkbox_required,

                             String radioBtn_hrMinSec,
                             String radio_hrMin,
                             String radio_minSec,
                             String radio_hour,

                             String checkbox_readOnly,
                             String checkbox_24hr

    ) throws InterruptedException {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $(elementLocators("TimeField")).should(appear).click();
        $(elementLocators("FormPropertiesCard")).should(appear);
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version has increased

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
        if (StringUtils.isNotEmpty(timefield_label)) {
            labelVerificationOnFormDesign(blockId, timefield_label);

        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, timefield_label);
        }

        //Help
        if (StringUtils.isNotEmpty(timefield_help)) {
            helpVerificationOnFormDesign(blockId, timefield_help);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        //Hour Minute Second radioBtn
        if (StringUtils.isNotEmpty(radioBtn_hrMinSec)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + TimeFieldTest.TimeFieldOptionsIds.prop_hourMinuteSecond_hourMinuteSecond.name();
            $(radioBtnId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Hour Minute radioBtn
        if (StringUtils.isNotEmpty(radio_hrMin)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String radioBtnId = "#" + TimeFieldTest.TimeFieldOptionsIds.prop_hourMinute_hourMinute.name();
            $(radioBtnId).shouldBe(visible).click();
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Minute Second radioBtn
        if (StringUtils.isNotEmpty(radio_minSec)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + TimeFieldTest.TimeFieldOptionsIds.prop_minuteSecond_minuteSecond.name();
            $(radioBtnId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }


        //Hour radioBtn
        if (StringUtils.isNotEmpty(radio_hour)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + TimeFieldTest.TimeFieldOptionsIds.prop_hour_hour.name();
            $(radioBtnId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Enter Default value
        if (StringUtils.isNotEmpty(time_defaultValueTime)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(TimeFieldTest.TimeFieldOptionsIds.time_defaultValueTime.name()))
                    .setValue(time_defaultValueTime).sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(elementLocators("DefaultTime")).shouldHave(value(time_defaultValueTime));


        }

        //Read only checkbox check
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + TimeFieldTest.TimeFieldOptionsIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            //Verify Read Only with Default:
            if (StringUtils.isNotEmpty(time_defaultValueTime)) {
                selectAndClear(By.id(TimeFieldTest.TimeFieldOptionsIds.time_defaultValueTime.name()))
                        .setValue(time_defaultValueTime).sendKeys(Keys.TAB);
                $(elementLocators("DefaultTime")).shouldHave(value(time_defaultValueTime));
            } else {
                $(elementLocators("DefaultTimeHelperText")).should(exist).shouldHave(text("Must be set, if read only")); //Verify the error shown when read only checkbox is checked wihtout any value in default value field
                Thread.sleep(2000);
                //Set some value in Default value
                selectAndClear(By.id(TimeFieldTest.TimeFieldOptionsIds.time_defaultValueTime.name()))
                        .setValue("01:00").sendKeys(Keys.TAB);
                Thread.sleep(2000);
                $(elementLocators("DefaultTime")).shouldHave(value("01:00"));
            }
            $(checkBoxId).shouldBe(visible).click();
            $(checkBoxId + " input").shouldNotBe(selected);
        }

        //Verify 12h/24h checkbox for hour selection
        if (StringUtils.isNotEmpty(checkbox_24hr)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 =$(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + TimeFieldTest.TimeFieldOptionsIds.checkbox_ampm.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
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
    @CsvFileSource(resources = "/time_field_test_data.csv", numLinesToSkip = 1)
    public void verifyFieldsOnForm(Integer row, Integer col, Integer colSpan,
                                   String timefield_label,
                                   String timefield_help,
                                   String checkbox_disableLabel,
                                   String time_defaultValueTime,
                                   String checkbox_required,

                                   String radioBtn_hrMinSec,
                                   String radio_hrMin,
                                   String radio_minSec,
                                   String radio_hour,

                                   String checkbox_readOnly,
                                   String checkbox_24hr
    ) throws ParseException {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";
        String helpInFillForm = blockStr + " .MuiFormHelperText-root";
        String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";
        String inputField = blockStr + " input";


        //Label
        if (StringUtils.isNotEmpty(timefield_label)) {
            System.out.println("Verifying label: " + timefield_label);
            if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
                $(labelInFillForm).shouldNotHave(text(timefield_label)); //Verify that Label should not appear on the form - hide label
            } else {
                $(labelInFillForm).shouldHave(text(timefield_label)); //Verify that Label appears on the form
            }
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verifying required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        //Help
        if (StringUtils.isNotEmpty(timefield_help)) {
            System.out.println("Verifying help: " + timefield_help);
            $(helpInFillForm).shouldHave(text(timefield_help));
        }


        //Default value
        if (StringUtils.isNotEmpty(time_defaultValueTime)) {
            System.out.println("Verifying default value: " + time_defaultValueTime);
             $(inputField).shouldHave(value(time_defaultValueTime));
        }


//        //Read Only checkbox
//        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
//            System.out.println("Verifying checkbox readOnly");
//            System.out.println("Verifying for value: " + time_defaultValueTime);
//            $(inputField).shouldBe(disabled);
//        }

        //Verify on Fill form radioBtn_hrMinSec_hrMinSec
        if (StringUtils.isNotEmpty(radioBtn_hrMinSec)) {
            System.out.println("Verifying radioBtn_hrMinSec_hrMinSec field");

            selectAndClear(inputField).setValue("01:05").sendKeys(Keys.TAB); //Enter hour and minutes values
            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Invalid Time Format")); //Verify the error shown when user enters Inavlid time format

            selectAndClear(inputField).setValue("23:59:59").sendKeys(Keys.TAB); //Enter hour, minutes and seconds values
            $(inputField).shouldHave(value("23:59:59"));

            selectAndClear(inputField).setValue("00:59:59").sendKeys(Keys.TAB); //Enter hour, minutes and seconds values
            $(inputField).shouldHave(value("00:59:59"));

            selectAndClear(inputField).setValue("00:60:59").sendKeys(Keys.TAB); //Enter hour, minutes and seconds values
            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Invalid Time Format")); //Verify the error shown when user enters Inavlid time format

            selectAndClear(inputField).setValue("00:00:60").sendKeys(Keys.TAB); //Enter hour, minutes and seconds values
            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Invalid Time Format")); //Verify the error shown when user enters Inavlid time format

        }

        //Verify on Fill form radio_hrMin_hrMin
        if (StringUtils.isNotEmpty(radio_hrMin)) {
            System.out.println("Verifying radioBtn_hrMin field");

            selectAndClear(inputField).setValue("01").sendKeys(Keys.TAB); //Enter hour value
            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Invalid Time Format")); //Verify the error shown when user enters Inavlid time format

            selectAndClear(inputField).setValue("00:00").sendKeys(Keys.TAB); //Enter valid boundry value
            $(inputField).shouldHave(value("00:00"));

            selectAndClear(inputField).setValue("23:60").sendKeys(Keys.TAB); //Enter invalid boundry value
            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Invalid Time Format")); //Verify the error shown when user enters Inavlid time format

            selectAndClear(inputField).setValue("12:59").sendKeys(Keys.TAB);
            $(inputField).shouldHave(value("12:59"));
        }

        //Verify radio_minSec
        if (StringUtils.isNotEmpty(radio_minSec)) {
            System.out.println("Verifying radio_minSec field");

            selectAndClear(inputField).setValue("01").sendKeys(Keys.TAB); //Enter hour value
            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Invalid Time Format")); //Verify the error shown when user enters Inavlid time format

            selectAndClear(inputField).setValue("59:59").sendKeys(Keys.TAB); //Enter hour min value
            $(inputField).shouldHave(value("59:59"));

            selectAndClear(inputField).setValue("00:00").sendKeys(Keys.TAB); //Enter hour min value
            $(inputField).shouldHave(value("00:00"));

            selectAndClear(inputField).setValue("60:00").sendKeys(Keys.TAB); //Enter hour min value
            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Invalid Time Format")); //Verify the error shown when user enters Inavlid time format

            selectAndClear(inputField).setValue("00:60").sendKeys(Keys.TAB); //Enter hour min value
            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Invalid Time Format")); //Verify the error shown when user enters Inavlid time format

        }

        //Verify radio_hour
        if (StringUtils.isNotEmpty(radio_hour)) {
            System.out.println("Verifying radio_hour field");

            selectAndClear(inputField).setValue("01:00").sendKeys(Keys.TAB);
            $(inputField).shouldHave(value("01"));

            selectAndClear(inputField).setValue("00").sendKeys(Keys.TAB);
            $(inputField).shouldHave(value("00"));

            selectAndClear(inputField).setValue("24").sendKeys(Keys.TAB);
            $(inputField).shouldHave(value("00")); //Verify that when user enters 24, it is converted in 00

            selectAndClear(inputField).setValue("25").sendKeys(Keys.TAB);
            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Invalid Time Format")); //Verify the error shown when user enters Inavlid time format

        }

    }

}
