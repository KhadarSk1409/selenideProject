package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import reusables.ReuseActionsFormCreation;

import static com.codeborne.selenide.Selenide.executeJavaScript;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActionsFormCreation.labelVerificationOnFormDesign;
import static reusables.ReuseActionsFormCreation.navigateToFormDesign;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Label Field Creation Tests")
public class LabelFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Label Field Test Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    public enum LabelFieldOptionsIds {
        textfield_value;
    }

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() {
        navigateToFormDesign(ReuseActionsFormCreation.FormField.LABEL_FIELD);

    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForLabelfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/label_field_test_data.csv", numLinesToSkip = 1)
    public void allLabelField(Integer row, Integer col, Integer colSpan, String textfield_value) throws InterruptedException {

        String blockId = "#block-loc_en-GB-r_" + row + "-c_" + col;

        //create new block, if not exist
        if (!$(blockId).exists()) {
            String prevBlockId = "#block-loc_en-GB-r_" + (row - 1) + "-c_" + col;
            $(prevBlockId + " .add-row").shouldBe(visible).click();
        }
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(blockId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased

        $("#formelement_properties_card").should(appear);
        $("#li-template-LabelField-06").should(appear).click();
        $(blockId).shouldBe(visible).click(); //? actually this should not be needed again

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
        if (StringUtils.isNotEmpty(textfield_value)) {
            $(blockId).shouldBe(visible).click();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(LabelFieldTest.LabelFieldOptionsIds.textfield_value.name()))
                    .setValue(textfield_value).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId + " input").shouldHave(value(textfield_value));
            //  labelVerificationOnFormDesign(blockId,textfield_value);
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

    @Test
    @Order(4)
    @DisplayName("verify fill form for Label field")
    @ParameterizedTest
    @CsvFileSource(resources = "/label_field_test_data.csv", numLinesToSkip = 1)
    public void verifyFillFormForLabelField(Integer row, Integer col, Integer colSpan, String textfield_value) throws InterruptedException {

        String blockStr = "#data_block-loc_en-GB-r_" + row + "-c_" + col;
        String labelInFillForm = blockStr + " .MuiFormLabel-root";

        if (StringUtils.isNotEmpty(textfield_value)) {
            $(labelInFillForm).shouldHave(text(textfield_value)); //Verify that Label appears on the form

        }
    }

}

