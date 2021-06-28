package com.vo.formdesign;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Select Tests")
public class SelectTest extends BaseTest {

    enum SelectIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        property_select_direction,
        numberField_minCount,
        numberField_maxCount,
        checkbox_multiple;
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
        $("#li-template-SelectField-04").should(appear).click();
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(appear);

        $("#panel2a-header").should(exist).click(); //Advanced section dropdown

        //options for text field should exist:
        Arrays.asList(SelectTest.SelectIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

        //Verify that initial value in Label field is 'Select':
        $("#textfield_label").should(exist).shouldHave(text("Select"));

        //Verify that initially the Minimum count field is disabled
        $("#numberField_minCount").shouldBe(disabled);

        //Verify that initially the Maximum count field is disabled
        $("#numberField_minCount").shouldBe(disabled);

        //Verify that initial value in Direction dropdown is Horizontal
        $("#property_select_direction").should(exist).shouldHave(text("Horizontal"));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-SelectField-04").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForSelect")
    @ParameterizedTest
    @CsvFileSource(resources = "/select_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String text_label,
                             String text_help,
                             String edit_values,
                             String preselection_value,
                             String disableLabel,
                             String checkbox_required,
                             String checkbox_allow_multiple,
                             String text_numberField_minCount,
                             String text_numberField_maxCount,
                             String dropdown_direction


    ) {
        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $("#li-template-SelectField-04").should(appear).click();
        $("#formelement_properties_card").should(appear);

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
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            selectAndClear(By.id(SelectTest.SelectIds.textfield_label.name()))
                    .setValue(text_label).sendKeys(Keys.TAB);
            $(blockId).should(exist);
            $(blockId).shouldHave(text(text_label)).waitUntil(appears, 4000);
        }


        //Help
        if (StringUtils.isNotEmpty(text_help)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(SelectTest.SelectIds.textfield_help.name()))
                    .setValue(text_help).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(text_help)).waitUntil(appears, 4000);
        }

        //Values
        if (StringUtils.isNotEmpty(edit_values)) {

            String[] values = edit_values.split(",");

            $("#formelement_properties_card .editForm").should(exist).click(); //Click on edit value pen icon
            $("#form-value-list-card-dialog_content").should(exist); //Value List Editor window

            //Deleting the existing rows:
            List<SelenideElement> delBtn = $$("div.ag-pinned-right-cols-container .ag-row .fa-trash-alt");
            int countDelBtn = $$("div.ag-pinned-right-cols-container .ag-row .fa-trash-alt").size();
            for (int n = countDelBtn; n >= 1; n--) {
                String strDeleteBtn = ".ag-row:nth-child(" + n + ") .fa-trash-alt"; //Delete the n th row
                $(strDeleteBtn).click();
                $(strDeleteBtn).waitUntil(disappear, 10000);
            }

            //Add rows in value list editor for the number of labels
            if (!$("div.ag-pinned-right-cols-container .ag-row").exists()) {
                for (int x = 0; x < values.length; x++) {
                    $("#value_list_values button .fa-plus").should(exist).click();
                }
            }

            List<String> preselected = new ArrayList<>();
            if (StringUtils.isNotEmpty(preselection_value)) {
                preselected = Arrays.asList(preselection_value.split(","));
            }

            for (int i = 1; i <= values.length; i++) {
                //Click on label option
                String labelSelector = "div.ag-body-viewport .ag-center-cols-viewport .ag-row:nth-child(" + i + ") .ag-cell:nth-child(2)";
                $(labelSelector).should(exist).doubleClick();
                String labelValue = values[i - 1];
                $("div.ag-popup input.ag-input-field-input").sendKeys(Keys.BACK_SPACE); //Clear the default value in label field
                $("div.ag-popup input.ag-input-field-input").setValue(labelValue).sendKeys(Keys.ENTER);
                $(labelSelector).shouldHave(text(labelValue));

                if (preselected.contains(labelValue)) {
                    String checkboxSelector = "div.ag-pinned-left-cols-container .ag-row:nth-child(" + i + ") input";
                    $(checkboxSelector).should(exist).click();
                    $(checkboxSelector).shouldBe(checked);
                }
            }

            //Click on close button
            $("#form-value-list-card-dialog_actions #btnClosePropertiesForm").should(exist).click();

            //verify preselection on designer surface
            $(blockId).should(exist);
            for (int i = 1; i <= values.length; i++) {
                String labelValue = values[i - 1];
                $(blockId).find("fieldset label:nth-child(" + i + ")").shouldHave(text(labelValue));
                if (preselected.contains(labelValue)) {
                    $(blockId).find("fieldset label:nth-child(" + i + ") input").shouldBe(checked);
                } else {
                    $(blockId).find("fieldset label:nth-child(" + i + ") input").shouldNotBe(checked);
                }
            }
        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(disableLabel)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + SelectTest.SelectIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldNotHave(value(disableLabel)).waitUntil(appears, 4000);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String checkBoxId = "#" + SelectTest.SelectIds.checkbox_required.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).should(exist).shouldHave(text("*"));
        }

        //Enter Minimum Count
        if (StringUtils.isNotEmpty(text_numberField_minCount)) {
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            selectAndClear(By.id(SelectTest.SelectIds.numberField_minCount.name()))
                    .setValue(text_numberField_minCount).sendKeys(Keys.TAB);
            $("#numberField_minCount").shouldHave(value(text_numberField_minCount)).waitUntil(appears, 4000);
        }

        //Enter Maximum Count
        if (StringUtils.isNotEmpty(text_numberField_maxCount)) {
            //    $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(SelectIds.numberField_maxCount.name()))
                    .setValue(text_numberField_maxCount).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_maxCount").shouldHave(value(text_numberField_maxCount)).waitUntil(appears, 4000);
        }

        //Allow Multiple:
        if (StringUtils.isNotEmpty(checkbox_allow_multiple)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + SelectTest.SelectIds.checkbox_multiple.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);

            //Now the Minimum and Maximum count field should be enabled:
            $("#numberField_minCount").should(exist).shouldBe(enabled);
            $("#numberField_maxCount").should(exist).shouldBe(enabled);
        }

        //Direction
        //Click on Direction. Select Vertical
        if (StringUtils.isNotEmpty(dropdown_direction)) {
            $(By.id(SelectTest.SelectIds.property_select_direction.name())).should(exist).click();
            $(By.id(SelectTest.SelectIds.property_select_direction.name())).selectOptionByValue(dropdown_direction);
            $(By.id(SelectTest.SelectIds.property_select_direction.name())).shouldHave(value(dropdown_direction));
        }

    }
}
