package com.vo.createformdialog;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import java.util.List;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LanguageProperties extends BaseTest {

    //Fetch languages from 'preferred form creation locales'
    //Pre-requisite: User has selected English and German as Preferred languages in Preferences and Default as English
    @Test
    @DisplayName("Verify initial setup in Add Language screen")
    @Order(1)
    public void validateIntialSetup() {
        //Create Form:
        $("#toDashboard").click(); //Go back to Dashboard
        $("#btnCreateForm").should(exist).click(); //Click on Create Form button
        $("#wizardFormDlg").should(appear); //Create Form wizard appears
        String formTitle = RandomStringUtils.randomAlphanumeric(4);
        $("#wizard-formTitle").setValue(formTitle); //Set Title name
        String formDesc = RandomStringUtils.randomAlphanumeric(5);
        $("#wizard-formHelp").setValue(formDesc); //Setting form Description
        $("#btnCreateForm").shouldBe(enabled); //Create Form button should be enabled
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Click on Additional Options

        $("#wizardFormDlg .mtable_toolbar button:first-of-type").should(exist); //+ button in Add Language - confirmation that user has navigated

        //the buttons “BACK”, “CREATE FORM”, “NEXT” and “CANCEL” should all be enabled:
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(enabled);
        $("#wizard-addlOptionsButton").shouldBe(enabled); //Next button
        $("#wizard-cancelButton").shouldBe(enabled);

        //Define the table rows
        SelenideElement firstRow = $$("#wizardFormDlg tbody tr").get(0);
        List<SelenideElement> cellsInFirstRow = firstRow.$$("td"); //Cells in the first row
        cellsInFirstRow.get(0).shouldHave(text("English - Great Britain"));
        cellsInFirstRow.get(1).shouldHave(text(formTitle)); //Form Title should have the same text as Title in Create Form
        cellsInFirstRow.get(2).shouldHave(text(formDesc)); //Form Description text should be same as Description in Create form
        cellsInFirstRow.get(3).shouldHave(value("true")); //Default button is checked for first row - English - German

        SelenideElement SecondRow = $$("#wizardFormDlg tbody tr").get(1);
        List<SelenideElement> cellsInSecondRow = SecondRow.$$("td"); //Cells in the second row
        cellsInSecondRow.get(0).shouldHave(text("German - Germany"));
        cellsInSecondRow.get(1).shouldHave(text(formTitle)); //Form Title should have the same text as Title in Create Form
        cellsInSecondRow.get(2).shouldHave(text(formDesc)); //Form Description text should be same as Description in Create form
        cellsInSecondRow.get(3).shouldHave(value("false")); //Default button is unchecked for secondary language
    }


    @Test
    @DisplayName("Verify Delete Language in Additional Language screen")
    @Order(2)
    public void verifyDeleteLanguage() {
        SelenideElement SecondRow = $$("#wizardFormDlg tbody tr").get(1); //Row for German - German
        List<SelenideElement> cellsInSecondRow = SecondRow.$$("td"); //Cells in the second row
        cellsInSecondRow.get(4).$(byAttribute("title", "Edit")).should(exist).click(); //Click on edit button for German - German
        cellsInSecondRow.get(4).$(byAttribute("title", "Cancel")).should(exist).click(); //Cancel the Edit
        cellsInSecondRow.get(4).$(byAttribute("title", "Edit")).should(exist); //User is back on the previous page
        cellsInSecondRow.get(1).click(); //Click on Screen

        cellsInSecondRow.get(4).$(byAttribute("title", "Delete")).should(exist);
        cellsInSecondRow.get(4).$(byAttribute("title", "Delete")).click(); //Delete button for German - German
        cellsInSecondRow.get(0).shouldHave(text("Are you sure you want to delete this row?")); //Confirmation shown for deletion of secondary langauge
        cellsInSecondRow.get(0).click(); //Click on Screen
        cellsInSecondRow.get(1).$(byAttribute("title", "Cancel")).should(exist); //Cancel the Deletion
        cellsInSecondRow.get(1).$(byAttribute("title", "Cancel")).click();
        cellsInSecondRow.get(4).$(byAttribute("title", "Edit")).should(exist); //User is back on the previous page
        cellsInSecondRow.get(1).click(); //Click on screen
        cellsInSecondRow.get(4).$(byAttribute("title", "Delete")).should(exist);
        cellsInSecondRow.get(4).$(byAttribute("title", "Delete")).click(); //Delete button for German - German
        cellsInSecondRow.get(1).$(byAttribute("title", "Save")).click(); //Confirm the Deletion

    }

    @Test
    @DisplayName("Verify Add Language in Additional Language screen")
    @Order(3)
    public void verifyAddLanguage() {
        $(".mtable_toolbar button[title=\"Add\"]").should(exist).click(); //Click on + button

        //Define the table rows
        SelenideElement secondRow = $$("#wizardFormDlg tbody tr").get(1);
        List<SelenideElement> cellsInSecondRow = secondRow.$$("td"); //Cells in the first row

        cellsInSecondRow.get(3).should(exist).shouldNotBe(checked); //The checkbox for newly added Germany-Germany

        cellsInSecondRow.get(0).click(); //Click on Language Dropdown
        $(By.xpath("//body/div[@id='menu-']/div[3]/ul[1]/li[1]")).shouldHave(text("German - Germany")).click(); //->German - Germany dropdown menu, id??
        //cellsInSecondRow.get(1).shouldHave(text("Form Title")); -> Not working ??
        //cellsInSecondRow.get(2).shouldHave(text("Form Description")); -> Not working ??
        cellsInSecondRow.get(4).shouldBe(enabled); //Save button/Check is enabled
        cellsInSecondRow.get(3).shouldNotBe(checked); //Default checkbox is originally unchecked
        cellsInSecondRow.get(3).click(); //Check the chekbox for setting as Default language
        // Note: After selecting German as Default language and saving, only English - Great Britain is shown-> ??
        cellsInSecondRow.get(4).shouldBe(enabled).click(); //Click on Save tick

        SelenideElement firstRow = $$("#wizardFormDlg tbody tr").get(0);
        List<SelenideElement> cellsInFirstRow = firstRow.$$("td"); //Cells in the first row
        cellsInFirstRow.get(0).click(); //Click on screen
        cellsInFirstRow.get(4).$(byAttribute("title", "Edit")).should(exist);
        cellsInFirstRow.get(4).$(byAttribute("title", "Edit")).click(); //Click on the Edit button
        cellsInFirstRow.get(1).shouldBe(enabled); //Form Title is now enabled
        cellsInFirstRow.get(1).shouldBe(enabled); //Form Description is now enabled
        $("#wizard-cancelButton").click(); //Click on Cancel button
        $("#confirmation-dialog-title").should(exist); //Confirmation for Cancellation is shown
        $("#confirmation-dialog-content").should(exist).click(); //Click on Confirm button

    }

}
