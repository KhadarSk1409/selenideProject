package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture without Approval by Two Users")
public class DataCaptureWithoutApprovalByTwoUsers extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture without approval")
    @Order(1)
    public void openFormDashboard() {

        open("/dashboard/DATA-CAPTURE-WO-PROCESS");
    }

    @Test
    @DisplayName("Data Capture without approval should create a Form Fill Task with Two Users")
    @Order(2)
    public void dataCaptureWithoutApproval() {

        $("#formDashboardHeaderLeft").should(appear);
        $(".fa-ellipsis-v").closest(("button")).shouldBe(enabled).click();
        $("#optionsMenu ul li:nth-child(3)").should(exist).shouldHave(Condition.text("Data Capture")).click();
        $("#selUser").should(appear);
        $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Tester 01guitester01@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester 01")).click(); //Click on GUI Tester 01
        $("#selUser").click();
        $("#btnStartProcess").click(); //Start Data Capture Process
        $("#gridItemTasks").should(exist);
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("In Progress")); //Verify the Data Capture state



        //Should Login as GUI TESTER 01

        shouldLogin(UserType.USER_01);

        $("#tasksCard tbody td:nth-child(1)").should(exist); //A Task should be visible in MY TASKS
        $("#tasksCard tbody td:nth-child(6)").should(exist).click();
        $("dashboard-data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").should(exist);
        $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").setValue("TEST");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").click();
        $("#tasksCard .voEmptySpaceFiller").shouldBe(visible); //MY TASKS should be Empty

        //Should Login as GUI Tester

        shouldLogin(UserType.MAIN_TEST_USER);

        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty
        $("div[role='tablist'] button:nth-child(3)").click();
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed"));

    }
}
