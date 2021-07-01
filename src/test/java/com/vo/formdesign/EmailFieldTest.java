package com.vo.formdesign;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static reusables.ReuseActions.createNewForm;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Email Field Tests")
public class EmailFieldTest extends BaseTest {

    protected static ThreadLocal<String> formName = ThreadLocal.withInitial(() -> "Emailfield Form-Design Auto Test " + BROWSER_CONFIG.get() + " " + System.currentTimeMillis());

    enum EmailFielsIds {
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
        createNewForm();
        $("#wizard-createFormButton").should(exist).click();
        $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design

        String blockId = "#block-loc_en-GB-r_1-c_1"; //Need to change later as of now _1 is returning two results
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Initial version
        $(blockId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased

        //Click on Show More
        $("#template_basis_list").find(byText("Show More")).should(exist).click();

        $("#li-template-EmailField-04").should(appear).click();
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(appear);

        $("#panel2a-header").should(exist).click(); //Advanced section dropdown

        //options for text field should exist:
        Arrays.asList(EmailFieldTest.EmailFielsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

        $("#blockButtonDelete").shouldBe(visible).click();
        $("#li-template-EmailField-04").should(disappear);
    }

    @Order(2)
    @DisplayName("createNewFormulaDesignForEmailfields")
    @ParameterizedTest
    @CsvFileSource(resources = "/email_field_test_data.csv", numLinesToSkip = 1)
    public void alltextfield(Integer row, Integer col, Integer colSpan,
                             String text_label,
                             String checkbox_disableLabel,
                             String text_help,
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
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $("#li-template-EmailField-04").should(appear).click();
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
            selectAndClear(By.id(EmailFieldTest.EmailFielsIds.textfield_label.name()))
                    .setValue(text_label).sendKeys(Keys.TAB);
            $(blockId).should(exist);
            $(blockId).shouldHave(text(text_label)).waitUntil(appears, 4000);
        }


        //Help
        if (StringUtils.isNotEmpty(text_help)) {
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            selectAndClear(By.id(EmailFieldTest.EmailFielsIds.textfield_help.name()))
                    .setValue(text_help).sendKeys(Keys.TAB);
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(blockId).shouldHave(text(text_help)).waitUntil(appears, 4000);
        }

        //Hide(disable) Label
        if (StringUtils.isNotEmpty(checkbox_disableLabel)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            String checkBoxId = "#" + EmailFieldTest.EmailFielsIds.checkbox_disableLabel.name();
            $(checkBoxId).shouldBe(visible).click();
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).shouldNotHave(value(checkbox_disableLabel)).waitUntil(appears, 4000);
        }

        //required
        if (StringUtils.isNotEmpty(checkbox_required)) {
            $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
            String checkBoxId = "#" + EmailFieldTest.EmailFielsIds.checkbox_required.name();
            String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
            $(checkBoxId).shouldBe(visible).click();
            //$(checkBoxId + " input").shouldHave(value("true"));
            $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
            $(checkBoxId + " input").shouldBe(selected);
            $(blockId).should(exist).shouldHave(text("*"));
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
                $("#numberField_defaultValueNumber-helper-text").should(exist).shouldHave(text("Must be set, if read only")); //-> Defect raised for this part
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
}