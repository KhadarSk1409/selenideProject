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
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsFormCreation.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Formatted Tests")
public class FormattedTextTest extends BaseTest {

    public enum FormattedTextIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        richTextField_areaValueHtml,
        checkbox_readOnly;
    }

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() throws IOException {
        navigateToFormDesign(FormField.FORMATTED_TEXT);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForFormattedtextField")
    @ParameterizedTest
    @CsvFileSource(resources = "/formatted_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String text_label,
                             String checkbox_disableLabel,
                             String text_help,
                             String checkbox_required,
                             String checkbox_readonly,
                             String edit_values,
                             String fraction_edit_value


    ) {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $(elementLocators("TemplateList")).find(byText("Show More")).should(exist).click(); //Click on Show More
        $(elementLocators("FormattedTextField")).should(exist).click();
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


        //Help
        if (StringUtils.isNotEmpty(text_help)) {
            helpVerificationOnFormDesign(blockId, text_help);
        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            hideLabelVerificationOnFormDesign(blockId, text_label);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            requiredCheckboxVerificationOnFormDesign(blockId);
        }

        //Read only checkbox
        if (StringUtils.isNotEmpty(checkbox_readonly)) {
            String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown
            String checkBoxId = "#" + FormattedTextTest.FormattedTextIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            if (StringUtils.isEmpty(edit_values)) {
                //When you don't have any value in Default value edit box and click on Read only checkbox it should show error
                $(elementLocators("FormattedTextErrorHelperText")).should(exist).shouldHave(text("Must be set, if read only"));
            }
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown

        }

        //Value edit
        if (StringUtils.isNotEmpty(edit_values)) {
            String checkBoxId = "#richTextField_areaValueHtml .fa-pen";
            $(elementLocators("AdvancedSection")).should(exist).click(); //Advanced section dropdown
            $(checkBoxId).shouldBe(visible).click();
            $(elementLocators("TextEditor")).should(appear); //Text Editor should appear

            $(elementLocators("TextInputField")).should(exist).setValue(edit_values); //Set the value
            $(elementLocators("CloseTextEditorBtn")).should(exist).click(); //Close button

            //Verify that Text Area Value has text fraction
            if (StringUtils.isNotEmpty(fraction_edit_value)) {
                $(By.id(FormattedTextTest.FormattedTextIds.richTextField_areaValueHtml.name())).shouldHave(text(fraction_edit_value));

            }

            $(blockId + " .fr-element").setValue(edit_values).pressTab();
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
    @DisplayName("verify fill form for formattedtext field")
    @ParameterizedTest
    @CsvFileSource(resources = "/formatted_field_test_data.csv", numLinesToSkip = 1)
    public void formattedtextFillFormField(Integer row, Integer col, Integer colSpan,
                                           String text_label,
                                           String checkbox_disableLabel,
                                           String text_help,
                                           String checkbox_required,
                                           String checkbox_readonly,
                                           String edit_values,
                                           String fraction_edit_value) {
        String blockId = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockId + " .MuiFormLabel-root";
        String helpInFillForm = blockId + " .MuiFormHelperText-root";
        String requiredInFillForm = blockId + " .MuiFormLabel-asterisk";
        String richtextInFillForm = blockId + " [data-custom-type=richtext-output]";
        String inputField = blockId + " .fr-element";


        // Default
        if (StringUtils.isNotEmpty(edit_values)) {
            System.out.printf("Require Default: %s%n", edit_values);
            $(richtextInFillForm).shouldHave(text(edit_values));
        }


        // Label
        if (StringUtils.isNotEmpty(text_label)) {

            if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
                System.out.println("Verify label is hidden");
                $(labelInFillForm).should(exist).shouldNotHave(text(text_label));

            } else {

                System.out.printf("Verify label: %s%n", text_label);
                $(labelInFillForm).shouldHave(text(text_label));
            }
        }

        // Help
        if (StringUtils.isNotEmpty(text_help)) {
            System.out.printf("Verify help: %s%n", text_help);
            $(helpInFillForm).shouldHave(text(text_help));
        }


        // Required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            System.out.println("Verify required: *");
            $(requiredInFillForm).should(exist).shouldHave(text("*"));
        }

        $(richtextInFillForm).shouldBe(visible).click(); // Try open text editor

        // Readonly
        if (StringUtils.isNotEmpty(checkbox_readonly)) {
            System.out.println("Verify readonly");

            $(inputField).shouldNot(exist); // text editor does not appear

        } else {

            System.out.println("Verify not readonly");

            $(inputField).should(exist); // text editor appears
        }
    }
}
