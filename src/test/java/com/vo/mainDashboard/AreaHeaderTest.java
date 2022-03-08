package com.vo.mainDashboard;

import com.codeborne.selenide.*;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.lang.*;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActions.navigateToFormDashBoardFromFavoriteForms;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Area Header validations")
public class AreaHeaderTest extends BaseTest {

    @Test
    @DisplayName("Navigate to Form Dashboard from Favorites")
    @Order(1)
    public void navigateToFormDashboardFromFavorites() {
        navigateToFormDashBoardFromFavoriteForms();

    }

    @Test
    @DisplayName("Navigate to Form Dashboard from Form Table")
    @Order(2)
    public void navigateToDashboardFormTable() {
        String getFormName = $(elementLocators("FormNameOnFormDashboardHeader")).should(exist).getText();
        System.out.println(getFormName);
        $(elementLocators("Launchpad")).should(exist).click(); //Click on Launchpad
        $(elementLocators("NavigateToLibrary")).should(exist).click();
        $(elementLocators("CreateNewFormButton")).should(exist); //Verify that Create form button is available
        $(elementLocators("Body")).click();
        $(elementLocators("FormsList")).should(exist);
        ElementsCollection formRows = $$(elementLocators("FormsAvailableInTable")); //Fetch form rows
        int formRowsSize = formRows.size();
        for (int i = 0; i < formRowsSize; i++) {
            String formName = formRows.get(i).$(byCssSelector(elementLocators("formName"))).getText(); //Select form name for each row
            System.out.println("formName : " + formName);

            if (formName.contentEquals(getFormName)) {
                formRows.get(i).$(byCssSelector(elementLocators("OpenFormInFormDashboardButton"))).should(exist).click();
                $(elementLocators("FormDashboardData")).should(exist); //Navigated to Form Dashboard
                $(elementLocators("FormNameOnFormDashboardHeader")).should(exist).getText().contentEquals(getFormName); //Navigated to the Form Dashboard for the form
            break;
            }

        }

    }

    @Test
    @DisplayName("Verify UI elements on the form Dashboard")
    @Order(3)
    public void verifyUIelementsOnFormDashboard() {
        //Following should be available for the user FORM INFORMATION “i”,
        // BUTTON FILL FORM, BUTTON EDIT FORM DESIGN, ICON/BUTTON NOTIFICATIONS and a MENU
        $(elementLocators("FormInfoButton")).should(exist); //FORM INFORMATION “i”
        $(elementLocators("FillFormButton")).should(exist); //Fill form button
        $(elementLocators("NotificationPopover")).should(exist); //Notifications
        $(elementLocators("SubMenu")).should(exist); //Menu option should be present
    }

    @Test
    @DisplayName("Validate Form Data Card")
    @Order(4)
    public void validateTheFormDataCard() {
        $(elementLocators("FormInfoButton")).should(exist).click(); //The icon i for Data Card
        $(elementLocators("UsageOverview")).should(exist); //Data card is displayed
        String expectedUrl = url(); //Get the url of the browser
        $(elementLocators("FormLinkCopyIcon")).should(exist).click(); //Click on copy url button in the Data card
        $("#notistack-snackbar").should(appear).shouldHave(text("Copied"));
        String clipBoardUrl = Selenide.clipboard().getText();
        assertEquals(clipBoardUrl, expectedUrl, "Expected: " + expectedUrl + " Received: " + clipBoardUrl);
    }

    @Test
    @DisplayName("Verify the button fill form")
    @Order(5)
    public void verifyButtonFillForm() {
        $(elementLocators("Body")).click();
        $(elementLocators("FillFormButton")).click(); //Click Fill form
        $(elementLocators("DataCardActions")).should(exist); //Fill form screen
        $(elementLocators("ButtonCloseDataFillForm")).should(exist).click(); //Click on x and close the Fill form screen
        $(elementLocators("FillFormButton")).should(exist); //Fill form button is available
    }

    @Test
    @DisplayName("Verify Edit Form Design button")
    @Order(6)
    public void verifyEditFormDesignButton() {
        $(elementLocators("SubMenu")).should(exist, Duration.ofSeconds(10)).click();
        $(elementLocators("EditFormDesignInSubMenu")).should(exist).click(); //Edit Form Design
        $(elementLocators("PublishButton")).should(exist); //Publish button to ensure user has navigated to Edit Form screen
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch initial version
        $(elementLocators("CloseEditFormButton")).shouldBe(enabled).click(); //Click on x on Edit Form design
        $(elementLocators("SubMenu")).should(exist, Duration.ofSeconds(10)).click();
        $(elementLocators("EditFormDesignInSubMenu")).should(exist).click(); //Edit Form Design
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version has increased
        $(elementLocators("CloseEditFormButton")).shouldBe(enabled).click(); //Click on x on Edit Form design
        $(elementLocators("InitialVersion")).shouldNot(exist);
        $(elementLocators("CloseEditFormButton")).shouldNot(exist);
        $(elementLocators("EditFormDesignInSubMenu")).shouldNot(exist); //Ensure that user has navigated back to Form Dashboard
    }

    @Test
    @DisplayName("Verify button notifications")
    @Order(7)
    public void verifyButtonNotifications() {
        $(elementLocators("NotificationPopover")).should(exist).click(); //Notifications icon
        $(elementLocators("MyNotifications")).should(appear).click(); //Notifications card and click on it
        $(elementLocators("MyNotifications")).should(appear); //Notifications card still exists
        $(elementLocators("Body")).click(); //?? Not wokring - user needs to click on the Form Dashboard page and come back
        $(elementLocators("MyNotifications")).should(disappear);
        $(elementLocators("LeftFormDashboardHeader")).should(appear); //Menu button to ensure that user has navigated back to Form Dashboard
    }

    @Test
    @DisplayName("Verify Menu items for Form Dashboard")
    @Order(8)
    public void verifyMenuItemsForFormDashboard() {
        open("/dashboard/Sample_Form");
        $(elementLocators("SubMenu")).should(exist).click();
        $$(elementLocators("SubMenuList")).shouldHaveSize(6)
                .shouldHave(CollectionCondition.texts("Edit Form Design", "PDF", "Edit Form Permissions", "Data Capture", "Visualize form design changes in data tables", "Copy Form Dataset URL to Clipboard" ));

    }

}
