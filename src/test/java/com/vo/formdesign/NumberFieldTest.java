package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Number Field Creation Tests")
public class NumberFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Number Field Test Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    private static DecimalFormat getDecimalFormat(String textfield_decimalScale, String checkbox_thousandSeparator) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.UK); //GUI Test user is per default in Locale UK
        if (StringUtils.isNotEmpty(checkbox_thousandSeparator)) {
            df.setGroupingUsed(true);
        } else {
            df.setGroupingUsed(false);
        }
        if (StringUtils.isNotEmpty(textfield_decimalScale)) {
            df.setMaximumFractionDigits(Integer.parseInt(textfield_decimalScale));
        }
        df.setRoundingMode(RoundingMode.DOWN);
        df.setParseBigDecimal(true);
        return df;
    }

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() {
        navigateToFormDesign("Number Field");
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForNumberFields")
    @ParameterizedTest
    @CsvFileSource(resources = "/number_field_test_data.csv", numLinesToSkip = 1)
    public void allNumberField(Integer row, Integer col, Integer colSpan,
                               String label_text,
                               String help_text,
                               String checkbox_disableLabel,
                               String checkbox_required,

                               String numberfield_decimalScale,
                               String minValue,
                               String maxValue,

                               String checkbox_readOnly,
                               String checkbox_applyFormatter,
                               String checkbox_thousandSeparator,
                               String checkbox_allowNegative,
                               String checkbox_allowLeadingZeros,
                               String checkbox_onlyInteger,
                               String numberField_defaultValueNumber
    ) throws IOException {

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

        DecimalFormat df = getDecimalFormat(numberfield_decimalScale, checkbox_thousandSeparator);

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
        if (StringUtils.isNotEmpty(label_text)) {
//            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
//            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
//            selectAndClear(By.id(NumberFieldOptionsIds.textfield_label.name()))
//                    .setValue(label_text).sendKeys(Keys.TAB);
//            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
//            $(blockId).shouldHave(text(label_text));
            labelVerificationOnFormDesign(blockId,label_text);
        }
//
        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
//            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
//            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
//
//            String checkBoxId = "#" + NumberFieldOptionsIds.checkbox_disableLabel.name();
//            $(checkBoxId).shouldBe(visible).click();
//            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
//            $(checkBoxId + " input").shouldBe(selected);
//            $(blockId).shouldNotHave(value(label_text));
            hideLabelVerificationOnFormDesign(blockId, label_text);
        }

        //Help
        if (StringUtils.isNotEmpty(help_text)) {
            // $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
//            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
//            selectAndClear(By.id(NumberFieldOptionsIds.textfield_help.name()))
//                    .setValue(help_text).sendKeys(Keys.TAB);
//            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
//            $(blockId).shouldHave(text(help_text));
            helpVerificationOnFormDesign(blockId, help_text);
        }


        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
//            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
//            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_required.name();
//            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
//            $(checkBoxId).shouldBe(visible).click();
//            //$(checkBoxId + " input").shouldHave(value("true"));
//            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
//            $(checkBoxId + " input").shouldBe(selected);
            requiredCheckboxVerificationOnFormDesign(blockId);
        }
//

        //Read only checkbox
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {

            readOnlyCheckboxOnFormDesign(blockId);

                //When you don't have any value in Default value edit box and click on Read only checkbox it should show error
                $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text("Must be set, if read only"));

                //Uncheck the readonly checkbox
                String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_readOnly.name();
                String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
                $(checkBoxId).shouldBe(visible).click();
                $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
                $(checkBoxId + " input").shouldNotBe(selected); //Uncheck the Read only checkbox

                //Set the value in the Default value:
                selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                        .setValue("1").sendKeys(Keys.TAB);
                $(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name())).shouldHave(value("1"));

            $(checkBoxId).shouldBe(visible).click();
            $(checkBoxId + " input").shouldBe(selected);

        }

        //Apply user format checkbox check
        if (StringUtils.isNotEmpty(checkbox_applyFormatter)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_applyFormatter.name();
            $(checkBoxId).shouldBe(visible).click();

            //Set the number in Default value:
            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB); //Enter value
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            $(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name())).shouldHave(value(numberField_defaultValueNumber));

            //Now apply format and verify
            String applyFormatStr = df.format(new BigDecimal(numberField_defaultValueNumber));

            //Set the number in Default value:
            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(applyFormatStr).sendKeys(Keys.TAB); //Enter value

            $(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name())).shouldHave(value(applyFormatStr));
        }

        //Thousand Separator checkbox check
        if (StringUtils.isNotEmpty(checkbox_thousandSeparator)) {
            $(By.id(NumberFieldTest.NumberFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB); //Enter value in Default chekbox

            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_thousandSeparator.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            //Verify the changed format:
            if (StringUtils.isNotEmpty(numberField_defaultValueNumber)) {
                $(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                        .shouldHave(value(df.format(new BigDecimal(numberField_defaultValueNumber))));
            }
        }

        //Allow Negative checkbox
        if (StringUtils.isNotEmpty(checkbox_allowNegative)) {
            $(By.id(NumberFieldTest.NumberFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
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


        //Enter Default value
        if (StringUtils.isNotEmpty(numberField_defaultValueNumber)) {
            $(By.id(NumberFieldTest.NumberFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
            $(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name())).should(exist);
            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB);
            if (StringUtils.isNotEmpty(numberfield_decimalScale)) {
                $("#numberField_defaultValueNumber").shouldHave(value("1234.56"));
                selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_decimalScale.name())).sendKeys(Keys.TAB); //Clear the decimal scale field
                selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                        .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB); //Again set numberField_defaultValueNumber
            } else {
                $("#numberField_defaultValueNumber")
                        .shouldHave(value(df.format(new BigDecimal(numberField_defaultValueNumber))));
            }
        }


        //Enter Minimum Value
        if (StringUtils.isNotEmpty(minValue)) {
            //Error verification:
            $(By.id(NumberFieldTest.NumberFieldOptionsIds.textfield_label.name()))
                    .shouldHave(text(label_text));
            int int_numberfield_minValue = Integer.parseInt(minValue);
            int int_numberfield_lessThanminValue = int_numberfield_minValue - 1;
            String str_numberfield_lessThanMinValue = Integer.toString(int_numberfield_lessThanminValue);

            $(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_minValue.name())).should(exist);
            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_minValue.name()))
                    .setValue(minValue).sendKeys(Keys.TAB);
            $(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_minValue.name())).shouldHave(value(minValue));

            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name())).setValue(str_numberfield_lessThanMinValue).sendKeys(Keys.TAB);
            $(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name())).shouldHave(value(str_numberfield_lessThanMinValue));

            String errorStr = "The value " + str_numberfield_lessThanMinValue + " is smaller than minimum value " + int_numberfield_minValue;
            $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text(errorStr)); //Verify error shown

            //Set the allowable value again:
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_minValue").shouldHave(value(numberField_defaultValueNumber));

        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(maxValue)) {
            //Error verification:
            $(By.id(NumberFieldTest.NumberFieldOptionsIds.textfield_label.name()))
                    .shouldHave(text(label_text));
            int int_numberfield_maxValue = Integer.parseInt(maxValue);
            int int_numberfield_moreThanMaxValue = int_numberfield_maxValue + 1;
            String str_numberfield_maxValue = Integer.toString(int_numberfield_moreThanMaxValue);

            $(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_maxValue.name())).should(exist);
            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_maxValue.name()))
                    .setValue(maxValue).sendKeys(Keys.TAB);

            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name())).setValue(str_numberfield_maxValue).sendKeys(Keys.TAB);
            String errorStr = "The value " + str_numberfield_maxValue + " is greater than maximum value " + int_numberfield_maxValue;

            $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text(errorStr)); //Verify error shown

            //Set back the allowable value again:
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(NumberFieldTest.NumberFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(numberField_defaultValueNumber).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_maxValue").shouldHave(value(numberField_defaultValueNumber));
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
                                     String numberField_defaultValueNumber
    ) throws ParseException {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";
        String helpInFillForm = blockStr + " .MuiFormHelperText-root";
        String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";
        String inputField = blockStr + " input";

        DecimalFormat df = getDecimalFormat(numberfield_decimalScale, checkbox_thousandSeparator);


        //Label
        if (StringUtils.isNotEmpty(numberfield_label)) {
            System.out.println("Verifying label: " + numberfield_label);
            if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
                $(labelInFillForm).shouldNotHave(text(numberfield_label)); //Verify that Label should not appear on the form - hide label
            } else {
                $(labelInFillForm).shouldHave(text(numberfield_label)); //Verify that Label appears on the form
            }
        }

        //Help
        if (StringUtils.isNotEmpty(numberfield_help)) {
            System.out.println("Verifying help: " + numberfield_help);
            $(helpInFillForm).shouldHave(text(numberfield_help));
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verifying required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        //  Default value
        if (StringUtils.isNotEmpty(numberField_defaultValueNumber)) {
            System.out.println("Verifying numberField_defaultValueNumber");
            $(inputField).shouldHave(value(df.format(new BigDecimal(numberField_defaultValueNumber))));
        }

        //Read Only checkbox
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            System.out.println("Verifying checkbox readOnly");
            System.out.println("Verifying for value: " + numberField_defaultValueNumber);
                $(inputField).shouldBe(disabled);
        }

//        //Apply format
//        if (StringUtils.isNotEmpty(checkbox_applyFormatter)) {
//            System.out.println("Verifying apply formatter");
//            $(inputField).shouldHave(value(numberField_defaultValueNumber));
//        }

        //Thousand Separator
        if (StringUtils.isNotEmpty(checkbox_thousandSeparator)) {
            System.out.println("Verifying checkbox thousandSeparator for value: " + numberField_defaultValueNumber);

            String strRandomInt = RandomStringUtils.randomNumeric(6);
            selectAndClear(inputField).setValue(strRandomInt).sendKeys(Keys.TAB); //Enter random value in Thosand Separator field

            $(inputField).shouldHave(value(df.format(new BigDecimal(strRandomInt)))); //Thousand separator should have numberField_defaultValueNumber
        }

        //Allow Negative
        if (StringUtils.isNotEmpty(checkbox_allowNegative)) {
            System.out.println("Verifying checkbox allowNegative");

            //Enter random negative number and verify that it works:
            String randomStr = RandomStringUtils.randomNumeric(4);
            selectAndClear(inputField).setValue(randomStr).sendKeys(Keys.TAB);
            $(inputField).shouldHave(value(randomStr));

            String randomStrNeg = "-"+randomStr; //Create random negative string
            selectAndClear(inputField).setValue(randomStrNeg).sendKeys(Keys.TAB);
            $(inputField).shouldHave(value(randomStrNeg));

        }

//        //Allow leading zeroes:
        if (StringUtils.isNotEmpty(checkbox_allowLeadingZeros)) {
            System.out.println("Verifying allow leading zeros");
            String randomStr = "000"+RandomStringUtils.randomNumeric(4);
            selectAndClear(inputField).setValue(randomStr).sendKeys(Keys.TAB);
            $(inputField).shouldHave(value(randomStr));
        }

        //Only Integer
        if (StringUtils.isNotEmpty(checkbox_onlyInteger)) {
            System.out.println("Verifying only integer");

            //Negative scenario:
            String str = RandomStringUtils.randomAlphabetic(4);
            $(inputField).setValue(str);
            $(inputField).shouldNotHave(value(str));
        }

        //    Decimal scale value verify, that decimal places are cutted by configured amount of decimal places
        if (StringUtils.isNotEmpty(numberfield_decimalScale)) {
            System.out.println("Verifying label: " + numberfield_decimalScale);
            //construct a number decimal value
            Random r = new Random();
            int integer = r.nextInt(100);
            double decimal = r.nextDouble();
            BigDecimal bd = new BigDecimal(integer + decimal);

            System.out.println("random bigdecmial: " + " .... " + bd.toString() + " .... " + df.format(bd));
            //try to set a big decimal value with full amount of decimal places
            selectAndClear(inputField).setValue(bd.toString()).pressTab();

            //it should have only specified amount of decimal places in gui accepted
            $(inputField).shouldHave(value(df.format(bd)));

        }


        //Min value
        if (StringUtils.isNotEmpty(numberfield_minValue)) {
            System.out.println("Verifying Min value");

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


    public enum NumberFieldOptionsIds {
        textfield_label,
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

}
