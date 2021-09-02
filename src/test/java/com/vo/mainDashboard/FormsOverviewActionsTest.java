package com.vo.mainDashboard;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Actions on Form Overview Table")
public class FormsOverviewActionsTest extends BaseTest {

    @Test
    @DisplayName("Actions on Form Overview Table should be enabled or disabled depending on form state")
    @Order(1)
    public void IdentificationOfActionsInFormOverviewTable() {
        $("#navLibrary").should(exist).hover().click(); //Hover and click on Library
        $("#tabDataCapture").should(exist).hover();
        SelenideElement table = $("#formRelatedTabsCard .MuiTableBody-root").shouldBe(visible);
        ElementsCollection rows = table.$$("tr");
        System.out.println(" Form Count is " + rows.size());

        if (rows.size() == 0) {
            System.out.println("No Forms available");
            return;
        }

        rows.forEach(rowEl -> {
            String formState = rowEl.$("td:nth-child(3)").getText();
            String createdBy = rowEl.$("td:nth-child(4)").getText();

            if (formState.equals("Published") && createdBy.equals("GUI Tester")) {
                rowEl.$(".fa-edit").closest("button").shouldBe(enabled);
                //Fill Form should be enabled
                rowEl.$(".fa-pen").closest("button").shouldBe(enabled);
                //Open form in form designer should be enabled
                rowEl.$(".fa-chart-area").closest("button").shouldBe(enabled);
                //Open form Dashboard should be enabled
                rowEl.$(".favorite").closest("button").shouldBe(enabled);
                //Add favorite should be enabled
            } else if (formState.equals("Published/in draft") && createdBy.equals("GUI Tester")) {
                rowEl.$(".fa-edit").closest("button").shouldBe(enabled);
                rowEl.$(".fa-pen").closest("button").shouldBe(enabled);
                rowEl.$(".fa-chart-area").closest("button").shouldBe(enabled);
                rowEl.$(".favorite").closest("button").shouldBe(enabled);
            } else {
                rowEl.$(".fa-edit").closest("button").shouldBe(disabled);
                rowEl.$(".fa-pen").closest("button").shouldBe(enabled);
                rowEl.$(".fa-chart-area").closest("button").shouldBe(disabled);
                rowEl.$(".favorite").shouldNotBe(visible);
            }
        });
    }

    @Test
    @DisplayName("Navigation Actions in Form Overview Table")
    @Order(2)
    public void formsNavigationActions() {
        $("#navLibrary").should(exist).hover().click(); //Hover and click on Library
        $("#tabDataCapture").should(exist).hover();
        SelenideElement table = $("#formRelatedTabsCard .MuiTableBody-root").shouldBe(visible);

        ElementsCollection rows = table.$$("tr");
        int rowsSize = rows.size();
        System.out.println(" Form Count is " + rowsSize);

        if (rowsSize == 0) {
            System.out.println("No Forms available");
            return;
        }

        IntFunction<SelenideElement> getRow = (int idx) -> $("#formListTable tbody tr:nth-of-type(" + idx + ")");

        for (int i = 1; i <= rowsSize; i++) {
            SelenideElement rowEl = getRow.apply(i);

            String formState = rowEl.$("td:nth-child(3)").getText();
            String createdBy = rowEl.$("td:nth-child(4)").getText();

            if (formState.equals("Published") && createdBy.equals("GUI Tester")) {
                rowEl.$(".fa-edit").closest("button").shouldBe(enabled).click(); //Click on Fill form
                $("#cDataCardActions").shouldBe(visible);
                $("#cDataCardActions").$("#btnCloseDataFillForm").click();
                $("#formListTable").should(appear);
                rowEl.$(".fa-pen").closest("button").shouldBe(enabled).click();//Click on Open form in form designer
                $("#formtree_card").shouldBe(visible).shouldHave(text("Form structure"));
                $("#navLibrary").should(exist).hover().click(); //Hover and click on Library
                $("#formListTable").should(appear);
                rowEl = getRow.apply(i);
                rowEl.$(".fa-chart-area").closest("button").shouldBe(enabled).click();//Click on Open form dashboard
                $("#navLibrary").should(exist).hover().click(); //Hover and click on Library

            } else if (formState.equals("Published/in draft") && createdBy.equals("GUI Tester")) {
                rowEl.$(".fa-edit").closest("button").shouldBe(enabled).click(); //Click on Fill form
                $("#cDataCardActions").shouldBe(visible);
                $("#cDataCardActions").$("#btnCloseDataFillForm").click();
                $("#formListTable").should(appear);
                rowEl.$(".fa-pen").closest("button").shouldBe(enabled).click();//Click on Open form in form designer
                $("#formtree_card").shouldBe(visible).shouldHave(text("Form structure"));
                $("#navLibrary").should(exist).hover().click(); //Hover and click on Library
                $("#formListTable").should(appear);
                rowEl = getRow.apply(i);
                rowEl.$(".fa-chart-area").closest("button").shouldBe(enabled).click();//Click on Open form dashboard
                $("#navLibrary").should(exist).hover().click(); //Hover and click on Library

            } else {
                rowEl.$(".fa-pen").closest("button").shouldBe(enabled).click();//Click on Open form in form designer
                $("#formtree_card").shouldBe(visible).shouldHave(text("Form structure"));
                $("#navLibrary").should(exist).hover().click(); //Hover and click on Library

            }
        }
    }
}

