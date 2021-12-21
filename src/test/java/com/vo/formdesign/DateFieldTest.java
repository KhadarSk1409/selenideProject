package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.text.*;
import java.time.Duration;
import java.util.Scanner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsFormCreation.*;

import java.util.Date;
import java.util.Calendar;

public class DateFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Date Field Test Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    public enum DateFieldOptionsIds {
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
    public void precondition() throws IOException {
        navigateToFormDesign(FormField.DATE);

    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForDateFields")
    @ParameterizedTest
    @CsvFileSource(resources = "/date_field_test_data.csv", numLinesToSkip = 1)
    public void allDateField(Integer row, Integer col, Integer colSpan,
                             String text_label,
                             String text_help,
                             String checkbox_disableLabel,
                             String checkbox_required,
                             String text_timeField_defaultValueTime,

                             String radioBtn_Date,
                             String radio_yearMonth,
                             String radio_year,
                             String date_minValue,
                             String date_maxValue,

                             String checkbox_readOnly,
                             String checkbox_disableFuture,
                             String checkbox_disablePast

    ) throws ParseException {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;


        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //Getting current date
        Calendar cal = Calendar.getInstance();

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $(elementLocators("DateField")).should(appear).click();
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
        if (StringUtils.isNotEmpty(text_label)) {
            labelVerificationOnFormDesign(blockId, text_label);

        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, text_label);
        }

        //Help
        if (StringUtils.isNotEmpty(text_help)) {
            helpVerificationOnFormDesign(blockId, text_help);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        //Select Date radioBtn
        if (StringUtils.isNotEmpty(radioBtn_Date)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + DateFieldTest.DateFieldOptionsIds.prop_yearMonthDay_yearMonthDay.name();
            $(radioBtnId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Select Year and Month radioBtn
        if (StringUtils.isNotEmpty(radio_yearMonth)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + DateFieldTest.DateFieldOptionsIds.prop_yearMonthDay_yearMonthDay.name();
            $(radioBtnId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Select Year radioBtn
        if (StringUtils.isNotEmpty(radio_year)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String radioBtnId = "#" + DateFieldTest.DateFieldOptionsIds.prop_yearMonthDay_yearMonthDay.name();
            $(radioBtnId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(radioBtnId + " input").shouldBe(selected);
        }

        //Select Default
        if (StringUtils.isNotEmpty(text_timeField_defaultValueTime)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.date_defaultValueDate.name()))
                    .setValue(text_timeField_defaultValueTime).sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(elementLocators("DefaultDate")).shouldHave(value(text_timeField_defaultValueTime));
        }


        //Read only checkbox check
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + DateFieldTest.DateFieldOptionsIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            if (StringUtils.isEmpty(text_timeField_defaultValueTime)) {
                $(elementLocators("DefaultDateHelperText")).should(exist).shouldHave(text("Must be set, if read only")); //Verify the error shown when read only checkbox is checked wihtout any value in default value field

                //Uncheck the readonly checkbox
                String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
                $(checkBoxId).shouldBe(visible).click();
                $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
                $(checkBoxId + " input").shouldNotBe(selected); //Uncheck the Read only checkbox

                //Set the value in the Default value:
                selectAndClear(By.id(DateFieldOptionsIds.date_defaultValueDate.name()))
                        .setValue("01/01/2020").sendKeys(Keys.TAB);
                $(By.id(DateFieldOptionsIds.date_defaultValueDate.name())).shouldHave(value("01/01/2020"));

                $(checkBoxId).shouldBe(visible).click();
                $(checkBoxId + " input").shouldBe(selected);
            }
        }

        //Disable future checkbox check
        if (StringUtils.isNotEmpty(checkbox_disableFuture)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + DateFieldTest.DateFieldOptionsIds.checkbox_disableFuture.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Disable past checkbox check
        if (StringUtils.isNotEmpty(checkbox_disablePast)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + DateFieldTest.DateFieldOptionsIds.checkbox_disablePast.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Enter Minimum Value
        if (StringUtils.isNotEmpty(date_minValue)) {
            String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.date_minDate.name()))
                    .setValue(date_minValue).sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $(elementLocators("MinDate")).shouldHave(value(date_minValue)).should(appear, Duration.ofSeconds(5));
            //#panel2a-content div:nth-child(7) input

            //Verification of error
            //Convert min value string to date
            Date minDate = sdf.parse(date_minValue);
            cal.setTime(minDate); //Setting date to given date

            //Add one day to min date
            //Number of Days to add
            cal.add(Calendar.DAY_OF_MONTH, -1);

            //Date after adding the days to the current date
            String previousDate = sdf.format(cal.getTime());

            String initialVerNumStr3 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldOptionsIds.date_defaultValueDate.name()))
                    .setValue(previousDate).sendKeys(Keys.TAB); //Enter one day ahead date value
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr3)); //Verify that version has increased
            $(elementLocators("DefaultDateHelperText")).shouldHave(text("is before than minimum date"));

            //Correcting the default value
            String initialVerNumStr4 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.date_defaultValueDate.name()))
                    .setValue("").sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr4)); //Verify that version has increased
            $(blockId + " .MuiFormHelperText-root").shouldNotHave(text("is before than minimum date"));


        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(date_maxValue)) {
            String initialVerNumStr2 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.date_maxDate.name()))
                    .setValue(date_maxValue).sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $(elementLocators("MaxDate")).shouldHave(value(date_maxValue)).should(appear, Duration.ofSeconds(5));
            //#panel2a-content div:nth-child(8) input

            //Verification of error
            //Convert min value string to date
            Date maxDate = sdf.parse(date_maxValue);

            cal.setTime(maxDate); //Setting date to given date

            //Add one day to min date
            //Number of Days to add
            cal.add(Calendar.DAY_OF_MONTH, 1);

            //Date after adding the days to the current date
            String futureDate = sdf.format(cal.getTime());

            String initialVerNumStr3 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldOptionsIds.date_defaultValueDate.name()))
                    .setValue(futureDate).sendKeys(Keys.TAB); //Enter default value
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr3)); //Verify that version has increased
            $(elementLocators("DefaultDateHelperText")).shouldHave(text("is after than maximum date"));

            //Correcting the default value
            String initialVerNumStr4 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(DateFieldTest.DateFieldOptionsIds.date_defaultValueDate.name()))
                    .setValue("").sendKeys(Keys.TAB);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr4)); //Verify that version has increased
            $(blockId + " .MuiFormHelperText-root").shouldNotHave(text("is before than maximum date"));

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
        $(elementLocators("FillFormButton")).should(exist).click(); //Fill form button on Launch screen
        $(elementLocators("DataContainer")).should(appear); //Verify that the form details screen appears

    }

    @Order(4)
    @DisplayName("verify fields on form")
    @ParameterizedTest
    @CsvFileSource(resources = "/date_field_test_data.csv", numLinesToSkip = 1)
    public void verifyFieldsOnForm(Integer row, Integer col, Integer colSpan,
                                   String text_label,
                                   String text_help,
                                   String checkbox_disableLabel,
                                   String checkbox_required,
                                   String text_timeField_defaultValueTime,

                                   String radioBtn_Date,
                                   String radio_yearMonth,
                                   String radio_year,
                                   String textfield_minValue,
                                   String textfield_maxValue,

                                   String checkbox_readOnly,
                                   String checkbox_disableFuture,
                                   String checkbox_disablePast
    ) throws ParseException {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";
        String helpInFillForm = blockStr + " .MuiFormHelperText-root";
        String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";
        String inputField = blockStr + " input";

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //Getting current date
        Calendar cal = Calendar.getInstance();
        //Displaying current date in the desired format
        System.out.println("Current Date: " + sdf.format(cal.getTime()));


        //Label
        if (StringUtils.isNotEmpty(text_label)) {
            System.out.println("Verifying label: " + text_label);
            if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
                $(labelInFillForm).shouldNotHave(text(text_label)); //Verify that Label should not appear on the form - hide label
            } else {
                $(labelInFillForm).shouldHave(text(text_label)); //Verify that Label appears on the form
            }
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verifying required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        //Help
        if (StringUtils.isNotEmpty(text_help)) {
            System.out.println("Verifying help: " + text_help);
            $(helpInFillForm).shouldHave(text(text_help));
        }


        //Default value
        if (StringUtils.isNotEmpty(text_timeField_defaultValueTime)) {
            System.out.println("Verifying default value: " + text_timeField_defaultValueTime);
            $(inputField).shouldHave(value(text_timeField_defaultValueTime));
        }


//        //Read Only checkbox
//        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
//            System.out.println("Verifying checkbox readOnly");
//            System.out.println("Verifying for value: " + text_timeField_defaultValueTime);
//            $(inputField).shouldBe(disabled);
//        }

        //Disable Future
        //Enter future date and ensure that system does not allow or show relevant error
        if (StringUtils.isNotEmpty(checkbox_disableFuture)) {

            //Number of Days to add
            cal.add(Calendar.DAY_OF_MONTH, 1);
            //Date after adding the days to the current date
            String futureDate = sdf.format(cal.getTime());
            //Displaying the new Date after addition of Days to current date
            System.out.println("Future Date: " + futureDate);

            $(inputField).setValue(futureDate); //Enter future date in the Disable future field

            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Date should not be after maximal date")); //Verify the error shown when user enters future date

        }


        //Disable Past
        //Enter past date and ensure that system does not allow or show relevant error
        if (StringUtils.isNotEmpty(checkbox_disablePast)) {

            //Number of Days to add
            cal.add(Calendar.DAY_OF_MONTH, -1);
            //Date after adding the days to the current date
            String pastDate = sdf.format(cal.getTime());
            //Displaying the new Date after addition of Days to current date
            System.out.println("Past Date: " + pastDate);

            $(inputField).setValue(pastDate); //Enter future date in the Disable future field

            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Date should not be before minimal date")); //Verify the error shown when user enters future date

        }

        //Minimum date
        //Enter date less than minimum date and ensure that system does not allow or shows relevant error
        if (StringUtils.isNotEmpty(textfield_minValue)) {

            Date minDate = sdf.parse(textfield_minValue);
            cal.setTime(minDate); //Setting date to given date

            //Add one day to min date
            //Number of Days to add
            cal.add(Calendar.DAY_OF_MONTH, -1);


            //Date after adding the days to the current date
            String previousDate = sdf.format(cal.getTime());
            selectAndClear(inputField).setValue(previousDate).sendKeys(Keys.TAB); //Set min value as previous date

            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Date should not be before minimal date")); //Verify the error shown when user enters future date

        }

        //Maximum date
        //Enter date more than max date and ensure that system does not allow or shows relevant error
        if (StringUtils.isNotEmpty(textfield_maxValue)) {
            Date maxDate = sdf.parse(textfield_maxValue);

            cal.setTime(maxDate); //Setting date to given date

            //Add one day to min date
            //Number of Days to add
            cal.add(Calendar.DAY_OF_MONTH, 1);


            //Date after adding the days to the current date
            String nextDate = sdf.format(cal.getTime());
            selectAndClear(inputField).setValue(nextDate).sendKeys(Keys.TAB); //Set min value as previous date

            $(blockStr + " .MuiFormHelperText-root").shouldHave(text("Date should not be after maximal date")); //Verify the error shown when user enters future date

        }

    }

}


