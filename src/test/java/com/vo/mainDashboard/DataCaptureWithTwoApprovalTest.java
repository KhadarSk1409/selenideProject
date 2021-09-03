package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture with Two Approval")
public class DataCaptureWithTwoApprovalTest extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture with Two approval")
    @Order(1)
    public void openFormDashboard(){

        open("/dashboard/DATA-CAPTURE-WITH-TWO-PROCESS");
    }

    @Test
    @DisplayName("Data Capture with Two approval should create a Form Fill Task")
    @Order(2)
    public void dataCaptureWithTwoApproval() throws InterruptedException {
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).shouldBe(enabled).click();
        $("#optionsMenu ul li:nth-child(4)").should(exist).shouldHave(Condition.text("Data Capture")).click();
        $("#selUser").should(appear);
        $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Testerguitester@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester")).click();
        $("#selUser").should(exist).click();
        $("#btnStartProcess").should(exist).click(); //Start Data Capture Process
        $("#client-snackbar").should(appear)
                .shouldHave(Condition.text("Started Data Capture process for the form: DATA-CAPTURE-WITH-TWO-PROCESS and version 1.0"));
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $("#tasksCard tbody tr:nth-of-type(2)").should(exist).getAttribute("id");
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonFillForm").should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-8210db0d-bed5-4657-805f-5e5b0c51ac7e").should(exist);
        $("#textField_form-user-8210db0d-bed5-4657-805f-5e5b0c51ac7e").setValue("TEST");
        $("#btnAcceptTask").should(exist).click();
        $("#data-approve-reject-dialog").$("#btnConfirm").should(exist).click();
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval")); //Verify the Data Capture state as In Approval

        //First Approval
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonQuickApprove").should(exist).click(); //Click on quick approve
        Thread.sleep(2000);
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(disappear);
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval"));

        //Second Approval
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonQuickApprove").should(exist).click(); //Click on quick approve
        Thread.sleep(2000);
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).shouldNot(exist);
        //Verifying final state of the form
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("Completed"));

    }
}
