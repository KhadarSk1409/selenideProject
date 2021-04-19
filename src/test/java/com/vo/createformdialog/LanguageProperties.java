package com.vo.createformdialog;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import java.util.List;
import static com.codeborne.selenide.Condition.*;
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
        List<SelenideElement> cellsInSecondRow = SecondRow.$$("td"); //Cells in the first row
        cellsInSecondRow.get(0).shouldHave(text("German - Germany"));
        cellsInSecondRow.get(1).shouldHave(text(formTitle)); //Form Title should have the same text as Title in Create Form
        cellsInSecondRow.get(2).shouldHave(text(formDesc)); //Form Description text should be same as Description in Create form
        cellsInSecondRow.get(3).shouldHave(value("false")); //Default button is unchecked for secondary language
    }


    @Test
    @DisplayName("Verify Delete Language in Additional Language screen")
    @Order(2)
    public void verifyDeleteLanguage() {
        //  $(By.xpath("//tbody/tr[1]/td[5]/div[1]/span[1]")).shouldBe(disabled); //Delete button should be disabled for Default language -> Not working??
        $(By.xpath("//tbody/tr[2]/td[5]/div[1]/button[1]/span[1]/span[1]")).should(exist).click(); //Click on Edit button for German - German
        $(By.xpath("//tbody/tr[2]/td[5]/div[1]/button[2]/span[1]")).should(exist).click(); //Click on Cancel Edit button for German - German

        $(By.xpath("//tbody/tr[2]/td[5]/div[1]/button[2]")).should(exist).click(); //Delete button for German - German
        $(By.xpath("//h6[contains(text(),'Are you sure you want to delete this row?')]")).shouldHave(text("Are you sure you want to delete this row?"));
        //Confirmation shown for deletion of secondary langauge

        $(By.xpath("//tbody/tr[2]/td[2]/div[1]/button[2]")).should(exist).click(); //Click on Cancel for Confirmation
        $(By.xpath("//h6[contains(text(),'Are you sure you want to delete this row?')]")).shouldNot(exist); //The confirmation no longer exists

        $(By.xpath("//tbody/tr[2]/td[5]/div[1]/button[2]")).should(exist).click(); //Delete button for German - German
        $(By.xpath("//tbody/tr[2]/td[2]/div[1]/button[1]")).should(exist).click(); //Confirmation button for delete
        $(By.xpath("//tbody/tr[2]/td[5]/div[1]/button[2]")).shouldNot(exist); //German - German is deleted
    }

    @Test
    @DisplayName("Verify Add Language in Additional Language screen")
    @Order(3)
    public void verifyAddLanguage() {
        $(".mtable_toolbar button[title=\"Add\"]").should(exist).click(); //Click on + button
        //   $(By.xpath("//tbody/tr[1]/td[4]/*[1]")).shouldBe(disabled); //The Tick in Default column is disabled -> This is failing ??
        $(By.xpath("//tbody/tr[2]/td[4]/span[1]")).should(exist).shouldNotBe(checked); //The checkbox for newly added Germany-Germany
        $(By.xpath("//tbody/tr[2]/td[1]/div[1]/*[1]")).click(); //Click on Language Dropdown
        $(By.xpath("//body/div[@id='menu-']/div[3]/ul[1]/li[1]")).shouldHave(text("German - Germany")).click();
        //  $(By.xpath("//tbody/tr[2]/td[2]/div[1]/input[1]")).shouldHave(text("Form Title")); //Form Title field after selecting Language -> Not working??
        //    $(By.xpath("//tbody/tr[2]/td[3]/div[1]/input[1]")).shouldHave(text("Form Description")); //Form Description field after selecting Language -> Not working??
        $(By.xpath("//tbody/tr[2]/td[5]/div[1]/button[1]/span[1]/*[1]")).shouldBe(enabled); //Save button/Check is enabled
        $(By.xpath("//tbody/tr[2]/td[4]/span[1]/span[1]/input[1]")).should(exist).shouldNotBe(checked); //Default checkbox is originally unchecked
        $(By.xpath("//tbody/tr[2]/td[4]/span[1]/span[1]/input[1]")).click(); //Check the chekbox for setting as Default language
        // Note: After selecting German as Default language and saving, only English - Great Britain is shown-> ??

        $(By.xpath("//tbody/tr[2]/td[5]/div[1]/button[1]/span[1]/*[1]")).shouldBe(enabled).click(); //Click on Save tick

        $(By.xpath("//tbody/tr[1]/td[5]/div[1]/button[1]/span[1]/span[1]")).should(exist).click(); //Click on the Edit button
        $(By.xpath("//tbody/tr[1]/td[2]/div[1]/input[1]")).shouldBe(enabled); //Form Title is now enabled
        $(By.xpath("//tbody/tr[1]/td[3]/div[1]/input[1]")).shouldBe(enabled); //Form Description is now enabled
        $("#wizard-cancelButton").click(); //Click on Cancel button
        $("#confirmation-dialog-title").should(exist); //Confirmation for Cancellation is shown
        $("#confirmation-dialog-content").should(exist).click(); //Click on Confirm button

    }

}
