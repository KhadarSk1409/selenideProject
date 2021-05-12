package com.vo.mainDashboard;

import com.codeborne.selenide.*;
import com.codeborne.selenide.impl.WebDriverContainer;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.util.List;
import java.lang.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ReuseActions.navigateToFormDashBoardFromFavoriteForms;

import org.openqa.selenium.By;
import utils.ReuseActions;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Form Dashboard components validations")

public class FormDashBoardComponents extends BaseTest {
    @BeforeAll
    @DisplayName("Navigate to Form Dashboard from forms in Favorite Forms")
    public static void navigateFormDashboardFavoritesForms() {
        navigateToFormDashBoardFromFavoriteForms();
    }

    @Test
    @DisplayName("Main components in Form Dashboard")
    @Order(1)
    public void verifyMainComponentsInFormDashboard() {
        $("#gridItemTasks").should(exist);  //My Tasks section should be avaialble
        $("#gridItemTasks button").getAttribute("title").equals("Expand");  //Verify that Expand button is present in My Tasks section
        $("#gridItemUserDataList").should(exist);   //User Data Lists should be available
        $("#gridItemUserDataList button").getAttribute("title").equals("Expand");   //Verify that Expand button is present in User Data Lists section
        ElementsCollection userDataListTabs = $$("#full-width-tab-0");
        System.out.println("The number of tabs in User Data List Tabs are: " + userDataListTabs.size());

        userDataListTabs.get(0).shouldHave(text("MY SUBMISSIONS"));     //User data list section has My Submissions tab
        userDataListTabs.get(1).shouldHave(text("All Submissions"));    //User data list section has All Submissions tab
        userDataListTabs.get(2).shouldHave(text("Data Capture"));       //User data list section has Data capture tab

    }

    @Test
    @DisplayName("Verify Expand/Collapse and layout of Dashboard")
    @Order(2)
    public void verifyExpandCollapseAndLayoutOfDashboard() {
        $("#gridItemTasks button.MuiButtonBase-root.MuiIconButton-root.vo-expand-collapse.collapsed").should(exist).click(); //Click on Expand button in My Tasks section
        $("#gridItemUserDataList").should(disappear); //User Data Lists should disappear
        $("#gridItemTasks button.MuiButtonBase-root.MuiIconButton-root.vo-expand-collapse.expanded").should(exist).click(); //Verify that Collapse button exists and click on that
        $("#gridItemUserDataList").should(appear); //User Data list appears again meaning My Tasks is collapsed again
        $("#full-width-tabpanel-MY_DATA button.MuiButtonBase-root.MuiIconButton-root.vo-expand-collapse.collapsed").should(exist).click(); //Expand button in User Data list section
        $("#gridItemTasks").should(disappear); //My tasks section has disappeared when User Data List section is expanded
        $("#full-width-tabpanel-MY_DATA button.MuiButtonBase-root.MuiIconButton-root.vo-expand-collapse.expanded").should(exist).click(); //Collapse button in User Data list section and click it
        $("#gridItemTasks").should(appear); //My tasks section appears again

    }


    @Test
    @DisplayName("Verify Switching on User Data List Tabs should change visible tables")
    @Order(3)
    public void verifySwitchingOnUserDataListTabs() {
        $("#gridItemUserDataList button.MuiButtonBase-root.MuiTab-root").shouldHave(text("My Submissions")).shouldHave(attribute("aria-selected", "true"));
        $("#userDataListCardTable").shouldBe(visible); //Grid for My Submissions
        $("#gridItemUserDataList button.MuiButtonBase-root.MuiTab-root").shouldHave(value("All Submissions")).should(exist).click();
        $("#dataListCardTable").should(appear); //Grid for All Submissions
        $("#gridItemUserDataList button.MuiButtonBase-root.MuiTab-root").shouldHave(text("Data Capture")).should(exist).click();
        $("#full-width-tabpanel-DATA_CAPTURE table").should(appear); //Grid for Data Capture
    }

}
