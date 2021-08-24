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
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;
import static java.lang.Double.parseDouble;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActionsFormCreation.*;
import static reusables.ReuseActionsFormCreation.readOnlyCheckboxOnFormDesign;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

@DisplayName("Currency Field Tests")
public class CurrencyFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Currency Field Test Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    private DecimalFormat getDecimalFormat(String textfield_decimalScale, String checkbox_thousandSeparator) {
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
        navigateToFormDesign("Currency Field");
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForCurrencyFields")
    @ParameterizedTest
    @CsvFileSource(resources = "/currency_field_test_data.csv", numLinesToSkip = 1)
    public void allCurrencyField(Integer row, Integer col, Integer colSpan,
                                 String label_text,
                                 String help_text,
                                 String currency_field,
                                 String checkbox_disableLabel,
                                 String checkbox_required,

                                 String textfield_decimalScale,
                                 String textfield_minValue,
                                 String textfield_maxValue,

                                 String checkbox_readOnly,
                                 String checkbox_applyFormatter,
                                 String checkbox_thousandSeparator,
                                 String checkbox_allowNegative,
                                 String checkbox_allowLeadingZeros,
                                 String checkbox_onlyInteger,
                                 String text_currencyField_defaultValueCurrency
    ) throws InterruptedException {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#li-template-CurrencyField-05").should(appear).click();
        $("#formelement_properties_card").should(appear);
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased

        DecimalFormat df = getDecimalFormat(textfield_decimalScale, checkbox_thousandSeparator);

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
            labelVerificationOnFormDesign(blockId,label_text);
        }
//
        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, label_text);
        }

        //Help
        if (StringUtils.isNotEmpty(help_text)) {
            helpVerificationOnFormDesign(blockId, help_text);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        //Currencies field
        if (StringUtils.isNotEmpty(currency_field)) {
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
            $("#sel_input_currencies").shouldBe(visible);
            $("#sel_control_currencies .selLabelChip").shouldHave(text("EUR"));
            $("#sel_control_currencies .selLabelChip").sendKeys(Keys.BACK_SPACE); //Clear the default value in Currencies field

            Arrays.asList(currency_field.split(",")).forEach(currency -> {
                $("#sel_input_currencies").should(exist).click();
                $(".MuiAutocomplete-popper").should(appear);
                $$(".MuiAutocomplete-popper li").findBy(text(currency)).should(exist).click();
                $("#sel_control_currencies").shouldHave(text(currency));
            });
        }


        //Read only checkbox
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            readOnlyCheckboxOnFormDesign(blockId);

            //  if (StringUtils.isEmpty(text_currencyField_defaultValueCurrency)) {
            //When you don't have any value in Default value edit box and click on Read only checkbox it should show error
            $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text("Must be set, if read only"));

            //Uncheck the readonly checkbox
            String checkBoxId = "#" + NumberFieldTest.NumberFieldOptionsIds.checkbox_readOnly.name();
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $(checkBoxId + " input").shouldNotBe(selected); //Uncheck the Read only checkbox

            //Set the value in the Default value:
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue("1").sendKeys(Keys.TAB);
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name())).shouldHave(value("1"));

            $(checkBoxId).shouldBe(visible).click();
            $(checkBoxId + " input").shouldBe(selected);

        }


        //Apply user format checkbox check
        if (StringUtils.isNotEmpty(checkbox_applyFormatter)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CurrencyFieldTest.CurrencyFieldOptionsIds.checkbox_applyFormatter.name();
            $(checkBoxId).shouldBe(visible).click();

            //Set the number in Default value:
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(text_currencyField_defaultValueCurrency).sendKeys(Keys.TAB); //Enter value
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name())).shouldHave(value(text_currencyField_defaultValueCurrency));

            //Now apply format and verify
            String applyFormatStr = df.format(new BigDecimal(text_currencyField_defaultValueCurrency));

            //Set the number in Default value:
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(applyFormatStr).sendKeys(Keys.TAB); //Enter value

            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name())).shouldHave(value(applyFormatStr));
        }

        //  Thousand Separator checkbox check
        if (StringUtils.isNotEmpty(checkbox_thousandSeparator)) {
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(text_currencyField_defaultValueCurrency).sendKeys(Keys.TAB); //Enter value in Default chekbox

            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CurrencyFieldTest.CurrencyFieldOptionsIds.checkbox_thousandSeparator.name();
            $(checkBoxId).shouldBe(visible).click(); //TBD - Thousand separator logic
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            //Verify the changed format:
            if (StringUtils.isNotEmpty(text_currencyField_defaultValueCurrency)) {
                $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name()))
                        .shouldHave(value(df.format(new BigDecimal(text_currencyField_defaultValueCurrency))));
            }
        }

        //Allow Negative checkbox
        if (StringUtils.isNotEmpty(checkbox_allowNegative)) {
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CurrencyFieldTest.CurrencyFieldOptionsIds.checkbox_allowNegative.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }


        // Allow leading zeros
        if (StringUtils.isNotEmpty(checkbox_allowLeadingZeros)) {
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CurrencyFieldTest.CurrencyFieldOptionsIds.checkbox_allowLeadingZeros.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Only integer
        if (StringUtils.isNotEmpty(checkbox_onlyInteger)) {
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CurrencyFieldTest.CurrencyFieldOptionsIds.checkbox_onlyInteger.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            //here also check that after this is checked the Decimal places textfield should be disabled
            $("#numberField_decimalScale").shouldBe(disabled); //Decimal places
            $(checkBoxId).shouldBe(visible).click(); //Click Only integer checkbox again
            $(checkBoxId).shouldNotBe(checked);
            $(blockId).shouldHave(text(label_text));
        }

        //Enter Decimal Places
        if (StringUtils.isNotEmpty(textfield_decimalScale)) {
            // $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_decimalScale.name()))
                    .setValue(textfield_decimalScale).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_decimalScale").shouldHave(value(textfield_decimalScale));
        }

        //Enter Default value
        if (StringUtils.isNotEmpty(text_currencyField_defaultValueCurrency)) {
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.textfield_label.name())).shouldHave(text(label_text));
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name())).should(exist);
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(text_currencyField_defaultValueCurrency).sendKeys(Keys.TAB);
            if (StringUtils.isNotEmpty(textfield_decimalScale)) {
                $("#numberField_defaultValueNumber").shouldHave(value("1234.56"));
                selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_decimalScale.name())).sendKeys(Keys.TAB); //Clear the decimal scale field
                selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name()))
                        .setValue(text_currencyField_defaultValueCurrency).sendKeys(Keys.TAB); //Again set text_currencyField_defaultValueCurrency
            } else {
                $("#numberField_defaultValueNumber")
                        .shouldHave(value(df.format(new BigDecimal(text_currencyField_defaultValueCurrency))));
            }
        }

        //Enter Minimum Value
        if (StringUtils.isNotEmpty(textfield_minValue)) {
            //Error verification:
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.textfield_label.name()))
                    .shouldHave(text(label_text));
            int int_numberfield_minValue = Integer.parseInt(textfield_minValue);
            int int_numberfield_lessThanminValue = int_numberfield_minValue - 1;
            String str_numberfield_lessThanMinValue = Integer.toString(int_numberfield_lessThanminValue);

            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_minValue.name())).should(exist);
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_minValue.name()))
                    .setValue(textfield_minValue).sendKeys(Keys.TAB);
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_minValue.name())).shouldHave(value(textfield_minValue));

            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name())).setValue(str_numberfield_lessThanMinValue).sendKeys(Keys.TAB);
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name())).shouldHave(value(str_numberfield_lessThanMinValue));

            String errorStr = "The value " + str_numberfield_lessThanMinValue + " is smaller than minimum value " + int_numberfield_minValue;
            $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text(errorStr)); //Verify error shown

            //Set the allowable value again:
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(text_currencyField_defaultValueCurrency).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_minValue").shouldHave(value(text_currencyField_defaultValueCurrency));
        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(textfield_maxValue)) {
            //Error verification:
            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.textfield_label.name()))
                    .shouldHave(text(label_text));
            int int_numberfield_maxValue = Integer.parseInt(textfield_maxValue);
            int int_numberfield_moreThanMaxValue = int_numberfield_maxValue + 1;
            String str_numberfield_maxValue = Integer.toString(int_numberfield_moreThanMaxValue);

            $(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_maxValue.name())).should(exist);
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_maxValue.name()))
                    .setValue(textfield_maxValue).sendKeys(Keys.TAB);

            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name())).setValue(str_numberfield_maxValue).sendKeys(Keys.TAB);
            String errorStr = "The value " + str_numberfield_maxValue + " is greater than maximum value " + int_numberfield_maxValue;

            $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text(errorStr)); //Verify error shown

            //Set back the allowable value again:
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CurrencyFieldTest.CurrencyFieldOptionsIds.numberField_defaultValueNumber.name()))
                    .setValue(text_currencyField_defaultValueCurrency).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_maxValue").shouldHave(value(text_currencyField_defaultValueCurrency));
        }

    }

    @Test
    @Order(3)
    @DisplayName("publish and open FormPage")
    public void publishAndOpenFormPage() {
        //Click on publish button, wait until form dashboard opens and click on fill form
        $("#btnFormDesignPublish").should(exist).click();

        $("#form-publish-dialog .MuiPaper-root").should(appear); //Publish confirmation dialog appears
        $("#form-publish-dialog #btnConfirm").should(exist).click(); //Click on Confirm button
        $("#btnCreateNewData").waitUntil(exist, 50000).click(); //Fill form button on Launch screen
        $("#dataContainer").should(appear); //Verify that the form details screen appears

    }

    @Order(4)
    @DisplayName("verify fill form for Currency fields")
    @ParameterizedTest
    @CsvFileSource(resources = "/currency_field_test_data.csv", numLinesToSkip = 1)
    public void currencyFillFormField(Integer row, Integer col, Integer colSpan,
                                      String label_text,
                                      String currency_help,
                                      String currency_field,
                                      String checkbox_disableLabel,
                                      String checkbox_required,

                                      String textfield_decimalScale,
                                      String textfield_minValue,
                                      String textfield_maxValue,

                                      String checkbox_readOnly,
                                      String checkbox_applyFormatter,
                                      String checkbox_thousandSeparator,
                                      String checkbox_allowNegative,
                                      String checkbox_allowLeadingZeros,
                                      String checkbox_onlyInteger,
                                      String text_currencyField_defaultValueCurrency
    ) throws InterruptedException {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";
        String helpInFillForm = blockStr + " .MuiFormHelperText-root";
        String requiredFieldInFillForm = blockStr + " .MuiFormLabel-asterisk";
        String inputField = blockStr + " input";

        DecimalFormat df = getDecimalFormat(textfield_decimalScale, checkbox_thousandSeparator);


        //  Label
        if (StringUtils.isNotEmpty(label_text)) {
            System.out.println("Verifying label: " + label_text);
            if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
                $(labelInFillForm).shouldNotHave(text(label_text)); //Verify that Label should not appear on the form - hide label
            } else {
                $(labelInFillForm).shouldHave(text(label_text)); //Verify that Label appears on the form
            }
        }

        //Help
        if (StringUtils.isNotEmpty(currency_help)) {
            System.out.println("Verifying help: " + currency_help);
            $(helpInFillForm).shouldHave(text(currency_help));
        }

        //  required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verifying required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        //  Default value
        if (StringUtils.isNotEmpty(text_currencyField_defaultValueCurrency)) {
            System.out.println("Verifying text_currencyField_defaultValueCurrency");
            $(inputField).shouldHave(value(df.format(new BigDecimal(text_currencyField_defaultValueCurrency))));
        }


        //Currency field
        if (StringUtils.isNotEmpty(currency_field)) {
            System.out.println("Verifying currency label: " + label_text);
            //If multiple values given by comma separation, split and check for individual values:
            String str_currency_field = currency_field.toString();
            String currencies_block = blockStr + " .MuiTextField-root";
            if (str_currency_field.contains(",")) {
                List<String> currencyStr = Arrays.asList(str_currency_field.split(","));
                for (int i = 0; i < currencyStr.size(); i++) {
                    $(currencies_block).shouldHave(text(currencyStr.get(0))); //Verify that all the comma separated values are present
                }
            } else {
                $(currencies_block).shouldHave(value(currency_field));
            }
        }

        //Read Only checkbox
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            System.out.println("Verifying checkbox readOnly");
            System.out.println("Verifying for value: " + text_currencyField_defaultValueCurrency);
            $(inputField).shouldBe(disabled);
        }

//        //Apply user format
//        if (StringUtils.isNotEmpty(checkbox_applyFormatter)) {
//            System.out.println("Verifying apply formatter");
//            System.out.println("Value text_currencyField_defaultValueCurrency: " + text_currencyField_defaultValueCurrency);
//            $(inputField).shouldHave(value(text_currencyField_defaultValueCurrency));
//        }

        //Thousand Separator
        if (StringUtils.isNotEmpty(checkbox_thousandSeparator)) {
            System.out.println("Verifying label: " + label_text);
            System.out.println("Verifying checkbox thousandSeparator for value: " + text_currencyField_defaultValueCurrency);
            $(inputField).shouldHave(value(df.format(new BigDecimal(text_currencyField_defaultValueCurrency))));

            String strRandomInt = RandomStringUtils.randomNumeric(6);
            selectAndClear(inputField).setValue(strRandomInt).sendKeys(Keys.TAB); //Enter random value in Thosand Separator field

            $(inputField).shouldHave(value(df.format(new BigDecimal(strRandomInt)))); //Thousand separator should have text_currencyField_defaultValueCurrency
        }


        //Allow Negative
        if (StringUtils.isNotEmpty(checkbox_allowNegative)) {
            System.out.println("Verifying checkbox allowNegative");

            //Enter random negative number and verify that it works:
            String randomStr = RandomStringUtils.randomNumeric(4);
            selectAndClear(inputField).setValue(randomStr).sendKeys(Keys.TAB);
            $(inputField).shouldHave(value(randomStr));

            String randomStrNeg = "-" + randomStr; //Create random negative string
            selectAndClear(inputField).setValue(randomStrNeg).sendKeys(Keys.TAB);
            $(inputField).shouldHave(value(randomStrNeg));

        }
//
        //Allow leading zeroes:
        if (StringUtils.isNotEmpty(checkbox_allowLeadingZeros)) {
            System.out.println("Verifying allow leading zeros");
            String randomStr = "000"+RandomStringUtils.randomNumeric(4);
            selectAndClear(inputField).setValue(randomStr).sendKeys(Keys.TAB);
            $(inputField).shouldHave(value(randomStr));
        }

        //Only Integer
        if (StringUtils.isNotEmpty(checkbox_onlyInteger)) {
            System.out.println("Verifying label: " + label_text);
            System.out.println("Verifying only integer: " + text_currencyField_defaultValueCurrency);

            //Negative scenario:
            String str = RandomStringUtils.randomAlphabetic(4);
            $(inputField).setValue(str);
            $(inputField).shouldNotHave(value(str));

        }


        //    Decimal scale value verify, that decimal places are cutted by configured amount of decimal places
        if (StringUtils.isNotEmpty(textfield_decimalScale)) {
            System.out.println("Verifying label: " + label_text);
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
        if (StringUtils.isNotEmpty(textfield_minValue)) {
            System.out.println("Verifying Min value for value: " + textfield_minValue);

            //Negative scenario:
            //Error verification:
            int int_numberfield_minValue = Integer.parseInt(textfield_minValue);
            int int_text_currencyField_defaultValueCurrency = Integer.parseInt(text_currencyField_defaultValueCurrency);
            String str_text_currencyField_defaultValueCurrency = Integer.toString(int_text_currencyField_defaultValueCurrency);

            if (int_numberfield_minValue > int_text_currencyField_defaultValueCurrency) {
                selectAndClear(inputField).setValue(str_text_currencyField_defaultValueCurrency).sendKeys(Keys.TAB);
                $(inputField).shouldHave(value(str_text_currencyField_defaultValueCurrency));
                String errorStr = "The value must be greater than " + textfield_minValue;

                $(helpInFillForm).should(exist).shouldHave(text(errorStr)); //Verify error shown
            }
        }

        //Max value
        if (StringUtils.isNotEmpty(textfield_maxValue)) {
            System.out.println("Verifying Max value: " + textfield_maxValue);

            //Negative scenario:
            //Error verification:
            int int_numberfield_maxValue = Integer.parseInt(textfield_maxValue);
            int int_text_currencyField_defaultValueCurrency = Integer.parseInt(text_currencyField_defaultValueCurrency);
            String str_text_currencyField_defaultValueCurrency = Integer.toString(int_text_currencyField_defaultValueCurrency);

            if (int_numberfield_maxValue < int_text_currencyField_defaultValueCurrency) {
                selectAndClear(inputField).setValue(str_text_currencyField_defaultValueCurrency).sendKeys(Keys.TAB);
                $(inputField).shouldHave(value(str_text_currencyField_defaultValueCurrency));

                String errorStr = "The value must be less than " + textfield_maxValue;

                $(helpInFillForm).should(exist).shouldHave(text(errorStr)); //Verify error shown

            }

        }

    }


    public enum CurrencyFieldOptionsIds {
        textfield_label,
        numberField_defaultValueNumber,
        numberField_decimalScale,
        numberField_minValue,
        numberField_maxValue,

        checkbox_applyFormatter,
        checkbox_thousandSeparator,
        checkbox_allowNegative,
        checkbox_allowLeadingZeros,
        checkbox_onlyInteger;

    }

    public static void main(String[] args) {
        Random r = new Random();
        int integer = r.nextInt(100);
        double decimal = r.nextDouble();
        BigDecimal bd = new BigDecimal(integer + decimal);
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.UK);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
        df.setParseBigDecimal(true);
        System.out.println("random bigdecmial: " + " .... " + bd.toString() + " .... " + df.format(bd));

    }

}
