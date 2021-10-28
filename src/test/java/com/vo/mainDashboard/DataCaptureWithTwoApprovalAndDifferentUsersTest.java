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
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).shouldBe(enabled).click();
        $("#optionsMenu ul li:nth-child(4)").should(exist).shouldHave(Condition.text("Data Capture")).click();
        $("#selUser").should(appear);
        $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Tester 01guitester01@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester 01")).click(); //Click on GUI Tester 01
        $("#selUser").should(exist).click();
        $("#btnStartProcess").should(exist).click(); //Start Data Capture Process
        $("#client-snackbar").should(appear)
                .shouldHave(Condition.text("Started Data Capture process for the form: TA-TWO-APPROVAL-DIFF-USERS and version 2.0"));
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard .MuiChip-label:nth-child(1)").shouldHave(Condition.text("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $("#tasksCard .MuiCardContent-root .MuiDataGrid-main div:nth-child(2) div:nth-child(8) div").should(exist).getAttribute("id");

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#formDashboardHeaderLeft").should(appear);
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonFillForm").should(exist).shouldBe(enabled).click(); //Click on Fill Form
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
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonQuickApprove").should(exist).click(); //Click on quick approve
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(disappear);

        //Should Login as GUI TESTER 03
        shouldLogin(UserType.USER_03);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#formDashboardHeaderLeft").should(appear);
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonQuickApprove").should(exist).click(); //Click on quick approve
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(disappear);

        //Should Login as GUI Tester
        shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#formDashboardHeaderLeft").should(appear);
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard .MuiChip-label:nth-child(1)").shouldHave(Condition.text("Completed")); //Verify the Final Data Capture State

    }
}
