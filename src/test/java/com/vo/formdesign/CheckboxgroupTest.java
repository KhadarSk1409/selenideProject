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
@DisplayName("Checkboxgroup Tests")
public class CheckboxgroupTest extends BaseTest {

    enum CheckboxgroupIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        property_select_direction,
        checkbox_globalSelection,
        numberField_minCount,
        numberField_maxCount,
        checkbox_other;
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
        $("#li-template-CheckboxGroupField-03").should(appear).click();
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(appear);

        $("#panel2a-header").should(exist).click(); //Advanced section dropdown

        //options for text field should exist:
        Arrays.asList(CheckboxgroupTest.CheckboxgroupIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

        //Verify that initial value in Direction dropdown is Horizontal
        $("#property_select_direction").should(exist).shouldHave(text("Horizontal"));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-CheckboxGroupField-03").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForTextfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/checkboxgroup_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String text_label,
                             String text_help,
                             String edit_values,
                             String disableLabel,
                             String checkbox_required,
                             String checkbox_globalSelection,
                             String text_numberField_minCount,
                             String text_numberField_maxCount,
                             String checkbox_other_values,
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
        $("#li-template-CheckboxGroupField-03").should(appear).click();
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
            selectAndClear(By.id(CheckboxgroupTest.CheckboxgroupIds.textfield_label.name()))
                    .setValue(text_label).sendKeys(Keys.TAB);
            $(blockId).should(exist);
            $(blockId).shouldHave(text(text_label)).waitUntil(appears, 4000);
        }


        //Help
        if (StringUtils.isNotEmpty(text_help)) {
            // $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CheckboxgroupTest.CheckboxgroupIds.textfield_help.name()))
                    .setValue(text_help).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(text_help)).waitUntil(appears, 4000);
        }

        //Values
        if (StringUtils.isNotEmpty(edit_values)) {

            String[] arr = edit_values.split(",");

            $("#formelement_properties_card .editForm").should(exist).click(); //Click on edit value pen icon
            $("#form-value-list-card-dialog_content").should(exist); //Value List Editor window

            //Deleting the existing rows:
            while ($("div.ag-pinned-right-cols-container .ag-row div div button").exists()) {
                $("div.ag-pinned-right-cols-container .ag-row div div button").waitUntil(appear, 5000);
                $("div.ag-pinned-right-cols-container .ag-row div div button").click();
            }

            //Add rows in value list editor for the number of labels
            if (!$("div.ag-pinned-right-cols-container .ag-row").exists()) {
                for (int x = 0; x < arr.length; x++) {
                    $("#value_list_values span button").should(exist).click();
                }
            }

            for (int i = 0; i < arr.length; i++) {
                int j = i + 1;

                //Click on label option
                String strLabel = "div.ag-body-viewport .ag-center-cols-viewport .ag-row:nth-child(" + j + ") .ag-cell:nth-child(2)";
                $(strLabel).should(exist).doubleClick();
                String str = arr[i].toString();
                $("div.ag-popup input.ag-input-field-input").sendKeys(Keys.BACK_SPACE); //Clear the default value in Currencies field
                $("div.ag-popup input.ag-input-field-input").setValue(str).sendKeys(Keys.ENTER);
                $(strLabel).shouldHave(text(str));
            }

            //Select a record as primary
            $("div.ag-pinned-left-cols-container .ag-row:nth-child(1) input").should(exist).click();

            //Click on close button
            $("#form-value-list-card-dialog_actions #btnClosePropertiesForm").should(exist).click();

            Arrays.asList(edit_values.split(",")).forEach(labelvalue -> {
                //Verify that values are reflecting in the block:
                $(blockId).should(exist).should(exist).shouldHave(text(labelvalue));
            });

        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(disableLabel)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CheckboxgroupTest.CheckboxgroupIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldNotHave(value(disableLabel)).waitUntil(appears, 4000);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String checkBoxId = "#" + CheckboxgroupTest.CheckboxgroupIds.checkbox_required.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }


        //Enter Minimum Value
        if (StringUtils.isNotEmpty(text_numberField_minCount)) {
            //    $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CheckboxgroupTest.CheckboxgroupIds.numberField_minCount.name()))
                    .setValue(text_numberField_minCount).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_minCount").shouldHave(value(text_numberField_minCount)).waitUntil(appears, 4000);
        }

        //Enter Maximum Value
        if (StringUtils.isNotEmpty(text_numberField_maxCount)) {
            //    $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr2 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(CheckboxgroupIds.numberField_maxCount.name()))
                    .setValue(text_numberField_maxCount).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr2)); //Verify that version has increased
            $("#numberField_maxCount").shouldHave(value(text_numberField_maxCount)).waitUntil(appears, 4000);
        }

        //Allow select:
        if (StringUtils.isNotEmpty(checkbox_globalSelection)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CheckboxgroupTest.CheckboxgroupIds.checkbox_globalSelection.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Other values:
        if (StringUtils.isNotEmpty(checkbox_other_values)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + CheckboxgroupTest.CheckboxgroupIds.checkbox_other.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
        }

        //Direction
        //Click on Direction. Select Vertical
        if (StringUtils.isNotEmpty(dropdown_direction)) {
            $(By.id(CheckboxgroupTest.CheckboxgroupIds.property_select_direction.name())).should(exist).click();
            $(By.id(CheckboxgroupTest.CheckboxgroupIds.property_select_direction.name())).selectOptionByValue(dropdown_direction);
            $(By.id(CheckboxgroupTest.CheckboxgroupIds.property_select_direction.name())).shouldHave(value(dropdown_direction));
        }

    }
}
