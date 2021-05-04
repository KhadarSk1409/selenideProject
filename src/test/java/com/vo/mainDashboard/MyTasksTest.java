package com.vo.mainDashboard;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Expand and Collapse My Tasks")
public class MyTasksTest extends BaseTest {

    @Test
    @DisplayName("Should Expand My Tasks")
    @Order(1)
    public void expandMyTasks() {
            $("#tasksCard").$(byAttribute("title", "Expand")).click(); //Expands My Tasks
            $("#formRelatedTabs").shouldBe(visible).shouldHave(text("Priority")); //Verify whether My Tasks is expanded and Priority is visible or not
            $("#usageStatistics").shouldNot(exist); //Task Statistics should not exist
            $("#platformNews").shouldNot(exist); //Platform News should not exist

    }

    @Test
    @DisplayName("Should Collapse My Tasks")
    @Order(2)
    public void collapseMyTasks() {
            $("#tasksCard").$(byAttribute("title", "Collapse")).click(); //COllapse My Tasks
            $("#usageStatistics").should(exist); //Verify whether Task Statistics is visible or not
            $("#platformNews").should(exist); //Verify whether Platform News is visible or not

    }
}
