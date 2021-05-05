package com.vo.mainDashboard;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Expandable Cards")
public class ExpandableCards extends BaseTest {

    @Test
    @DisplayName("Should Expand and Collapse My Tasks")
    @Order(1)
    public void shouldExpandAndCollapseMyTasks() {
            $("#bpmRelatedTabsCard .vo-expand-collapse").click(); //Expands My Tasks
            $("#bpmRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Collapse")));
            $("#usageStatistics").shouldNot(exist); //Task Statistics should not exist
            $("#platformNews").shouldNot(exist); //Platform News should not exist
            $("#bpmRelatedTabsCard .vo-expand-collapse").click(); //Collapse My Tasks
            $("#bpmRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Expand")));
            $("#platformNews").should(exist); //Verify whether Platform News is visible or not
    }

    @Test
    @DisplayName("Should Expand and Collapse Last Submissions")
    @Order(2)
    public void shouldExpandAndCollapseLastSubmissions() {
            $("#lastUsedListCard .vo-expand-collapse").click(); //Expands Last Submissions
            $("#lastUsedListCard .vo-expand-collapse").should(have(attribute("title", "Collapse")));
            $("#bpmRelatedTabsCard").shouldNot(exist); //My Tasks should not exist
            $("#formRelatedTabsCard").shouldNot(exist); //My Forms should not exist
            $("#usageStatistics").shouldNot(exist); //Task Statistics should not exist
            $("#platformNews").shouldNot(exist); //Platform News should not exist
            $("#lastUsedListCard .vo-expand-collapse").click(); //Collapse My Forms
            $("#lastUsedListCard .vo-expand-collapse").should(have(attribute("title", "Expand")));
            $("#bpmRelatedTabsCard").should(exist); //My Tasks should be visible
            $("#usageStatistics").should(exist); //Verify whether Task Statistics is visible or not
            $("#platformNews").should(exist); //Verify whether Platform News is visible or not
    }

    @Test
    @DisplayName("Should Expand and Collapse My Forms")
    @Order(3)
    public void shouldExpandAndCollapseMyForms() {
            $("#formRelatedTabsCard .vo-expand-collapse").click(); //Expands My Forms
            $("#formRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Collapse")));
            $("#usageStatistics").shouldNot(exist); //Task Statistics should not exist
            $("#platformNews").shouldNot(exist); //Platform News should not exist
            $("#formRelatedTabsCard .vo-expand-collapse").click(); //Collapse My Forms
            $("#formRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Expand")));
            $("#bpmRelatedTabsCard").should(exist); //My Tasks should be visible
            $("#usageStatistics").should(exist); //Verify whether Task Statistics is visible or not
            $("#platformNews").should(exist); //Verify whether Platform News is visible or not
    }
}