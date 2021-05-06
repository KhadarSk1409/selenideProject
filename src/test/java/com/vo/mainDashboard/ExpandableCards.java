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
            $("#bpmRelatedTabsCard").should(appear);
            $("#usageStatistics").should(disappear);
            $("#platformNews").should(disappear);
            $("#lastUsedListCard").should(disappear);
            $("#formRelatedTabsCard").should(disappear);
            $("#bpmRelatedTabsCard .vo-expand-collapse").click(); //Collapse My Tasks
            $("#bpmRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Expand")));
            $("#bpmRelatedTabsCard").should(appear);
            $("#usageStatistics").should(appear);
            $("#platformNews").should(appear);
            $("#lastUsedListCard").should(appear);
            $("#formRelatedTabsCard").should(appear);
    }

    @Test
    @DisplayName("Should Expand and Collapse Last Submissions")
    @Order(2)
    public void shouldExpandAndCollapseLastSubmissions() {
            $("#lastUsedListCard .vo-expand-collapse").click(); //Expands Last Submissions
            $("#lastUsedListCard .vo-expand-collapse").should(have(attribute("title", "Collapse")));
            $("#lastUsedListCard").should(appear);
            $("#bpmRelatedTabsCard").should(disappear);
            $("#formRelatedTabsCard").should(disappear);
            $("#usageStatistics").should(disappear);
            $("#platformNews").should(disappear);
            $("#lastUsedListCard .vo-expand-collapse").click(); //Collapse Last Submissions
            $("#lastUsedListCard .vo-expand-collapse").should(have(attribute("title", "Expand")));
            $("#bpmRelatedTabsCard").should(appear);
            $("#usageStatistics").should(appear);
            $("#platformNews").should(appear);
            $("#lastUsedListCard").should(appear);
            $("#formRelatedTabsCard").should(appear);
    }

    @Test
    @DisplayName("Should Expand and Collapse My Forms")
    @Order(3)
    public void shouldExpandAndCollapseMyForms() {
            $("#formRelatedTabsCard .vo-expand-collapse").click(); //Expands My Forms
            $("#formRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Collapse")));
            $("#formRelatedTabsCard").should(appear);
            $("#usageStatistics").should(disappear);
            $("#platformNews").should(disappear);
            $("#bpmRelatedTabsCard").should(disappear);
            $("#lastUsedListCard").should(disappear);
            $("#formRelatedTabsCard .vo-expand-collapse").click(); //Collapse My Forms
            $("#formRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Expand")));
            $("#bpmRelatedTabsCard").should(appear);
            $("#usageStatistics").should(appear);
            $("#platformNews").should(appear);
            $("#lastUsedListCard").should(appear);
            $("#formRelatedTabsCard").should(appear);
    }
}