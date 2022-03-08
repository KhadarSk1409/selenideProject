package com.vo.mainDashboard;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.util.function.IntFunction;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Actions on Form Overview Table")
public class FormsOverviewActionsTest extends BaseTest {

    @Test
    @DisplayName("Actions on Form Overview Table should be enabled or disabled depending on form state")
    @Order(1)
    public void IdentificationOfActionsInFormOverviewTable() throws InterruptedException {
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("DataCapture")).should(exist).hover();
        Thread.sleep(5000); //Waiting for 5 seconds so that table gets loaded
        SelenideElement table = $(elementLocators("FormsList")).shouldBe(visible);
        ElementsCollection rows = table.$$(elementLocators("FormsAvailableInTable"));
        System.out.println(" Form Count is " + rows.size());

        if (rows.size() == 0) {
            System.out.println("No Forms available");
            return;
        }

        rows.forEach(rowEl -> {
            String formState = null;
            formState = rowEl.$(elementLocators("FormsStateInTable")).getText();
            String createdBy = rowEl.$(elementLocators("FormCreatedBy")).getText();

            if (formState.equals("Published") && createdBy.equals("GUI Tester")) {
                rowEl.$(elementLocators("FillForm")).shouldBe(enabled);
                //Fill Form should be enabled
                rowEl.$(elementLocators("OpenFormInFormDesignerButton")).shouldBe(enabled);
                //Open form in form designer should be enabled
                rowEl.$(elementLocators("OpenFormInFormDashboard")).closest("button").shouldBe(enabled);
                //Open form Dashboard should be enabled
                rowEl.$(elementLocators("AddFormToFavoritesButton")).shouldBe(enabled);
                //Add favorite should be enabled
            } else if (formState.equals("Published/in draft") && createdBy.equals("GUI Tester")) {
                rowEl.$(elementLocators("FillForm")).shouldBe(enabled);
                rowEl.$(elementLocators("OpenFormInFormDesignerButton")).shouldBe(enabled);
                rowEl.$(elementLocators("OpenFormInFormDashboard")).closest("button").shouldBe(enabled);
                rowEl.$(elementLocators("AddFormToFavoritesButton")).shouldBe(enabled);
            } else {
                rowEl.$(elementLocators("FillForm")).shouldBe(disabled);
                rowEl.$(elementLocators("OpenFormInFormDesignerButton")).shouldBe(enabled);
                rowEl.$(elementLocators("OpenFormInFormDashboard")).closest("button").shouldBe(disabled);
                rowEl.$(elementLocators("AddFormToFavoritesButton")).shouldNotBe(visible);
            }
        });
    }

    @Test
    @DisplayName("Navigation Actions in Form Overview Table")
    @Order(2)
    public void formsNavigationActions() {
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("DataCapture")).should(exist).hover();
        SelenideElement table = $(elementLocators("FormsList")).shouldBe(visible);

        ElementsCollection rows = table.$$(elementLocators("FormsAvailableInTable"));
        int rowsSize = rows.size();
        System.out.println(" Form Count is " + rowsSize);

        if (rowsSize == 0) {
            System.out.println("No Forms available");
            return;
        }

        IntFunction<SelenideElement> getRow = (int idx) -> $("#formListTable .MuiDataGrid-row:nth-of-type(" + idx + ")");

        for (int i = 1; i <= rowsSize; i++) {
            SelenideElement rowEl = getRow.apply(i);

            String formState = rowEl.$(elementLocators("FormsStateInTable")).getText();
            String createdBy = rowEl.$(elementLocators("FormCreatedBy")).getText();

            if (formState.equals("Published") && createdBy.equals("GUI Tester")) {
                rowEl.$(elementLocators("FillForm")).shouldBe(enabled).click(); //Click on Fill form
                $(elementLocators("DataFillForm")).shouldBe(visible);
                $(elementLocators("ButtonCloseDataFillForm")).click();
                $(elementLocators("FormsList")).should(appear);
                rowEl.$(elementLocators("OpenFormInFormDesignerButton")).shouldBe(enabled).click();//Click on Open form in form designer
                $(elementLocators("FormStructure")).shouldBe(visible).shouldHave(text("Form structure"));
                $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
                $(elementLocators("FormsList")).should(appear);
                rowEl = getRow.apply(i);
                rowEl.$(elementLocators("OpenFormInFormDashboard")).closest("button").shouldBe(enabled).click();//Click on Open form dashboard
                $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library

            } else if (formState.equals("Published/in draft") && createdBy.equals("GUI Tester")) {
                rowEl.$(elementLocators("FillForm")).shouldBe(enabled).click(); //Click on Fill form
                $(elementLocators("DataFillForm")).shouldBe(visible);
                $(elementLocators("ButtonCloseDataFillForm")).click();
                $(elementLocators("FormsList")).should(appear);
                rowEl.$(elementLocators("OpenFormInFormDesignerButton")).shouldBe(enabled).click();//Click on Open form in form designer
                $(elementLocators("FormStructure")).shouldBe(visible).shouldHave(text("Form structure"));
                $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
                $(elementLocators("FormsList")).should(appear);
                rowEl = getRow.apply(i);
                rowEl.$(elementLocators("OpenFormInFormDashboard")).closest("button").shouldBe(enabled).click();//Click on Open form dashboard
                $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library

            } else {
                rowEl.$(elementLocators("OpenFormInFormDesignerButton")).shouldBe(enabled).click();//Click on Open form in form designer
                $(elementLocators("FormStructure")).shouldBe(visible).shouldHave(text("Form structure"));
                $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library

            }
        }
    }
}