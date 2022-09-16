package com.vo.checklist;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.function.IntFunction;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify Actions on Checklist Overview Table")
public class ChecklistsOverviewActionsTest extends BaseTest {

    @Test
    @DisplayName("Actions on Checklist Overview Table should be enabled or disabled depending on form state")
    @Order(1)
    public void IdentificationOfActions() throws InterruptedException {
        open("/library/checklists");
        Thread.sleep(5000); //Waiting for 5 seconds so that table gets loaded
        SelenideElement table = $(elementLocators("ChecklistTable")).shouldBe(visible);
        ElementsCollection rows = table.$$(elementLocators("FormsAvailableInTable"));

        int rowsSize = rows.size();
        System.out.println("Checklist Count is " + rows.size());

        if (rowsSize == 0) {
            System.out.println("No checklists available");
            return;
        }

        IntFunction<SelenideElement> getRow = (int idx) -> $(".MuiDataGrid-row:nth-of-type(" + idx + ")");

        for (int i = 1; i <= rowsSize; i++) {
            SelenideElement rowEl = getRow.apply(i);

            String checklistState = rowEl.$(elementLocators("FormsStateInTable")).getText();
            String createdBy = rowEl.$(elementLocators("FormCreatedBy")).getText();

            if (checklistState.equals("Published") && (createdBy.equals("GUI Tester"))) {
                rowEl.$(elementLocators("EditChecklistButton")).shouldBe(enabled);
                rowEl.$(elementLocators("openChecklistDashboardButton")).shouldBe(enabled);
            }
            else if (checklistState.equals("Published/in draft") && (createdBy.equals("GUI Tester"))) {
                rowEl.$(elementLocators("EditChecklistButton")).shouldBe(enabled);
                rowEl.$(elementLocators("openChecklistDashboardButton")).shouldBe(enabled);
            }
            else {
                rowEl.$(elementLocators("EditChecklistButton")).shouldBe(enabled);
                rowEl.$(elementLocators("openChecklistDashboardButton")).shouldBe(disabled);
            }
        }
    }

    @Test
    @DisplayName("Navigation actions in Checklist Overview Table")
    @Order(2)
    public void checklistNavigationActions() throws InterruptedException {
        open("/library/checklists");
        SelenideElement table = $(elementLocators("ChecklistTable")).shouldBe(visible);
        Thread.sleep(3000); //Waiting for 5 seconds so that table gets loaded

        ElementsCollection rows = table.$$(elementLocators("FormsAvailableInTable"));
        int rowsSize = rows.size();
        System.out.println("Checklist Count is " + rowsSize);

        if (rowsSize == 0) {
            System.out.println("No checklists available");
            return;
        }

        IntFunction<SelenideElement> getRow = (int idx) -> $(".MuiDataGrid-row:nth-of-type(" + idx + ")");

        for (int i = 1; i <= rowsSize; i++) {
            SelenideElement rowEl = getRow.apply(i);

            String formState = rowEl.$(elementLocators("FormsStateInTable")).getText();
            String createdBy = rowEl.$(elementLocators("FormCreatedBy")).getText();

            if (formState.equals("Published") && (createdBy.equals("GUI Tester"))) {
                rowEl.$(elementLocators("EditChecklistButton")).shouldBe(enabled).click(); //Click on pen icon to open checklist designer
                $(elementLocators("CloseChecklistDesignerButton")).closest("button").should(exist).click();
                $(elementLocators("ChecklistTable")).should(appear);
                rowEl = getRow.apply(i);
                rowEl.$(elementLocators("openChecklistDashboardButton")).shouldBe(enabled).click();
                $(elementLocators("UsageOverview")).should(appear);
                $(elementLocators("GridContainer")).should(appear);
                $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
                $(elementLocators("ChecklistsTab")).should(exist).click();

            }
            else if (formState.equals("Published/in draft") && (createdBy.equals("GUI Tester"))) {
                rowEl.$(elementLocators("EditChecklistButton")).shouldBe(enabled).click(); //Click on pen icon to open checklist designer
                $(elementLocators("CloseChecklistDesignerButton")).closest("button").should(exist).click();
                $(elementLocators("ChecklistTable")).should(appear);
                rowEl = getRow.apply(i);
                rowEl.$(elementLocators("openChecklistDashboardButton")).shouldBe(enabled).click();
                $(elementLocators("UsageOverview")).should(appear);
                $(elementLocators("GridContainer")).should(appear);
                $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
                $(elementLocators("ChecklistsTab")).should(exist).click();

            }
            else{
                rowEl.$(elementLocators("EditChecklistButton")).shouldBe(enabled).click(); //Click on pen icon to open checklist designer
                $(byText("Checklist Flow")).should(appear);
                $(elementLocators("CloseChecklistDesignerButton")).closest("button").should(exist).click();
                $(elementLocators("ChecklistTable")).should(appear);
            }
        }
    }
}
