package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(("Data Capture with Two Approval including Reject"))

public class DataCaptureWithTwoApproversIncludingRejectTest extends BaseTest {

    @Test
    @DisplayName(("Open Data Capture with Two Approval"))
    @Order(1)
    public  void openFormDashboard(){
        open("/dashboard/DATA-CAPTURE-WITH-TWO-PROCESS");
    }

        @Test
        @DisplayName("Data Capture with Two Approvals including Address Rejection and Final Approval")
        @Order(2)
        public void dataCaptureWithTwoApprovalIncludingReject() {
            $("#formDashboardHeaderLeft").should(appear);
            $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).shouldBe(enabled).click();
            $("#optionsMenu ul li:nth-child(4)").should(exist).shouldHave(Condition.text("Data Capture")).should(exist).click();
            $("#selUser").should(appear);
            $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
            $(".MuiAutocomplete-popper").should(appear);
            $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Testerguitester@visualorbit.com"), 5000);
            $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester")).click();
            $("#selUser").click();
            $("#btnStartProcess").click(); //Start Data Capture Process
            $("#client-snackbar").should(appear)
                    .shouldHave(Condition.text("Started Data Capture process for the form: DATA-CAPTURE-WITH-TWO-PROCESS and version 1.0"));
            $("#gridItemUserDataList").should(exist);
            $("#tabDataCapture").should(exist).click(); //Click on Data Capture
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
            String formDataCaptureId= $("#tasksCard tbody tr:nth-of-type(2)").should(exist).getAttribute("id");
            $("#gridItemUserDataList").should(exist);
            $("#tabMyTasks").should(exist).click(); //Click on My Tasks
            $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(".buttonFillForm").waitUntil(appears,10000).click();
            $("#data-card-dialog_actions").should(appear);
            $("#dataContainer").should(exist);
            $("#textField_form-user-343baf17-ff5e-42db-a382-77df0216a7f3").should(exist);
            $("#textField_form-user-343baf17-ff5e-42db-a382-77df0216a7f3").setValue("TEST");
            $("#btnAcceptTask").should(exist).click();
            $("#data-approve-reject-dialog").$("#btnConfirm").should(exist).click();
            $("#gridItemUserDataList").should(exist);
            $("#tabDataCapture").should(exist).click(); //Click on Data Capture
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval")); //Verify the Data Capture state as In Approval
            $("#gridItemUserDataList").should(exist);
            $("#tabMyTasks").should(exist).click(); //Click on My Tasks
            $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(".buttonPreview").waitUntil(appears,15000).click(); //Click on Preview
            $("#data-card-dialog_actions").should(appear).$("#btnAcceptTask").click(); //Click on Accept
            $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click();
            $("#gridItemUserDataList").should(exist);
            $("#tabDataCapture").should(exist).click(); //Click on Data Capture
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval"));
            $("#gridItemUserDataList").should(exist);
            $("#tabMyTasks").should(exist).click(); //Click on My Tasks
            $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(".buttonPreview").waitUntil(appears,15000).click(); //Click on Preview
            $("#data-card-dialog_actions").should(appear).$("#btnRejectDataTask").click(); //Click on Reject
            $("#textfield_RejectReason").should(appear).setValue("Form is being Rejected"); //Comment for Rejection
            $("#data-approve-reject-dialog").$("#btnConfirm").should(exist).click(); //Click on Confirm
            $("#gridItemUserDataList").should(exist);
            $("#tabMyTasks").should(exist).click(); //Click on My Tasks
            $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(".buttonPreview").waitUntil(appears,15000).click(); //Click on Preview
            $("#dataContainer").should(exist);
            $("#textField_form-user-343baf17-ff5e-42db-a382-77df0216a7f3").should(exist);
            //$("#textField_form-user-343baf17-ff5e-42db-a382-77df0216a7f3").setValue(" Verified");
            $("#btnAcceptTask").should(exist).click(); //Click on Accept
            $("#data-approve-reject-dialog").$("#btnConfirm").click();
            $("#tabDataCapture").should(exist).click(); //Click on Data Capture
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval"));
            $("#gridItemUserDataList").should(exist);
            $("#tabMyTasks").should(exist).click(); //Click on My Tasks
            $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(".buttonPreview").waitUntil(appears,15000).click(); //Click on Preview
            $("#dataContainer").should(exist);
            $("#textField_form-user-343baf17-ff5e-42db-a382-77df0216a7f3").should(exist);
            $("#btnAcceptTask").should(exist).click(); //Click on Accept
            $("#data-approve-reject-dialog").$("#btnConfirm").click();
            $("#gridItemUserDataList").should(exist);
            $("#tabDataCapture").should(exist).click(); //Click on Data Capture
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").should(exist).shouldHave(value("Completed"));

    }
}
