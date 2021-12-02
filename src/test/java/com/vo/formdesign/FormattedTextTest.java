package com.vo.formdesign;

import com.vo.BaseTest;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.commands.PressEnter;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static reusables.ReuseActions.createNewForm;
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
    @DisplayName("createNewFormulaDesignForCheckBoxGroupfields")
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
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#li-template-RichTextEditor-06").should(exist).click();
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
        if (StringUtils.isNotEmpty(text_label)) {
            labelVerificationOnFormDesign(blockId,text_label);
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
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + FormattedTextTest.FormattedTextIds.checkbox_readOnly.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            if (StringUtils.isEmpty(edit_values)) {
                //When you don't have any value in Default value edit box and click on Read only checkbox it should show error
                $("#richTextField_areaValueHtml .Mui-error").should(exist).shouldHave(text("Must be set, if read only"));
            }
        }

        //Value edit
        if (StringUtils.isNotEmpty(edit_values)) {
            String checkBoxId = "#richTextField_areaValueHtml .fa-pen";
            $(checkBoxId).shouldBe(visible).click();
            $("#rich_text_editor_wrapper").should(appear); //Text Editor should appear

            $("#rich_text_editor_wrapper .fr-element").should(exist).setValue(edit_values); //Set the value
            $("#appBar #btnClosePropertiesForm").should(exist).click(); //Close button

            //Verify that Text Area Value has text fraction
            if (StringUtils.isNotEmpty(fraction_edit_value)) {
                $(By.id(FormattedTextTest.FormattedTextIds.richTextField_areaValueHtml.name())).shouldHave(text(fraction_edit_value));

            }

        }

    }
}
