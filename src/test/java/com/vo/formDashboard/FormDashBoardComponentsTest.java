package com.vo.formDashboard;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.navigateToFormDashBoardFromFavoriteForms;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Form Dashboard components validations")

public class FormDashBoardComponentsTest extends BaseTest {
    @BeforeAll
    @DisplayName("Open Sample Form")
    public static void navigateFormDashboardFavoritesForms() {
        open("/dashboard/sample-form");
    }

    @Test
    @DisplayName("Main components in Form Dashboard")
    @Order(1)
    public void verifyMainComponentsInFormDashboard() {
        $("#gridItemTasks").should(exist);  //My Tasks section should be avaialble
        $("#gridItemTasks button").shouldHave(attribute("title", "Expand"));  //Verify that Expand button is present in My Tasks section
        $("#gridItemUserDataList").should(exist);   //User Data Lists should be available
        $("#gridItemUserDataList").find(byAttribute("title", "Expand")).should(exist);   //Verify that Expand button is present in User Data Lists section
        $$("#full-width-tab-0").shouldHave(texts("MY SUBMISSIONS", "All Submissions", "Data Capture")); //User data lists contain these three tabs
    }

    @Test
    @DisplayName("Verify Expand/Collapse and layout of Dashboard")
    @Order(2)
    public void verifyExpandCollapseAndLayoutOfDashboard() {
        $("#gridItemTasks .vo-expand-collapse").should(exist).click(); //Click on Expand button in My Tasks section
        $("#gridItemUserDataList").should(disappear); //User Data Lists should disappear
        $("#gridItemTasks .vo-expand-collapse").should(exist).click(); //Verify that Collapse button exists and click on that
        $("#gridItemUserDataList").should(appear); //User Data list appears again meaning My Tasks is collapsed again
        $("#full-width-tabpanel-MY_DATA .vo-expand-collapse").should(exist).click(); //Expand button in User Data list section
        $("#gridItemTasks").should(disappear); //My tasks section has disappeared when User Data List section is expanded
        $("#full-width-tabpanel-MY_DATA .vo-expand-collapse").should(exist).click(); //Collapse button in User Data list section and click it
        $("#gridItemTasks").should(appear); //My tasks section appears again

    }

    @Test
    @DisplayName("Verify Switching on User Data List Tabs should change visible tables")
    @Order(3)
    public void verifySwitchingOnUserDataListTabs() {
        $$("#gridItemUserDataList .MuiTab-root").shouldHave(texts("My Submissions", "All Submissions", "Data Capture"));
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("My Submissions")).shouldHave(cssClass("Mui-selected"));
        $("#userDataListCardTable").shouldBe(visible); //Grid for My Submissions

        $$("#gridItemUserDataList .MuiTab-root").findBy(text("All Submissions")).click();
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("All Submissions")).shouldHave(cssClass("Mui-selected"));
        $("#dataListCardTable").should(appear); //Grid for My Submissions

        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).shouldHave(cssClass("Mui-selected"));
        $("#full-width-tabpanel-DATA_CAPTURE").should(appear); //Grid for Data Capture
    }

}
