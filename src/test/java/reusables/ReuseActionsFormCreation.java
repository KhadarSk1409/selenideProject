package reusables;

import com.vo.formdesign.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.vo.BaseTest.*;
import static reusables.ReuseActions.createNewForm;

public class ReuseActionsFormCreation {

    public enum FormField {
        NUMBER("Number Field", "li-template-NumberField-05"),
        TEXTFIELD("Text Field", "li-template-Textfield-05"),
        TEXTFIELD_BLOCKACTIONS("TextField BlockActions", "li-template-Textfield-05"),
        CHECKBOX_DISABLE_LABEL("Checkbox DisableLabel", "li-template-CheckboxGroupField-04"),
        CURRENCY("Currency Field", "li-template-CurrencyField-06"),
        EMAIL("Email Field", "li-template-EmailField-05"),
        DATE("Date Field", "li-template-DateField-04"),
        FILE_UPLOAD_FIELD("File Upload Field", "li-template-FileUploadField-04"),
        FORMATTED_TEXT("Formatted Text", "li-template-RichTextEditor-06"),
        LABEL_FIELD("Label Field", "li-template-LabelField-06"),
        RADIO_GROUP("Radio Group", "li-template-RadioGroupField-04"),
        SELECT_TEST("Select Test", "li-template-SelectField-05"),
        TEXTAREA_FIELD("Textarea Field", "li-template-TextareaField-06"),
        TIME_FIELD("Time Field", "li-template-TimeField-04");


        public String fieldName;
        public String fieldId;

        private FormField(String fieldName, String fieldId) {
            this.fieldName = fieldName;
            this.fieldId = fieldId;
        }

    }

    public static void navigateToFormDesign(FormField formField) {
        createNewForm();
        $("#wizard-createFormButton").should(exist).click();
        $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design

        String blockId1 = "#block-loc_en-GB-r_1-c_1"; //Need to change later as of now _1 is returning two results
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Initial version
        $(blockId1).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased
        //Click on Show More
        $("#template_basis_list").find(byText("Show More")).should(exist).click();

        switch (formField) {
            case NUMBER:
                $("#" + formField.fieldId).should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);
                $("#panel2a-header").should(exist).click(); //Advanced section dropdown
                //options for text field should exist:
                Arrays.asList(NumberFieldTest.NumberFieldOptionsIds.values()).forEach(NumberFieldId -> $(By.id(NumberFieldId.name())).shouldBe(visible));
                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case CURRENCY:
                $("#" + formField.fieldId).should(appear).click(); //li-template-CurrencyField-05
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(CurrencyFieldTest.CurrencyFieldOptionsIds.values()).forEach(CurrencyFieldOptionsIds -> $(By.id(CurrencyFieldOptionsIds.name())).shouldBe(visible));
                $("#sel_control_currencies .selLabelChip").shouldHave(text("EUR"));
                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case EMAIL:
                $("#" + formField.fieldId).should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown
                //options for text field should exist:
                Arrays.asList(EmailFieldTest.EmailFielsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));
                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case DATE:
                $("#" + formField.fieldId).should(appear).click(); //li-template-DateField-04
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(DateFieldTest.DateFieldOptionsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));
                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case CHECKBOX_DISABLE_LABEL:
                $("#" + formField.fieldId).should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);
                $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(CheckboxgroupTest.CheckboxgroupIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

                //Verify that initial value in Direction dropdown is Horizontal
                $("#property_select_direction").should(exist).shouldHave(text("Horizontal"));

                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case FILE_UPLOAD_FIELD:
                $("#" + formField.fieldId).should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);

                //Verify that default value in Label field is "File Upload Field"
                $(By.id(FileUploadFieldTest.FileUploadFieldIds.textfield_label.name())).shouldHave(text("File Upload Field"));

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(FileUploadFieldTest.FileUploadFieldIds.values()).forEach(fileUploadFieldId -> $(By.id(fileUploadFieldId.name())).shouldBe(visible));

                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case FORMATTED_TEXT:
                $("#" + formField.fieldId).should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);
                $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(FormattedTextTest.FormattedTextIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case LABEL_FIELD:
                $("#" + formField.fieldId).should(appear);
                $("#formelement_properties_card").should(appear);
                $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased
                $(blockId1).shouldBe(visible).click();
                $("#" + formField.fieldId).should(appear).click();
                $("#textfield_value").should(appear);

                //options for text field should exist:
                Arrays.asList(LabelFieldTest.LabelFieldOptionsIds.values()).forEach(valueId -> $(By.id(valueId.name())).shouldBe(visible));

                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case RADIO_GROUP:
                $("#" + formField.fieldId).should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(RadioGroupTest.RadiogroupIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

                //Verify that initial value in Direction dropdown is Horizontal
                $("#property_select_direction").should(exist).shouldHave(text("Horizontal"));

                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case SELECT_TEST:
                $("#" + formField.fieldId).should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(SelectTest.SelectIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case TEXTAREA_FIELD:
                $("#" + formField.fieldId).should(exist).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(exist);
                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field area should exist:
                Arrays.asList(TextAreaFieldTest.TextAreaFieldOptionsIds.values()).forEach(textAreaFieldId -> $(By.id(textAreaFieldId.name())).shouldBe(visible));

                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case TEXTFIELD_BLOCKACTIONS:
                $("#" + formField.fieldId).should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);
                break;

            case TEXTFIELD:
                $("#" + formField.fieldId).should(appear).click();
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);

                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(TextFieldTest.TextFieldOptionsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));

                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;

            case TIME_FIELD:
                $("#" + formField.fieldId).should(appear).click(); //li-template-TimeField-04
                $(blockId1).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
                $("#formelement_properties_card").should(appear);
                $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased
                $("#panel2a-header").should(exist).click(); //Advanced section dropdown

                //options for text field should exist:
                Arrays.asList(TimeFieldTest.TimeFieldOptionsIds.values()).forEach(textFieldId -> $(By.id(textFieldId.name())).shouldBe(visible));
                $("#blockButtonDelete").shouldBe(visible).click();
                $("#" + formField.fieldId).should(disappear);
                break;


        }
    }

    //Label verification
    public static void labelVerificationOnFormDesign(String StrBlockId, String str_value) {
        $(StrBlockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        selectAndClear(By.id(DesignFormFieldIds.textfield_label.name()))
                .setValue(str_value).sendKeys(Keys.TAB);
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(StrBlockId).shouldHave(text(str_value));
    }

    //Hide label
    public static void hideLabelVerificationOnFormDesign(String StrBlockId, String str_value) {
        $(StrBlockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        String checkBoxId = "#" + DesignFormFieldIds.checkbox_disableLabel.name();
        $(checkBoxId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(checkBoxId + " input").shouldBe(selected);
        $(StrBlockId).shouldNotHave(value(str_value));
    }

    //Help
    public static void helpVerificationOnFormDesign(String StrBlockId, String str_value) {
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        selectAndClear(By.id(DesignFormFieldIds.textfield_help.name()))
                .setValue(str_value).sendKeys(Keys.TAB);
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(StrBlockId).shouldHave(text(str_value));
    }

    //Required
    public static void requiredCheckboxVerificationOnFormDesign(String StrBlockId) {
        $(StrBlockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        String checkBoxId = "#" + DesignFormFieldIds.checkbox_required.name();
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        $(checkBoxId).shouldBe(visible).click();
        //$(checkBoxId + " input").shouldHave(value("true"));
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(checkBoxId + " input").shouldBe(selected);
    }

    //Read only
    public static void readOnlyCheckboxOnFormDesign(String StrBlockId) {
        $(StrBlockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        String initialVerNumStr1 = $("#formMinorversion").should(exist).getText(); //Fetch initial version
        String checkBoxId = "#" + DesignFormFieldIds.checkbox_readOnly.name();
        $(checkBoxId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(checkBoxId + " input").shouldBe(selected);

    }

    public enum DesignFormFieldIds {
        textfield_label,
        checkbox_disableLabel,
        textfield_help,
        checkbox_required,
        checkbox_readOnly;

    }


}
