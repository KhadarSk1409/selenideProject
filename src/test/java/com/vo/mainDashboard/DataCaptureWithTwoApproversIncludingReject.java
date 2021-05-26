package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(("Data Capture with Two Approval including Reject"))

public class DataCaptureWithTwoApproversIncludingReject extends BaseTest {

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
            $("#cUsageOverview").should(exist);
            $(".fa-ellipsis-v").closest(("button")).should(exist).shouldBe(enabled).click();
            $("#optionsMenu ul li:nth-child(3)").should(exist).shouldHave(Condition.text("Data Capture")).should(exist).click();
            $("#selUser").should(appear);
            $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
            $(".MuiAutocomplete-popper").should(appear);
            $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Testerguitester@visualorbit.com"), 5000);
            $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester")).click();
            $("#selUser").click();
            $("#btnStartProcess").click(); //Start Data Capture Process
            $("#gridItemTasks").should(exist);
            $("#cUsageOverview").should(exist);
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1)").should(appear);
            $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
            $("#gridItemTasks").should(exist);
            $("#FormDashboardTasksCard .MuiCardContent-root").should(exist);
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far fa-edit']").shouldBe(visible).click();
            $("#data-card-dialog_actions").should(appear);
            $("#dataContainer").should(exist);
            $("#textField_form-user-8210db0d-bed5-4657-805f-5e5b0c51ac7e").should(exist);
            $("#textField_form-user-8210db0d-bed5-4657-805f-5e5b0c51ac7e").setValue("TEST");
            $("#btnAcceptTask").click();
            $("#data-approve-reject-dialog").$("#btnConfirm").click();
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval")); //Verify the Data Capture state as In Approval
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far far fa-eye']").shouldBe(visible).click(); //First Approval
            $("#data-card-dialog_actions").should(appear).$("#btnAcceptTask").click(); //Click on Accept
            $("#data-approve-reject-dialog").$("#btnConfirm").click();
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1)").shouldBe(visible).shouldHave(text("Approve record"));
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval"));
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far far fa-eye']").click();
            $("#btnRejectDataTask").click(); //Click on Reject
            $("#textfield_RejectReason").should(appear).setValue("Form is being Rejected by Second Approver"); //Comment for Rejection
            $("#data-approve-reject-dialog").$("#btnConfirm").click();
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1)").should(exist);
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far far fa-eye']").shouldBe(visible).click();
            $("#dataContainer").should(exist);
            $("#textField_form-user-8210db0d-bed5-4657-805f-5e5b0c51ac7e").should(exist);
            $("#btnAcceptTask").click(); //Click on Accept
            $("#data-approve-reject-dialog").$("#btnConfirm").click();
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval"));
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1)").should(exist);
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far far fa-eye']").shouldBe(visible).click();
            $("#dataContainer").should(exist);
            $("#textField_form-user-8210db0d-bed5-4657-805f-5e5b0c51ac7e").should(exist);
            $("#btnAcceptTask").click(); //Click on Accept
            $("#data-approve-reject-dialog").$("#btnConfirm").click();
            $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty
            $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("Completed"));

        }
}
