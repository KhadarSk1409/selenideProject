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
@DisplayName("Data Capture with Two Approval including Reject with Different Users")
public class DataCaptureWithTwoApprovalIncludingRejectandDifferentUsersTest extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture with two approval including reject and different users")
    @Order(1)
    public void openFormDashboard(){
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
    }

    @Test
    @DisplayName("Data Capture with Two Approvals including Address Rejection and Final Approval with Different Users")
    @Order(2)
    public void dataCaptureWithTwoApprovalIncludingRejectByTwoUsers() {
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).shouldBe(enabled).click();
        $("#optionsMenu ul li:nth-child(4)").should(exist).shouldHave(Condition.text("Data Capture")).click();
        $("#selUser").should(appear);
        $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Tester 01guitester01@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester 01")).click(); //Click on GUI Tester 01
        $("#selUser").click();
        $("#btnStartProcess").click(); //Start Data Capture Process
        $("#client-snackbar").should(appear)
                .shouldHave(Condition.text("Started Data Capture process for the form: TA-TWO-APPROVAL-DIFF-USERS and version 2.0"));
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $("#tasksCard tbody tr:nth-of-type(2)").should(exist).getAttribute("id");

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonFillForm").should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").setValue("TEST");
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
                .$(".buttonPreview").should(exist).click(); //Click on Preview
        $("#data-card-dialog_actions").should(appear);
        $("#btnRejectDataTask").should(exist).click(); //Click on Reject
        $("#textfield_RejectReason").should(appear).setValue("Form is being Rejected by Second Approver"); //Comment for Rejection
        $("#data-approve-reject-dialog").$("#btnConfirm").click();

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonFillForm").should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").setValue(" VERIFIED");
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
                .$(".buttonPreview").should(exist).click(); //Click on Preview
        $("#data-card-dialog_actions").should(appear);
        $("#btnAcceptTask").should(exist).click(); //Click on Accept
        $("#data-approve-reject-dialog").$("#btnConfirm").should(exist).click();

        //Should Login as GUI Tester
        shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed")); //Verify the Final Data Capture State
    }
}
