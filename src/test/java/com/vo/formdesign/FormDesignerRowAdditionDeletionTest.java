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
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the row addition and deletion in form designer")
public class FormDesignerRowAdditionDeletionTest extends BaseTest {

    @Test
    @DisplayName("Verify the addition and deletion of rows")
    public void addAndDeleteRows() {

        createNewForm();
        $(elementLocators("CreateFormButton")).should(exist).click();
        $(elementLocators("PublishButton")).should(exist);
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Initial version
        $(elementLocators("BlockR1C1")).should(exist).click();
        $(elementLocators("TemplateCard")).should(appear);
        $(elementLocators("TextField")).should(appear).click();
        $(elementLocators("BlockR1C1PenIcon")).should(exist).click();
        $(elementLocators("TextFieldLabel")).should(exist).setValue(" 01 ");
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $(elementLocators("BlockR1C2")).should(exist).click();

        $(elementLocators("BlockR2C1")).should(exist).click();
        $(elementLocators("TemplateCard")).should(appear);
        $(elementLocators("TextField")).should(appear).click();
        $(elementLocators("BlockR2C1PenIcon")).should(exist).click();
        $(elementLocators("TextFieldLabel")).should(exist).setValue(" 02 ");
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version has increased

        $(elementLocators("BlockR4C2")).click();
        $(elementLocators("BlockR4C1")).should(exist).click();
        $(elementLocators("TextField")).should(appear).click();
        $(elementLocators("BlockR4C1PenIcon")).should(exist).click();
        $(elementLocators("TextFieldLabel")).should(exist).setValue(" 03 ");
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $(elementLocators("BlockR4C2")).should(exist).click();

        //Adding rows
        //Adding a new row before existing first row
        String initialVerNumStr1 = $(elementLocators("InitialVersion")).should(exist).getText(); //Initial version
        $(elementLocators("Section1")).should(exist);
        $(elementLocators("Row1FirstPlusIconToAddRow")).should(exist).click();
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(elementLocators("BlockR1C1")).should(exist).click();
        $(elementLocators("TemplateCard")).should(appear);
        $(elementLocators("TextField")).should(appear).click();
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(elementLocators("BlockR1C1PenIcon")).should(exist).click();
        $(elementLocators("TextFieldLabel")).should(exist).setValue(" 000 ");
        $(elementLocators("BlockR1C2")).click();
        $(elementLocators("BlockR1C1")).should(exist).shouldHave(Condition.text("Text field 000"));

        //Adding a new row in between existing rows
        $(elementLocators("BlockR2C1PlusIconToAddRow")).should(exist).click(); //Click on + icon to add new row
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(elementLocators("BlockR3C1")).should(appear).click();
        $(elementLocators("TemplateCard")).should(appear);
        $(elementLocators("TextField")).should(appear).click();
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(elementLocators("BlockR3C1PenIcon")).should(exist).click();
        $(elementLocators("TextFieldLabel")).should(exist).setValue(" 001 ");
        $(elementLocators("BlockR3C2")).click();
        $(elementLocators("BlockR3C1")).should(exist).shouldHave(Condition.text("Text field 001"));

        //Adding a new row after the existing row
        $(elementLocators("BlockR6C1PlusIconToAddRow")).should(exist).click(); //Click on + icon to add new row
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(elementLocators("BlockR7C1")).should(exist).click();
        $(elementLocators("TemplateCard")).should(appear);
        $(elementLocators("TextField")).should(appear).click();
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr1)); //Verify that version has increased
        $(elementLocators("BlockR7C1PenIcon")).should(exist).click();
        $(elementLocators("TextFieldLabel")).should(exist).setValue(" 002 ");
        $(elementLocators("BlockR7C2")).click();
        $(elementLocators("BlockR7C1")).should(exist).shouldHave(Condition.text("Text field 002"));

        //Deletions of added rows
        $(elementLocators("1stRowDeleteBtn")).should(exist).click(); //Click on Delete 1st row
        $(elementLocators("BlockR1C2")).should(exist).click();
        $(elementLocators("BlockR1C1")).should(exist).shouldNotHave(Condition.text("Text field 000")); //Verify the added row is deleted

        $(elementLocators("2ndRowDeleteBtn")).should(exist).click(); //Click on Delete 2nd row
        $(elementLocators("BlockR2C2")).should(exist).click();
        $(elementLocators("BlockR2C1")).should(exist).shouldNotHave(Condition.text("Text field 001")); //Verify the added row is deleted

        $(elementLocators("5thRowDeleteBtn")).should(exist).click(); //Click on Delete 5th row
        $(elementLocators("BlockR4C2")).should(exist).click();
        $(elementLocators("BlockR4C1")).should(exist).shouldNotHave(Condition.text("Text field 002")); //Verify the added row is deleted

    }
}
