package com.vo.mainDashboard;

import com.codeborne.selenide.*;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.lang.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        String getFormName = $("#formDashboardHeaderLeft h6").should(exist).getText();
        $("#toDashboard").should(exist).click(); //Click on Launchpad
        $("#btnCreateForm").should(exist); //Verify that Create form button is available
        $("#formListTable table tbody tr").should(exist);
        ElementsCollection formRows = $$("#formListTable table tbody tr"); //Fetch form rows
        int formRowsSize = formRows.size();
        for (int i = 0; i < formRowsSize; i++) {
            String formName = formRows.get(i).$(byCssSelector("td:nth-child(2)")).getText(); //Select form name for each row
            System.out.println("formName : " + formName);

            if (formName.contentEquals(getFormName)) {
                formRows.get(i).$(byCssSelector("td:nth-child(1)")).click(); //Click on '>' Open Form Dashboard
                formRows.get(i).$(byCssSelector("button .fa-chart-area")).should(exist).click();
               // formRows.get(i).$(byCssSelector("td:nth-child(7) span:nth-child(3)")).should(exist).click();
                $("#full-width-tabpanel-MY_DATA").should(exist); //Navigated to Form Dashboard
                $("#formDashboardHeaderLeft h6").should(exist).getText().contentEquals(getFormName); //Navigated to the Form Dashboard for the form
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
        $("#formDashboardHeaderLeft div:nth-child(2) button span.MuiIconButton-label").should(exist); //FORM INFORMATION “i”
        $("#btnCreateNewData").should(exist); //Fill form button
        $("#btnEditFormDesign").should(exist); //Edit form button
        $("#btnNotificationPopover").should(exist); //Notifications
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist); //Menu option should be present
    }

    @Test
    @DisplayName("Validate Form Data Card")
    @Order(4)
    public void validateTheFormDataCard() {
        $("#formDashboardHeaderLeft button").should(exist).click(); //The icon i for Data Card
        $("#cUsageOverview").should(exist); //Data card is displayed
        String expectedUrl = url(); //Get the url of the browser
        $("#cUsageOverview p i").should(exist).click(); //Click on copy url button in the Data card
        String clipBoardUrl = Selenide.clipboard().getText();
        assertTrue(clipBoardUrl.equals(expectedUrl));
    }

    @Test
    @DisplayName("Verify the button fill form")
    @Order(5)
    public void verifyButtonFillForm() {
        $("body").click();
        $("#btnCreateNewData").click(); //Click Fill form
        $("#cDataCardActions").should(exist); //Fill form screen
        $("#btnCloseDataFillForm").should(exist).click(); //Click on x and close the Fill form screen
        $("#btnCreateNewData").should(exist); //Fill form button is available
    }

    @Test
    @DisplayName("Verify Edit Form Design button")
    @Order(6)
    public void verifyEditFormDesignButton() {
        $("#btnEditFormDesign").should(exist).click(); //Edit Form Design
        $("#btnFormDesignPublish").should(exist); //Publish button to ensure user has navigated to Edit Form screen
        $("#btnCloseEditForm").should(exist).click(); //Click on x on Edit Form design
        $("#btnCloseEditForm").should(disappear);
        $("#btnEditFormDesign").should(exist); //Edit Form Design to ensure that user has navigated back to Form Dashboard
    }

    @Test
    @DisplayName("Verify button notifications")
    @Order(7)
    public void verifyButtonNotifications() {
        $("#btnNotificationPopover").should(exist).click(); //Notifications icon
        $("#notificationsCard").should(appear).click(); //Notifications card and click on it
        $("#notificationsCard").should(appear); //Notifications card still exists
        $("body").click(); //?? Not wokring - user needs to click on the Form Dashboard page and come back
        $("#notificationsCard").should(disappear);
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(appear); //Menu button to ensure that user has navigated back to Form Dashboard
    }

    @Test
    @DisplayName("Verify Menu items for Form Dashboard")
    @Order(8)
    public void verifyMenuItemsForFormDashboard() {
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).click();
        $("#optionsMenu ul li:nth-child(1)").should(exist).shouldHave(Condition.text("PDF"));
        $("#optionsMenu ul li:nth-child(2)").should(exist).shouldHave(Condition.text("Edit Form Permissions"));
        $("#optionsMenu ul li:nth-child(3)").should(exist).shouldHave(Condition.text("Data Capture"));
        $("#optionsMenu ul li:nth-child(4)").should(exist).shouldHave(Condition.text("Visualize form design changes in data tables"));
        $("#optionsMenu ul li:nth-child(5)").should(exist).shouldHave(Condition.text("Copy Form Dataset URL to Clipboard"));

    }

}
