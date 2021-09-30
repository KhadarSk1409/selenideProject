package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;

import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.TAB;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Email Field Tests")
public class EmailFieldTest extends BaseTest {
    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Emailfield Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());


    @Test
    @DisplayName("precondition")
    @Order(1)
    public void precondition() {
        navigateToFormDesign(FormField.EMAIL);
    }

    @DisplayName("createNewFormulaDesignForEmailfields")
    @Order(2)
    @ParameterizedTest
    @CsvFileSource(resources = "/email_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String labelText,
                             String checkbox_disableLabel,
                             String help_text,
                             String invalid_email,
                             String checkbox_required,
                             String textfield_defaultValue,
                             String checkbox_readonly,
                             String checkbox_allow_multiple) {
        String blockId = String.format("#block-loc_en-GB-r_%d-c_%d", row, col);

        // create new block, if it does not exist
        if (!$(blockId).exists()) {
            String prevBlockId = String.format("#block-loc_en-GB-r_%d-c_%d", row - 1, col);
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNum = $("#formMinorversion").should(exist).getText();
        $(blockId).shouldBe(visible).click();
        $("#li-template-EmailField-05").should(appear).click();
        $("#formelement_properties_card").should(appear);
        $("#formMinorversion").shouldNotHave(text(initialVerNum));

        if (colSpan != null && colSpan > 1) {
            int prevWidth = $(blockId).getRect().getWidth();
            for (int i = 1; i < colSpan; ++i) {
                String initialVerNum1 = $("#formMinorversion").should(exist).getText();
                $("#blockButtonExpand").shouldBe(visible).click();
                $("#blockButtonExpand").shouldNotHave(text(initialVerNum1));
            }
            int width = $(blockId).getRect().getWidth();
            Assertions.assertEquals(colSpan, width / prevWidth, "block column span should be: " + colSpan);
        }

        // Label
        if (StringUtils.isNotEmpty(labelText)) {
            labelVerificationOnFormDesign(blockId, labelText);
        }

        // Help
        if (StringUtils.isNotEmpty(help_text)) {
            helpVerificationOnFormDesign(blockId, help_text);
        }

        // Hide
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, checkbox_disableLabel);
        }

        // required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        // default value
        if (StringUtils.isNotEmpty(textfield_defaultValue)) {
            $(blockId + " .fa-pen").closest("button").shouldBe(visible).click(); // Edit
            String initialVerNum1 = $("#formMinorversion").should(exist).getText(); // initial version
            $(By.id(EmailFielsIds.textfield_defaultValueEmail.name())).setValue(textfield_defaultValue).sendKeys(TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNum1)); // verify version changed
            $(By.id(EmailFielsIds.textfield_defaultValueEmail.name())).shouldHave(value(textfield_defaultValue));

            // verify error for forbidden default value
            if (StringUtils.isNotEmpty(invalid_email)) {
                String invalidEmailMsg = "Invalid email address: " + textfield_defaultValue;
                $("#textfield_defaultValueEmail-helper-text").shouldHave(text(invalidEmailMsg));
                selectAndClear(By.id(EmailFielsIds.textfield_defaultValueEmail.name()));
            }
        }

        // readonly
        if (StringUtils.isNotEmpty(checkbox_readonly)) {
            $(blockId + " .fa-pen").closest("button").should(exist).click();
            String initialVerNum1 = $("#formMinorversion").should(exist).getText();
            String checkBoxId = "#" + EmailFielsIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNum1));
            $(checkBoxId + " input").shouldBe(selected);

            if (StringUtils.isEmpty(textfield_defaultValue)) {
                // error when default value is empty
                $("#textfield_defaultValueEmail-helper-text").should(exist).shouldHave(text("Must be set, if read only"));
                $(checkBoxId).shouldBe(visible).click(); // make not readonly because form cannot be published otherwise
            }
        }

        // allow multiple
        if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {
            String initialVerNum1 = $("#formMinorversion").should(exist).getText(); // initial version
            String checkBoxId = "#" + EmailFielsIds.checkbox_multiple.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNum1)); // verify version changed
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

        if (StringUtils.isNotEmpty(invalid_email)) {

            // invalid email
            $(inputField).setValue(textfield_defaultValue).sendKeys(TAB);

            // verify error
            $(helpInFillForm).should(matchText("^Invalid email add?ress: " + Pattern.quote(textfield_defaultValue) + "$")); // work around for both wrong and correct spelling of "adress"
//            $(helpInFillForm).shouldHave(text("Invalid email address: " + textfield_defaultValue)); // for correct spelling of address

        } else if (StringUtils.isNotEmpty(textfield_defaultValue)) {

            // Default Value
            System.out.printf("Verify email default value: %s%n", textfield_defaultValue);
            $(inputField).shouldHave(value(textfield_defaultValue));

            // Readonly
            if (StringUtils.isNotEmpty(checkbox_readonly)) {
                System.out.println("Verify readonly");
                $(inputField).shouldBe(disabled);
            } else {
                System.out.println("Verify not readonly");
                $(inputField).shouldBe(enabled).setValue("").sendKeys(TAB); // clear and default value should refill
                $(inputField).shouldHave(value(textfield_defaultValue));
            }
        } // other case will never happen since you cannot publish a form with forbidden default value

        if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {

            // Allow Multiple
            System.out.println("Verify allow multiple");
            $(inputField).setValue("e1@em.co, e2@em.co, e3@em.co").sendKeys(TAB);

            // should not throw error, since allow multiple is enabled
            $(helpInFillForm).shouldNot(matchText("^Invalid email add?ress\\. Only single Email is allowed\\.$")); // fix typo
//            $(helpInFillForm).shouldNotHave(text("Invalid email address. Only single Email is allowed")); // correct spelling
            selectAndClear(inputField);

        } else if (StringUtils.isEmpty(checkbox_readonly)) {

            // Don't allow multiple
            System.out.println("Verify do not allow multiple");
            $(inputField).setValue("e1@em.co, e2@em.co, e3@em.co").sendKeys(TAB);

            // verify error, since allow multiple is disabled
            $(helpInFillForm).should(matchText("^Invalid email add?ress\\. Only single Email is allowed\\.$"));
//            $(helpInFillForm).shouldHave(text("Invalid email address. Only single Email is allowed.")); // correct spelling
            selectAndClear(inputField);
        }
    }

    // Typo: Fiels (used in other files)
    public enum EmailFielsIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        textfield_defaultValueEmail,
        checkbox_readOnly,
        checkbox_multiple
    }
}
