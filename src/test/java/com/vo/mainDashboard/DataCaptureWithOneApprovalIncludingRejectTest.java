package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture with One Approval including Reject")
    public class DataCaptureWithOneApprovalIncludingRejectTest extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture with one approval")
    @Order(1)
    public void openFormDashboard() {

        open("/dashboard/DATA-CAPTURE-WITH-ONE-PROCESS");
    }

    @Test
    @DisplayName("Data Capture with One approval should create a Form Fill Task and verify reject process")
    @Order(2)
    public void dataCaptureWithOneApprovalAndReject() {
        $("#formDashboardHeaderLeft").should(appear);
        $(".fa-ellipsis-v").closest(("button")).shouldBe(enabled).click();
        $("#optionsMenu ul li:nth-child(3)").should(exist).shouldHave(Condition.text("Data Capture")).click();
        $("#selUser").should(appear);
        $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Testerguitester@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester")).click();
        $("#selUser").should(exist).click();
        $("#btnStartProcess").should(exist).click(); //Start Data Capture Process
        $("#gridItemTasks").should(exist);
        $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1)").should(appear);
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).should(exist).click();
        $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $("#tasksCard tbody tr:nth-of-type(2)").should(exist).getAttribute("id");
        $("#gridItemTasks").should(exist);
        $("#FormDashboardTasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                .should(exist).$(".fa-edit").closest("button").should(exist).click();
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-cd4b7447-4047-4b50-9495-2fd44a9f2321").should(exist);
        $("#textField_form-user-cd4b7447-4047-4b50-9495-2fd44a9f2321").setValue("TEST"); //Enter some text in the Text field
        $("#btnAcceptTask").should(exist).click();
        $("#data-approve-reject-dialog").$("#btnConfirm").should(exist).click();
        $("#FormDashboardTasksCard .MuiCardContent-root").should(exist);
        $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval"));
        $("#FormDashboardTasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                .should(exist).$(".fa-eye").closest("button").should(exist).click();
        $("#data-card-dialog_actions").should(appear).$("#btnRejectDataTask").click(); //Click on Reject
        $("#textfield_RejectReason").should(appear).setValue("Form is being Rejected"); //Comment for Rejection
        $("#data-approve-reject-dialog").$("#btnConfirm").should(exist).click();
        $("#FormDashboardTasksCard .MuiCardContent-root").should(exist);
        $("#FormDashboardTasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                .should(exist).$(".fa-edit").should(exist).closest("button").click();
        $("#dataContainer").should(exist);
        $("#textField_form-user-cd4b7447-4047-4b50-9495-2fd44a9f2321").should(exist);
        $("#textField_form-user-cd4b7447-4047-4b50-9495-2fd44a9f2321").setValue(" Approved ");
        $("#btnAcceptTask").should(exist).click();
        $("#selUser").should(exist);
        $("#data-approve-reject-dialog").$("#btnConfirm").should(exist).click();
        $("#FormDashboardTasksCard .MuiCardContent-root").should(exist);
        $("#FormDashboardTasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId ))
                .should(exist).$(".fa-eye").closest("button").should(exist).click();
        $("#btnAcceptTask").should(exist).click();
        $("#selUser").should(exist);
        $("#data-approve-reject-dialog").$("#btnConfirm").should(exist).click();
        $("#FormDashboardTasksCard .MuiCardContent-root").should(exist);
        $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("Completed"));

    }
}