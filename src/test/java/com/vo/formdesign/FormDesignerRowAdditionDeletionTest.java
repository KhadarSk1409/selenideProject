package com.vo.formdesign;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the row addition and deletion in form designer")
public class FormDesignerRowAdditionDeletionTest extends BaseTest {

    @Test
    @DisplayName("Verify the addition and deletion of rows")
    public void addAndDeleteRows() {

        createNewForm();
        $("#wizard-createFormButton").should(exist).click();
        $("#btnFormDesignPublish").should(exist);
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Initial version
        $("#block-loc_en-GB-r_1-c_1").should(exist).click();
        $("#li-template-Textfield-04").should(appear).click();
        $("#block-loc_en-GB-r_1-c_1 div:nth-child(1) div:nth-child(2) .fa-pen").should(exist).click();
        $("#textfield_label").should(exist).setValue(" 01 ");
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased

        $("#block-loc_en-GB-r_2-c_1").should(exist).click();
        $("#li-template-Textfield-04").should(appear).click();
        $("#block-loc_en-GB-r_2-c_1 div:nth-child(1) .fa-pen").should(exist).click();
        $("#textfield_label").should(exist).setValue(" 02 ");
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased

        $("#block-loc_en-GB-r_4-c_1").should(exist).click();
        $("#li-template-Textfield-04").should(appear).click();
        $("#block-loc_en-GB-r_4-c_1 .fa-pen").should(exist).click();
        $("#textfield_label").should(exist).setValue(" 03 ");
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $("#block-loc_en-GB-r_4-c_2").should(exist).click();

        //Adding rows
        $("#block-loc_en-GB-r_1-c_1").should(exist);
        $("#block-loc_en-GB-r_1-c_1 .fa-plus").should(exist).click(); //Click on + icon to add new row
        $("#block-loc_en-GB-r_2-c_1").should(exist).shouldNotHave(Condition.attribute("textfield_label"));
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version has increased

        $("#block-loc_en-GB-r_5-c_1").should(exist);
        $("#block-loc_en-GB-r_5-c_1 div:nth-child(2) .fa-plus").should(exist).click(); ////Click on + icon to add new row
        $("#block-loc_en-GB-r_6-c_1").should(exist);
        $("#block-loc_en-GB-r_6-c_1").should(exist).shouldNotHave(Condition.attribute("textfield_label"));
        $("#block-loc_en-GB-r_6-c_2").should(exist).click();

        //Deletions of added rows
        $("#block-loc_en-GB-r_2-c_1 .fa-trash-alt").should(exist).click();
        $("#block-loc_en-GB-r_2-c_2").should(exist).click();

        $("#block-loc_en-GB-r_5-c_1 .fa-trash-alt").should(exist).click();
        $("#block-loc_en-GB-r_5-c_2").should(exist).click();
        
    }
}
