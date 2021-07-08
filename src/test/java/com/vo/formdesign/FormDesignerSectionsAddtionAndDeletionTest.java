package com.vo.formdesign;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the sections addition and deletion")
public class FormDesignerSectionsAddtionAndDeletionTest extends BaseTest {

    @Test
    @DisplayName("Verify the sections addition and deletion")
    public void addAndRemoveSections(){

        createNewForm();
        $("#wizard-createFormButton").should(exist).click();
        $("#btnFormDesignPublish").should(exist);
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Initial version
        $("#designer_formCardContent").should(exist);
        $("#section_1-loc_en-GB-r_1-c_1").should(exist);
        $("#section_1-loc_en-GB-r_1-c_1 div:nth-child(1) .fa-pen").click();
        $("#textfield_label").should(exist).setValue(" 01 ");//Add name to the section
        $("#block-loc_en-GB-r_1-c_1").should(exist).click();
        $("#li-template-Textfield-04").should(appear).click();
        $("#designer_formCardContent span div:nth-child(1)").click(); //Click on + icon to add new section
        $("#section_5-loc_en-GB-r_5-c_1").should(exist); //New section should be visible
        $("#section_5-loc_en-GB-r_5-c_1 > div:nth-child(2) button").should(exist).click(); //Click on + to add another section
        $("#section_9-loc_en-GB-r_9-c_1").should(exist); //New Section added
        $("#textfield_label").should(exist).setValue(" 02 ");//Add name to the section
        $("#section_9-loc_en-GB-r_9-c_1").$("#block-loc_en-GB-r_1-c_1").should(exist).click();
        $("#li-template-Textfield-04").should(appear).click();

        //Deletion of added sections
        $("#section_1-loc_en-GB-r_1-c_1").should(exist);
        $("#section_1-loc_en-GB-r_1-c_1 div:nth-child(1) .fa-pen").should(exist).click(); //Click on edit
        $("#blockButtonDelete").should(exist).click(); //Click on Delete button

        $("#section_5-loc_en-GB-r_5-c_1").should(exist);
        $("#section_5-loc_en-GB-r_5-c_1 div:nth-child(1) .fa-pen").should(exist).click(); //Click on edit
        $("#blockButtonDelete").should(exist).click(); //Click on Delete button

        $("#section_1-loc_en-GB-r_1-c_1").should(exist); //Section 1 should exist
        $("#block-loc_en-GB-r_1-c_1").should(exist).click();

    }
}
