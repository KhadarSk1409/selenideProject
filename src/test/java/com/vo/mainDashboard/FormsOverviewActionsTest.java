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

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Actions on Form Overview Table")
public class FormsOverviewActionsTest extends BaseTest {

    @Test
    @DisplayName("Actions on Form Overview Table should be enabled or disabled depending of form state")
    @Order(1)
    public void IdentificationOfActionsInFormOverviewTable() {
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
                //Fill Form should be enabled
                rowEl.$(".fa-edit").closest("button").shouldBe(enabled);
                //Open form in form designer should be enabled
                rowEl.$(".fa-pen").closest("button").shouldBe(enabled);
                //Open form Dashboard should be enabled
                rowEl.$(".fa-chart-area").closest("button").shouldBe(enabled);
                //Add favorite should be enabled
                rowEl.$(".favorite").closest("button").shouldBe(enabled);
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
        @DisplayName("Navigation Actions in Forms Overview Table")
        @Order(2)
        public void FormsNavigationActions () {
            $("#formRelatedTabsCard .vo-expand-collapse").click();
            $("#formRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Expand")));

            SelenideElement table = $("#formRelatedTabsCard .MuiTableBody-root").shouldBe(visible);
            List<WebElement> rows = table.findElements(By.tagName("tr"));
            System.out.println(" Form Count is " + rows.size());
            if (rows.size() >= 0) {
            for (int i = 2; i <= rows.size(); i++) {
                String Forms = $("#formListTable tr:nth-child(" + i + ") td:nth-child(3) div span").getText();
                String createdBy = $("#formListTable tr:nth-child(" + i + ") td:nth-child(4)").getText();

                if (Forms.equals("Published") && createdBy.equals("GUI Tester")) {
                    $("#formListTable tr:nth-child(" + i + ") td:nth-child(7) span:nth-child(1) span.MuiIconButton-label span.MuiIconButton-label span").shouldBe(enabled).click();
                    //Click on Fill form
                    $("#cDataCardActions").shouldBe(visible);
                    $("#cDataCardActions").$("#btnCloseDataFillForm").click();
                    $("#formListTable tr:nth-child(" + i + ") td:nth-child(7) span:nth-child(2) span.MuiIconButton-label span.MuiIconButton-label span").shouldBe(enabled).click();
                    //Click on Open form in form designer
                    $("#formtree_card").shouldBe(visible).shouldHave(text("Form structure"));
                    $("#toDashboard").click();
                    $("#formListTable tr:nth-child(" + i + ") td:nth-child(7) span:nth-child(3) span.MuiIconButton-label span.MuiIconButton-label span").shouldBe(enabled).click();
                    //Click on Open form dashboard
                    $("#cUsageOverview").should(appear);
                    //$("#formDashboardHeaderLeft").should(appear).$("#btnCreateNewData").shouldBe(enabled);
                    $("#toDashboard").click();
                } else if (Forms.equals("Published/in draft") && createdBy.equals("GUI Tester")) {
                    $("#formListTable tr:nth-child(" + i + ") td:nth-child(7) span:nth-child(1) span.MuiIconButton-label span.MuiIconButton-label span").shouldBe(enabled).click();
                    $("#cDataCardActions").shouldBe(visible);
                    $("#cDataCardActions").$("#btnCloseDataFillForm").click();
                    $("#formListTable tr:nth-child(" + i + ") td:nth-child(7) span:nth-child(2) span.MuiIconButton-label span.MuiIconButton-label span").shouldBe(enabled).click();
                    $("#formtree_card").shouldBe(visible).shouldHave(text("Form structure"));
                    $("#toDashboard").click();
                    $("#formListTable tr:nth-child(" + i + ") td:nth-child(7) span:nth-child(3) span.MuiIconButton-label span.MuiIconButton-label span").shouldBe(enabled).click();
                    $("#cUsageOverview").should(appear);
                    $("#toDashboard").click();
                } else {
                    $("#formListTable tr:nth-child(" + i + ") td:nth-child(7) span:nth-child(2) button span div button").shouldBe(enabled).click();
                    //Click Open form in form designer
                    $("#formtree_card").shouldBe(visible).shouldHave(text("Form structure"));
                    $("#toDashboard").click();
                }
            }
        }
                else{
                    System.out.println("No Forms available");
                }
    }
}
