package com.vo.createformdialog;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.*;

import java.util.List;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static reusables.ReuseActions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Language Properties")
public class LanguagePropertiesTest extends BaseTest {

    //Fetch languages from 'preferred form creation locales'
    //Pre-requisite: User has selected English and German as Preferred languages in Preferences and Default as English
    @Test
    @DisplayName("Verify initial setup in Add Language screen")
    @Order(1)
    public void validateInitialSet() {
        //Create Form:
        Pair<String, String> formTitleDesc = createForm();
        String formTitle = formTitleDesc.getLeft();
        String formDesc = formTitleDesc.getRight();

        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Click on Additional Options

        SelenideElement lC = $("#language_properties_container").should(appear);
        lC.$(".mtable_toolbar button:first-of-type").should(exist); //+ button in Add Language - confirmation that user has navigated

        //the buttons “BACK”, “CREATE FORM”, “NEXT” and “CANCEL” should all be enabled:
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-addlOptionsButton").shouldBe(enabled); //Next button

        //Define the table rows
        SelenideElement firstRow = lC.$("tbody tr:nth-of-type(1)");
        List<SelenideElement> cellsInFirstRow = firstRow.$$("td"); //Cells in the first row
        cellsInFirstRow.get(0).shouldHave(text("English - Great Britain"));
        cellsInFirstRow.get(1).shouldHave(text(formTitle)); //Form Title should have the same text as Title in Create Form
        cellsInFirstRow.get(2).shouldHave(text(formDesc)); //Form Description text should be same as Description in Create form
        cellsInFirstRow.get(3).shouldHave(value("true")); //Default button is checked for first row - English - German

        SelenideElement secondRow = lC.$("tbody tr:nth-of-type(2)");
        List<SelenideElement> cellsInSecondRow = secondRow.$$("td"); //Cells in the second row
        cellsInSecondRow.get(0).shouldHave(text("German - Germany"));
        cellsInSecondRow.get(1).shouldHave(text(formTitle)); //Form Title should have the same text as Title in Create Form
        cellsInSecondRow.get(2).shouldHave(text(formDesc)); //Form Description text should be same as Description in Create form
        cellsInSecondRow.get(3).shouldHave(value("false")); //Default button is unchecked for secondary language
    }


    @Test
    @DisplayName("Verify Delete Language in Additional Language screen")
    @Order(2)
    public void verifyDeleteLanguage() {
        SelenideElement lC = $("#language_properties_container").shouldBe(visible);
        SelenideElement secondRow = lC.$("tbody tr:nth-of-type(2)"); //Row for German - German

        secondRow.$(byAttribute("title", "Edit")).should(exist).click(); //Click on edit button for German - German
        secondRow = lC.$("tbody tr:nth-of-type(2)[mode=\"update\"]").should(appear);
        secondRow.$(byAttribute("title", "Cancel")).should(exist).click(); //Cancel the Edit
        secondRow = lC.$("tbody tr:nth-of-type(2)[mode=\"update\"]").should(disappear);
        secondRow = lC.$("tbody tr:nth-of-type(2)").should(appear);
        secondRow.$(byAttribute("title", "Edit")).should(appear); //User is back on the previous page
        secondRow.$("button [iconname=\"far fa-trash-alt\"]").should(appear).click(); //Delete button for German - German

        secondRow = lC.$("tbody tr:nth-of-type(2)[mode=\"delete\"]").should(appear);
        secondRow.shouldHave(text("Are you sure you want to delete this row?")); //Confirmation shown for deletion of secondary langauge

        secondRow.$("button:nth-of-type(2)").should(exist).click(); //Cancel is the second button, cancel the Deletion
        secondRow = lC.$("tbody tr:nth-of-type(2)[mode=\"delete\"]").should(disappear);
        secondRow = lC.$("tbody tr:nth-of-type(2)").should(appear);
        secondRow.$(byAttribute("title", "Edit")).should(exist); //User is back on the previous page
        secondRow.$("button [iconname=\"far fa-trash-alt\"]").should(appear).click(); //Delete button for German - German
        secondRow = lC.$("tbody tr:nth-of-type(2)[mode=\"delete\"]").should(appear);
        secondRow.shouldHave(text("Are you sure you want to delete this row?")); //Confirmation shown for deletion of secondary langauge
        secondRow.$(byAttribute("title", "Save")).click(); //Confirm the Deletion
        secondRow.should(disappear);
        lC.$$("tbody tr").shouldHave(size(1));

    }

    @Test
    @DisplayName("Verify Add Language in Additional Language screen")
    @Order(3)
    public void verifyAddLanguage() {
        SelenideElement lC = $("#language_properties_container").shouldBe(visible);
        lC.$(".mtable_toolbar button[title=\"Add\"]").shouldBe(visible, enabled).click(); //Click on + button

        //Define the table rows
        SelenideElement secondRow = lC.$("tbody tr:nth-of-type(2)[mode=\"add\"]").should(appear);
        List<SelenideElement> cellsInSecondRow = secondRow.$$("td"); //Cells in the first row
        cellsInSecondRow.get(3).should(exist).shouldNotBe(checked); //The checkbox for newly added Germany-Germany

        cellsInSecondRow.get(0).click(); //Click on Language Dropdown
        $("#menu- li").should(appear).shouldHave(text("German - Germany")).click();
        cellsInSecondRow.get(1).$(byAttribute("placeholder", "Form Title")).should(exist); //Column with Form Title should be enabled
        cellsInSecondRow.get(2).$(byAttribute("placeholder", "Form Description")).should(exist); //Column with Form Title should be enabled
        cellsInSecondRow.get(4).shouldBe(enabled); //Save button/Check is enabled
        cellsInSecondRow.get(3).shouldNotBe(checked); //Default checkbox is originally unchecked
        cellsInSecondRow.get(3).click(); //Check the checkbox for setting as Default language
        // Note: After selecting German as Default language and saving, only English - Great Britain is shown-> ??
        cellsInSecondRow.get(4).shouldBe(enabled).click(); //Click on Save tick

        SelenideElement firstRow = lC.$("tbody tr:nth-of-type(1)");
        List<SelenideElement> cellsInFirstRow = firstRow.$$("td"); //Cells in the first row
        cellsInFirstRow.get(0).click(); //Click on screen
        cellsInFirstRow.get(4).$(byAttribute("title", "Edit")).should(exist);
        cellsInFirstRow.get(4).$(byAttribute("title", "Edit")).click(); //Click on the Edit button
        cellsInFirstRow.get(1).shouldBe(enabled); //Form Title is now enabled
        cellsInFirstRow.get(1).shouldBe(enabled); //Form Description is now enabled
        $("#wizard-cancelButton").click(); //Click on Cancel button
        $("#confirmation-dialog-title").should(exist); //Confirmation for Cancellation is shown
        $("#btnConfirm").should(exist).click();
        $("#confirmation-dialog-content").shouldNot(appear); //Click on Confirm button
    }

}
