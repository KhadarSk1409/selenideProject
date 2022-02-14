package com.vo.formdesign;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.TAB;
import static reusables.ReuseActions.elementLocators;
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
    public void precondition() throws IOException {
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
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        //Click on Show More
        $(elementLocators("TemplateList")).find(byText("Show More")).should(exist).click();
        $(elementLocators("EmailField")).should(exist).click();
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
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(EmailFieldTest.EmailFielsIds.textfield_defaultValueEmail.name()))
                    .setValue(textfield_defaultValue).sendKeys(Keys.TAB);
            //TODO check appearance on designer
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(By.id(EmailFieldTest.EmailFielsIds.textfield_defaultValueEmail.name())).shouldHave(value(textfield_defaultValue));

            //Verify the error shown for invalid email address:
            if (StringUtils.isNotEmpty(invalid_email)) {
                String invalidEmail = "Invalid email address: " + textfield_defaultValue;
                $("#textfield_defaultValueEmail-helper-text").shouldHave(text(invalidEmail)); //Verify the error

            }
        }

        //Read only checkbox
        if (StringUtils.isNotEmpty(checkbox_readOnly)) {
            $(blockId).$(elementLocators("PenIcon")).closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + EmailFieldTest.EmailFielsIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $(checkBoxId + " input").shouldBe(selected);
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased

            if (StringUtils.isNotEmpty(textfield_defaultValue)) {
                selectAndClear(By.id(EmailFieldTest.EmailFielsIds.textfield_defaultValueEmail.name())).sendKeys(Keys.TAB);
                //When you don't have any value in Default value edit box and click on Read only checkbox it should show error (??)
                $("#textfield_defaultValueEmail-helper-text").should(exist).shouldHave(text("Must be set, if read only")); //-> Defect raised for this part
                selectAndClear(By.id(EmailFieldTest.EmailFielsIds.textfield_defaultValueEmail.name()))
                        .setValue(textfield_defaultValue).sendKeys(Keys.TAB);
            }
        }

        //Allow Multiple:
        if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown
            String checkBoxId = "#" + EmailFieldTest.EmailFielsIds.checkbox_multiple.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }
    }

    @Test
    @DisplayName("publish and open form page")
    @Order(3)
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

        // Default
        if (StringUtils.isNotEmpty(textfield_defaultValue)) {
            $(inputField).shouldHave(value(textfield_defaultValue));
        }

        // Readonly
        if (StringUtils.isNotEmpty(checkbox_readonly)) {
            System.out.println("Verify readonly");
            $(inputField).shouldBe(disabled);
        } else {
            System.out.println("Verify not readonly");
            $(inputField).shouldBe(enabled);

            // Allow Multiple only tested when readonly = False
            if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {

                // Allow Multiple
                System.out.println("Verify allow multiple");
                $(inputField).setValue("e1@em.co, e2@em.co, e3@em.co").sendKeys(TAB);

                // should not throw error, since allow multiple is enabled
                $(helpInFillForm).shouldNotHave(text("Invalid email address. Only single Email is allowed."));

            } else {

                // Don't allow multiple
                System.out.println("Verify do not allow multiple");
                $(inputField).setValue("e1@em.co, e2@em.co, e3@em.co").sendKeys(TAB);

                // verify error, since allow multiple is disabled
                $(helpInFillForm).shouldHave(text("Invalid email address. Only single Email is allowed."));
                $(inputField).sendKeys(Keys.CONTROL, Keys.COMMAND, "a", Keys.DELETE); //selectAndClear method is not working fine here so performed KeyBoard Actions
            }
        }

        if (StringUtils.isNotEmpty(invalid_email)) {
            // invalid email
            $(inputField).setValue(textfield_defaultValue).sendKeys(TAB);
            // verify error
            $(helpInFillForm).shouldHave(text("Invalid email address: " + textfield_defaultValue));
        }
    }
}