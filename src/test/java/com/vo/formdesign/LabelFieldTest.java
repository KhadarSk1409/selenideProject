package com.vo.formdesign;

import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import java.util.Arrays;
import java.util.stream.IntStream;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.createNewForm;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Label Field Creation Tests")
public class LabelFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Label Field Test Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    enum LabelFieldOptionsIds {
        textfield_value;
    }

    @Test
    @Order(1)
    @DisplayName("precondition")
    public void precondition() {
        createNewForm();
        $("#wizard-createFormButton").should(exist).click();
        $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design

        String blockId = "#block-loc_en-GB-r_1-c_1"; //Need to change later as of now _1 is returning two results
        $(blockId).shouldBe(visible);
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Initial version
        $(blockId).click();
        $("#li-template-LabelField-05").should(appear);
        $("#formelement_properties_card").should(appear);
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased
        $(blockId).shouldBe(visible).click();
        $("#li-template-LabelField-05").should(appear).click();
        $("#textfield_value").should(appear);

        //options for text field should exist:
        Arrays.asList(LabelFieldTest.LabelFieldOptionsIds.values()).forEach(valueId -> $(By.id(valueId.name())).shouldBe(visible));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-LabelField-05").should(disappear);

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
        $("#li-template-LabelField-05").should(appear).click();
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
        }
    }

}
