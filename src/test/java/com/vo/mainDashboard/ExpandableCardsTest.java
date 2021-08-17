package com.vo.mainDashboard;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.title;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Expandable Cards")
public class ExpandableCardsTest extends BaseTest {

    @Test
    @DisplayName("Should Expand and Collapse My Tasks")
    @Order(1)
    public void shouldExpandAndCollapseMyTasks() {
            $("#usageStatistics").should(appear);
            $("#tasksCard .vo-expand-collapse").click(); //Expands My Tasks
            $("#tasksCard .vo-expand-collapse").should(have(attribute("title", "Collapse")));
            $("#lastUsedListCard").should(disappear);
            $(".fa-compress-arrows-alt").closest("button").should(exist).click(); //Collapse My Tasks
            $("#tasksCard .vo-expand-collapse").should(have(attribute("title", "Expand")));
            $("#tasksCard").should(appear);

    }

    @Test
    @DisplayName("Should Expand and Collapse Recently Worked")
    @Order(2)
    public void shouldExpandAndCollapseRecentlyWorked() {
            $("#lastUsedListCard .vo-expand-collapse").click(); //Expands Last Submissions
            $("#lastUsedListCard .vo-expand-collapse").should(have(attribute("title", "Collapse")));
            $("#lastUsedListCard").should(appear);
            $("#tasksCard").should(disappear);
            $("#lastUsedListCard .vo-expand-collapse").click(); //Collapse Last Submissions
            $("#lastUsedListCard .vo-expand-collapse").should(have(attribute("title", "Expand")));
            $("#tasksCard").should(appear);

    }
    @Test
    @DisplayName("Should Expand and Collapse My Forms")
    @Order(3)
    public void shouldExpandAndCollapseMyForms() {
            $("#navLibrary").should(exist).hover().click(); //Hover and click on Library
            $("#formRelatedTabsCard .vo-expand-collapse").click(); //Expands My Forms
            $("#formRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Collapse")));
            $("#formRelatedTabsCard").should(appear);
            $("#usageStatistics").shouldNot(exist);
            $("#tasksCard").shouldNot(exist);
            $("#lastUsedListCard").shouldNot(exist);
            $("#formRelatedTabsCard .vo-expand-collapse").click(); //Collapse My Forms
            $("#formRelatedTabsCard .vo-expand-collapse").should(have(attribute("title", "Expand")));
            $("#formRelatedTabsCard").should(appear);
            $("#usageStatistics").shouldNot(exist);
            $("#tasksCard").shouldNot(exist);
            $("#lastUsedListCard").shouldNot(exist);
    }
}