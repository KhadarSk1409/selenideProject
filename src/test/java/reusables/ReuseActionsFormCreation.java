package reusables;

import com.vo.formdesign.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Selenide.$;
import static com.vo.BaseTest.*;
import static reusables.ReuseActions.createNewForm;

public class ReuseActionsFormCreation {

    public enum FormFieldIds {
        textfield_label,
        textfield_help,
        checkbox_disableLabel,
        checkbox_required,
        checkbox_readOnly
    }

    public static void navigateToFormDesign(String fieldName) {
        createNewForm();
        $("#wizard-createFormButton").should(exist).click();
        $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design

        String blockId1 = "#block-loc_en-GB-r_1-c_1"; //Need to change later as of now _1 is returning two results
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Initial version
        $(blockId1).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased

        switch (fieldName) {
            case "Number Field":
                $("#li-template-NumberField-04").should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);
                $("#panel2a-header").should(exist).click(); //Advanced section dropdown
                //options for text field should exist:
                Arrays.asList(NumberFieldTest.NumberFieldOptionsIds.values()).forEach(NumberFieldId -> $(By.id(NumberFieldId.name())).shouldBe(visible));
                $("#blockButtonDelete").shouldBe(visible).click();
                $("#li-template-NumberField-04").should(disappear);

            case "Currency Field":
                $("#li-template-CurrencyField-05").should(appear).click(); //li-template-CurrencyField-05
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(CurrencyFieldTest.CurrencyFieldOptionsIds.values()).forEach(CurrencyFieldOptionsIds -> $(By.id(CurrencyFieldOptionsIds.name())).shouldBe(visible));
                $("#sel_control_currencies .selLabelChip").shouldHave(text("EUR"));
                $("#blockButtonDelete").shouldBe(visible).click();
                $("#li-template-CurrencyField-05").should(disappear);
        }
    }

    //Label verification
    public static void labelVerificationOnFormDesign(String StrBlockId, String str_value) {
        $(StrBlockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        selectAndClear(By.id(FormFieldIds.textfield_label.name()))
                .setValue(str_value).sendKeys(Keys.TAB);
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(StrBlockId).shouldHave(text(str_value));
    }

    public static void hideLabelVerificationOnFormDesign(String StrBlockId, String str_value) {
        $(StrBlockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version

        String checkBoxId = "#" + FormFieldIds.checkbox_disableLabel.name();
        $(checkBoxId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(checkBoxId + " input").shouldBe(selected);
        $(StrBlockId).shouldNotHave(value(str_value));
    }

    public static void helpVerificationOnFormDesign(String StrBlockId, String str_value) {
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        selectAndClear(By.id(FormFieldIds.textfield_help.name()))
                .setValue(str_value).sendKeys(Keys.TAB);
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(StrBlockId).shouldHave(text(str_value));
    }

    public static void requiredCheckboxVerificationOnFormDesign(String StrBlockId) {
        $(StrBlockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        String checkBoxId = "#" + FormFieldIds.checkbox_required.name();
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(checkBoxId).shouldBe(visible).click();
        //$(checkBoxId + " input").shouldHave(value("true"));
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(checkBoxId + " input").shouldBe(selected);
    }

    public static void readOnlyCheckboxOnFormDesign(String StrBlockId) {
        $(StrBlockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        String checkBoxId = "#" + FormFieldIds.checkbox_readOnly.name();
        $(checkBoxId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(checkBoxId + " input").shouldBe(selected);

    }


}
