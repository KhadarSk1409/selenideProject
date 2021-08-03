package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
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
        Arrays.asList(NumberFieldOptionsIds.values()).forEach(NumberFieldId -> $(By.id(NumberFieldId.name())).shouldBe(visible));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-NumberField-04").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForNumberFields")
    @ParameterizedTest
    @CsvFileSource(resources = "/number_field_test_data.csv", numLinesToSkip = 1)
    public void allNumberField(Integer row, Integer col, Integer colSpan,
                               String numberfield_label,
                               String numberfield_help,
                               String checkbox_disableLabel,
                               String checkbox_required,

                               String numberfield_decimalScale,
                               String numberfield_minValue,
                               String numberfield_maxValue,

                               String checkbox_readOnly,
                               String checkbox_applyFormatter,
                               String checkbox_thousandSeparator,
                               String checkbox_allowNegative,
                               String checkbox_allowLeadingZeros,
                               String checkbox_onlyInteger,
                               String numberField_defaultValueNumber,
                               String numberField_defaultValueNew
    ) {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#li-template-NumberField-04").should(appear).click();
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
        if (StringUtils.isNotEmpty(numberfield_label)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.textfield_label.name()))
                    .setValue(numberfield_label).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(numberfield_label));
        }
//
        //Help
        if (StringUtils.isNotEmpty(numberfield_help)) {
            // $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.textfield_help.name()))
                    .setValue(numberfield_help).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(numberfield_help));
        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version

            String checkBoxId = "#" + NumberFieldOptionsIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldNotHave(value(numberfield_label));
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


        //Read only checkbox
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            if (StringUtils.isEmpty(numberField_defaultValueNumber)) {
                //When you don't have any value in Default value edit box and click on Read only checkbox it should show error
                $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text("Must be set, if read only"));

                //Uncheck the readonly checkbox
                String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
                String checkBoxId1 = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_readOnly.name();
                $(checkBoxId).shouldBe(visible).click();
                $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
                $(checkBoxId + " input").shouldNotBe(selected);

                //Set the value as 0 in the Default value:
                selectAndClear(By.id(NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                        .setValue("0").sendKeys(Keys.TAB);

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
                    .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB); //Enter value in Default chekbox
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
        if (StringUtils.isNotEmpty(numberfield_decimalScale)) {
            //    $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_decimalScale.name()))
                    .setValue(numberfield_decimalScale).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_decimalScale").shouldHave(value(numberfield_decimalScale));
        }
//
        //Enter Default value
        if (StringUtils.isNotEmpty(numberField_defaultValueNumber)) {
            //  String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB);
            //  $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            if (StringUtils.isNotEmpty(numberField_defaultValueNew)) {
                $("#numberField_defaultValueNumber").shouldHave(value(numberField_defaultValueNew));
            } else {
                $("#numberField_defaultValueNumber").shouldHave(value(numberField_defaultValueNumber));
            }
        }

        //Enter Minimum Value
        if (StringUtils.isNotEmpty(numberfield_minValue)) {
            //Error verification:
            int int_numberfield_minValue = Integer.parseInt(numberfield_minValue);
            int int_numberfield_lessThanminValue = int_numberfield_minValue - 1;
            String str_numberfield_lessThanMinValue = Integer.toString(int_numberfield_lessThanminValue);

            selectAndClear(By.id(NumberFieldOptionsIds.numberField_minValue.name()))
                    .setValue(numberfield_minValue).sendKeys(Keys.TAB);
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_defaultValueNumber.name())).setValue(str_numberfield_lessThanMinValue).sendKeys(Keys.TAB);

            String errorStr = "The value " + str_numberfield_lessThanMinValue + " is smaller than minimum value " + int_numberfield_minValue;
            $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text(errorStr)); //Verify error shown
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(numberfield_minValue).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_minValue").shouldHave(value(numberfield_minValue));

            }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(numberfield_maxValue)) {
            //Error verification:
            int int_numberfield_maxValue = Integer.parseInt(numberfield_maxValue);
            int int_numberfield_moreThanMaxValue = int_numberfield_maxValue+1;
            String str_numberfield_maxValue = Integer.toString(int_numberfield_moreThanMaxValue);

                selectAndClear(By.id(NumberFieldOptionsIds.numberField_maxValue.name()))
                        .setValue(numberfield_maxValue).sendKeys(Keys.TAB);
                selectAndClear(By.id(NumberFieldOptionsIds.numberField_defaultValueNumber.name())).setValue(str_numberfield_maxValue).sendKeys(Keys.TAB);
                String errorStr = "The value "+str_numberfield_maxValue+" is greater than maximum value "+numberfield_maxValue;

                $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text(errorStr)); //Verify error shown

            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(numberfield_maxValue).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_maxValue").shouldHave(value(numberfield_maxValue));

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
    @DisplayName("verify fill form for Number fields")
    @ParameterizedTest
    @CsvFileSource(resources = "/number_field_test_data.csv", numLinesToSkip = 1)
    public void fillFormNumberFields(Integer row, Integer col, Integer colSpan,
                                     String numberfield_label,
                                     String numberfield_help,
                                     String checkbox_disableLabel,
                                     String checkbox_required,

                                     String numberfield_decimalScale,
                                     String numberfield_minValue,
                                     String numberfield_maxValue,

                                     String checkbox_readOnly,
                                     String checkbox_applyFormatter,
                                     String checkbox_thousandSeparator,
                                     String checkbox_allowNegative,
                                     String checkbox_allowLeadingZeros,
                                     String checkbox_onlyInteger,
                                     String numberField_defaultValueNumber,
                                     String numberField_defaultValueNew
    ) throws ParseException {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";
        String helpInFillForm = blockStr + " .MuiFormHelperText-root";
        String defaultValueInFillForm = blockStr + " .MuiInputBase-input";
        String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";
        String inputField = blockStr + " input";


        //Label
        if (StringUtils.isNotEmpty(numberfield_label)) {
            System.out.println("Verifying label: " + numberfield_label);
            if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
                $(labelInFillForm).shouldNotHave(text(numberfield_label)); //Verify that Label should not appear on the form - hide label
            } else {
                $(labelInFillForm).shouldHave(text(numberfield_label)); //Verify that Label appears on the form
            }
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verifying required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        //Help
        if (StringUtils.isNotEmpty(numberfield_help)) {
            System.out.println("Verifying help: " + numberfield_help);
            $(helpInFillForm).shouldHave(text(numberfield_help));
        }

        //Default value
        if (StringUtils.isNotEmpty(numberField_defaultValueNumber)) {
            System.out.println("Verifying default value: " + numberField_defaultValueNumber);
            if(StringUtils.isNotEmpty(numberfield_minValue)){
                $(defaultValueInFillForm).shouldHave(value(numberfield_minValue));
        }
            else if(StringUtils.isNotEmpty(numberfield_maxValue)){
                $(defaultValueInFillForm).shouldHave(value(numberfield_maxValue));
            }
            else if(StringUtils.isNotEmpty(numberfield_decimalScale)){
                $(defaultValueInFillForm).shouldHave(value(numberField_defaultValueNew));
            }

            else{
                $(defaultValueInFillForm).shouldHave(value(numberField_defaultValueNumber));
            }
        }

        //Read Only checkbox
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            System.out.println("Verifying checkbox readOnly");
//             $(inputField).shouldBe(disabled); //Read only should be disabled
            $(inputField).shouldHave(value("0"));  //How to verify that 0 is present in Read Only //TBD
        }

        //Thousand Separator
        if (StringUtils.isNotEmpty(checkbox_thousandSeparator)) {
            System.out.println("Verifying checkbox thousandSeparator");
            $(inputField).shouldHave(value(numberField_defaultValueNumber)); //Thousand separator should have numberField_defaultValueNumber
        }

        //Allow Negative
        if (StringUtils.isNotEmpty(checkbox_allowNegative)) {
            System.out.println("Verifying checkbox allowNegative");
            $(inputField).shouldHave(value(numberField_defaultValueNumber));
        }

        //Decimal scale value verify, that decimal places are cutted by configured amount of decimal places
        if(StringUtils.isNotEmpty(numberfield_decimalScale)){
            //construct a number decimal value
            Random r = new Random();
            int integer = r.nextInt(100);
            double decimal = r.nextDouble();
            BigDecimal bd = new BigDecimal(integer+decimal);

            //use uk number format b/c gui test user has uk locale actived per default
            DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.UK);
            df.setMaximumFractionDigits(Integer.parseInt(numberfield_decimalScale));
            df.setRoundingMode(RoundingMode.DOWN);
            df.setParseBigDecimal(true);
            System.out.println("random bigdecmial: " +  " .... " + bd.toString() + " .... " + df.format(bd));
            //try to set a big decimal value with full amount of decimal places
            selectAndClear(inputField).setValue(bd.toString()).pressTab();

            //it should have only specified amount of decimal places in gui accepted
            $(inputField).shouldHave(value(df.format(bd)));

        }

        //Allow leading zeroes:
        if (StringUtils.isNotEmpty(checkbox_allowLeadingZeros)) {
            System.out.println("Verifying allow leading zeros");
            $(inputField).shouldHave(value(numberField_defaultValueNumber));
        }

//        //Apply format
        if (StringUtils.isNotEmpty(checkbox_applyFormatter)) {
            System.out.println("Verifying apply formatter");
            $(inputField).shouldHave(value(numberField_defaultValueNumber));
        }

        //Only Integer
        if (StringUtils.isNotEmpty(checkbox_onlyInteger)) {
            System.out.println("Verifying only integer");

            //Positive scenario:
            $(inputField).shouldHave(value(numberField_defaultValueNumber));

            //Negative scenario:
            String str = RandomStringUtils.randomAlphabetic(4);
            $(inputField).setValue(str);
            $(inputField).shouldNotHave(value(str));

        }

        //Min value
        if (StringUtils.isNotEmpty(numberfield_minValue)) {
            System.out.println("Verifying Min value");

            //Positive scenario:
            $(inputField).shouldHave(value(numberfield_minValue));

            //Negative scenario:
            //Error verification:
            int int_numberfield_minValue = Integer.parseInt(numberfield_minValue);
            int int_numberField_defaultValueNumber = Integer.parseInt(numberField_defaultValueNumber);

            if (int_numberfield_minValue > int_numberField_defaultValueNumber) {
                selectAndClear(By.id(NumberFieldOptionsIds.numberField_minValue.name()))
                        .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB);
                String errorStr = "The value " + int_numberField_defaultValueNumber + " is smaller than minimum value " + int_numberfield_minValue;

                $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text(errorStr)); //Verify error shown
            }
        }

        //Max value
        if (StringUtils.isNotEmpty(numberfield_maxValue)) {
            System.out.println("Verifying Max value");
            //Positive scenario:
            $(inputField).shouldHave(value(numberfield_maxValue));

            //Negative scenario:
            //Error verification:
            int int_numberfield_maxValue = Integer.parseInt(numberfield_maxValue);
            int int_numberField_defaultValueNumber = Integer.parseInt(numberField_defaultValueNumber);

            if (int_numberfield_maxValue < int_numberField_defaultValueNumber) {
                selectAndClear(By.id(NumberFieldOptionsIds.numberField_minValue.name()))
                        .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB);
                String errorStr = "The value " + int_numberField_defaultValueNumber + " is greater than maximun value " + int_numberfield_maxValue;

                $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text(errorStr)); //Verify error shown

            }
        }
    }

    public static void main(String[] args) {
        Random r = new Random();
        int integer = r.nextInt(100);
        double decimal = r.nextDouble();
        BigDecimal bd = new BigDecimal(integer+decimal);
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.UK);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
        df.setParseBigDecimal(true);
        System.out.println("random bigdecmial: " +  " .... " + bd.toString() + " .... " + df.format(bd));

    }

}
