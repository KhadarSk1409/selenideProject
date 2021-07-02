package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture with Two Approval and Different Users")
public class DataCaptureWithTwoApprovalAndDifferentUsersTest extends BaseTest {

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
        $("#selUser").should(exist).click();
        $("#btnStartProcess").should(exist).click(); //Start Data Capture Process
        $("#gridItemTasks").should(exist);
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $("#tasksCard tbody tr:nth-of-type(2)").should(exist).getAttribute("id");

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#formDashboardHeaderLeft").should(appear);
        $("#FormDashboardTasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                .should(exist).$(".fa-edit").closest("button").should(exist).click();
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").setValue("TEST");//Enter Some Text
        $("#btnAcceptTask").should(exist).click();
        $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#formDashboardHeaderLeft").should(appear);
        $("#FormDashboardTasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                .should(exist).$(".fa-check").closest("button").should(exist).click();

        //Should Login as GUI TESTER 03
        shouldLogin(UserType.USER_03);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#formDashboardHeaderLeft").should(appear);
        $("#FormDashboardTasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                .should(exist).$(".fa-check").closest("button").should(exist).click();

        //Should Login as GUI Tester
        shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#formDashboardHeaderLeft").should(appear);
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed")); //Verify the Final Data Capture State

    }
}
