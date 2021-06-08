package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture with Two Approval and Different Users")
public class DataCaptureWithTwoApprovalAndDifferentUsers extends BaseTest {

    @Test
    @DisplayName("Open TA Two Approval and Different Users Form")
    @Order(1)
    public void openFormDashboard() {

        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
    }

    @Test
    @DisplayName("Data Capture with Two Approval should create a Form Fill Task with Different Users")
    @Order(2)
    public void dataCaptureProcessWithTwoApprovalByDifferentUsers() {
        $("#formDashboardHeaderLeft").should(appear);
        $(".fa-ellipsis-v").closest(("button")).should(exist).shouldBe(enabled).click();
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
        shouldLogin(BaseTest.UserType.USER_01);
        $("#tasksCard tbody td:nth-child(1)").should(exist); //A Task should be visible in MY TASKS
        $("#tasksCard tbody td:nth-child(6)").should(exist).click();
        $("#dashboard-data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").setValue("TEST");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        $("#bpmRelatedTabsCard").should(exist);
        $("#tasksCard tbody td:nth-child(1)").should(exist); //A Task should be visible in MY TASKS
        $("#tasksCard tbody tr td:nth-child(6) span:nth-child(2) span[iconname='fas fa-check'").shouldBe(visible).shouldBe(enabled).click();
        $("#tasksCard div:nth-child(2) p").shouldBe(visible); //My Tasks Should be empty

        //Should Login as GUI TESTER 03
        shouldLogin(UserType.USER_03);
        $("#bpmRelatedTabsCard").should(exist);
        $("#tasksCard tbody td:nth-child(1)").should(exist); //A Task should be visible in MY TASKS
        $("#tasksCard tbody tr td:nth-child(6) span:nth-child(2) span[iconname='fas fa-check'").shouldBe(visible).shouldBe(enabled).click();
        $("#tasksCard div:nth-child(2) p").shouldBe(visible); //My Tasks Should be empty

        //Should Login as GUI Tester
        shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        open("/dashboard/ASdii60Gt");
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed"));

    }
}
