package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.TAB;
import static reusables.ReuseActionsFormCreation.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Email Field Tests")
public class EmailFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Emailfield Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    public enum EmailFielsIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        textfield_defaultValueEmail,
        checkbox_readOnly,
        checkbox_multiple;
    }

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() {
        navigateToFormDesign(FormField.EMAIL);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForEmailfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/email_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String label_text,
                             String checkbox_disableLabel,
                             String help_text,
                             String invalid_email,
                             String checkbox_required,
                             String textfield_defaultValue,
                             String checkbox_readOnly,
                             String checkbox_allow_multiple

    ) {
        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#li-template-EmailField-05").should(appear).click();
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
        if (StringUtils.isNotEmpty(label_text)) {
            labelVerificationOnFormDesign(blockId,label_text);
        }

        //Help
        if (StringUtils.isNotEmpty(help_text)) {
            helpVerificationOnFormDesign(blockId, help_text);
        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, label_text);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        //Default Value
        if (StringUtils.isNotEmpty(textfield_defaultValue)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(EmailFieldTest.EmailFielsIds.textfield_defaultValueEmail.name()))
                    .setValue(textfield_defaultValue).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(By.id(EmailFieldTest.EmailFielsIds.textfield_defaultValueEmail.name())).shouldHave(value(textfield_defaultValue));

            //Verify the error shown for invalid textfield_defaultValue:
            if (StringUtils.isNotEmpty(invalid_email)) {
                String invalidEmail = "Invalid email address: " + textfield_defaultValue;
                $("#textfield_defaultValueEmail-helper-text").shouldHave(text(invalidEmail)); //Verify the error
                selectAndClear(By.id(EmailFieldTest.EmailFielsIds.textfield_defaultValueEmail.name()))
                        .setValue("").sendKeys(TAB); //Error has to be fixed, so form can be published
            }
        }

        //Read only checkbox
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + EmailFieldTest.EmailFielsIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            if (StringUtils.isEmpty(textfield_defaultValue)) {
                //When you don't have any value in Default value edit box and click on Read only checkbox it should show error (??)
                $("#textfield_defaultValueEmail-helper-text").should(exist).shouldHave(text("Must be set, if read only")); //-> Defect raised for this part
                $(checkBoxId).shouldBe(visible).click(); // make not readonly because form cannot be published otherwise
            }
        }

        //Allow Multiple:
        if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + EmailFieldTest.EmailFielsIds.checkbox_multiple.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }
    }

    @Test
    @DisplayName("publish and open form page")
    @Order(3)
    public void publishAndOpenFormPage() {
        $("#btnFormDesignPublish").should(exist).click();
        $("#form-publish-dialog .MuiPaper-root").should(appear);
        $("#form-publish-dialog #btnConfirm").should(exist).click();
        $("#btnCreateNewData").waitUntil(exist, 50000).click(); // wait for form to publish
        $("#dataContainer").should(appear);
    }

    @Order(4)
    @DisplayName("verify fill form for emailfield")
    @ParameterizedTest
    @CsvFileSource(resources = "/email_field_test_data.csv", numLinesToSkip = 1)
    public void emailFillFormField(Integer row, Integer col, Integer colSpan,
                                   String labelText,
                                   String checkbox_disableLabel,
                                   String help_text,
                                   String invalid_email,
                                   String checkbox_required,
                                   String textfield_defaultValue,
                                   String checkbox_readonly,
                                   String checkbox_allow_multiple) {
        String blockId = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockId + " .MuiFormLabel-root";
        String helpInFillForm = blockId + " .MuiFormHelperText-root";
        String requiredFieldInFillForm = blockId + " .MuiFormLabel-asterisk";
        String inputField = blockId + " input";

        // Label
        if (StringUtils.isNotEmpty(labelText)) {
            System.out.printf("Verify label: %s%n", labelText);
            if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
                $(labelInFillForm).should(exist).shouldNotHave(text(labelText));
            } else {
                $(labelInFillForm).should(exist).shouldHave(text(labelText));
            }
        }

        // Help Text
        if (StringUtils.isNotEmpty(help_text)) {
            System.out.printf("Verify help: %s%n", help_text);
            $(helpInFillForm).shouldHave(text(help_text));
        }

        // Required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verify required: *");
            $(requiredFieldInFillForm).shouldHave(text("*"));
        }

        if (StringUtils.isNotEmpty(textfield_defaultValue)) {
            if (StringUtils.isNotEmpty(invalid_email)) {

                // invalid email
                $(inputField).setValue(textfield_defaultValue).sendKeys(TAB);

                // verify error
                $(helpInFillForm).shouldHave(text("Invalid email adress: " + textfield_defaultValue));

            } else {

                // Default Value
                System.out.printf("Verify email default value: %s%n", textfield_defaultValue);
                $(inputField).shouldHave(value(textfield_defaultValue));

                // Readonly
                if (StringUtils.isNotEmpty(checkbox_readonly)) {
                    System.out.println("Verify readonly");
                    $(inputField).shouldBe(disabled);
                } else {
                    System.out.println("Verify not readonly");
                    selectAndClear(inputField); // clear and default value should refill
                    $(inputField).shouldHave(value(textfield_defaultValue));
                }
            }
        }

        if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {

            // Allow Multiple
            System.out.println("Verify allow multiple");
            $(inputField).setValue("e1@em.co, e2@em.co, e3@em.co").sendKeys(TAB);

            // should not throw error, since allow multiple is enabled
            $(helpInFillForm).shouldNotHave(text("Invalid email adress. Only single Email is allowed.")); // fix typo

        } else if (StringUtils.isEmpty(checkbox_readonly)) { // if allow multiple is false and readonly verify error for putting in multiple values

            // Don't allow multiple
            System.out.println("Verify do not allow multiple");
            $(inputField).setValue("e1@em.co, e2@em.co, e3@em.co").sendKeys(TAB);

            // verify error, since allow multiple is disabled
            $(helpInFillForm).shouldHave(text("Invalid email adress. Only single Email is allowed."));
            selectAndClear(inputField);
        }
    }
}
