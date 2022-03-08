package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.elementLocators;

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
            $(elementLocators("LeftFormDashboardHeader")).should(appear, Duration.ofSeconds(30));
            $(elementLocators("SubMenu")).should(exist).shouldBe(enabled).click();
            $(elementLocators("DataCaptureInSubMenu")).should(exist).shouldHave(Condition.text("Data Capture")).click();
            $(elementLocators("UserSelectionInput")).should(appear);
            $(elementLocators("DropDownButton")).should(exist).click();
            $(elementLocators("Popover")).should(appear, Duration.ofSeconds(30));
            $$(elementLocators("ListOfOptions")).findBy(text("GUI Tester")).should(appear, Duration.ofSeconds(30)).click();
            $(elementLocators("UserSelectionInput")).click();
            $(elementLocators("StartDataCaptureButton")).click(); //Start Data Capture Process
            $(elementLocators("ConfirmationMessage")).should(appear)
                    .shouldHave(Condition.text("Started Data Capture process for the form: DATA-CAPTURE-WITH-TWO-PROCESS and version 3.0"));
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
            $(elementLocators("FormState")).shouldHave(Condition.text("In Progress")); //Verify the Data Capture state
            String formDataCaptureId= $(elementLocators("NewFormID")).should(exist).getAttribute("id");
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
            $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(elementLocators("FillForm")).should(appear, Duration.ofSeconds(10)).click();
            $(elementLocators("DataCardActionsPage")).should(appear);
            $(elementLocators("DataContainer")).should(exist);
            $("#textField_form-user-343baf17-ff5e-42db-a382-77df0216a7f3").should(exist);
            $("#textField_form-user-343baf17-ff5e-42db-a382-77df0216a7f3").setValue("TEST");
            $(elementLocators("SubmitDataButton")).click(); //Click on Accept
            $(elementLocators("ConfirmButton")).should(exist).click(); //Click on Confirm
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
            $(elementLocators("FormState")).shouldHave(Condition.text("In Approval")); //Verify the Data Capture state as In Approval
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
            $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(elementLocators("ButtonPreview")).should(appear, Duration.ofSeconds(15)).click(); //Click on Preview
            $(elementLocators("DataCardActionsPage")).should(appear);
            $(elementLocators("SubmitDataButton")).click(); //Click on Accept
            $(elementLocators("ConfirmButton")).should(exist).click(); //Click on Confirm
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
            $(elementLocators("FormState")).shouldHave(Condition.text("In Approval"));
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
            $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(elementLocators("ButtonPreview")).should(appear, Duration.ofSeconds(15)).click(); //Click on Preview
            $(elementLocators("DataCardActionsPage")).should(appear);
            $(elementLocators("RejectButton")).click(); //Click on Reject
            $(elementLocators("RejectReasonInputField")).should(appear).setValue("Form is being Rejected"); //Comment for Rejection
            $(elementLocators("ConfirmButton")).should(exist).click();
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
            $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(elementLocators("ButtonPreview")).should(appear, Duration.ofSeconds(15)).click(); //Click on Preview
            $(elementLocators("DataContainer")).should(exist);
            $("#textField_form-user-343baf17-ff5e-42db-a382-77df0216a7f3").should(exist);
            $(elementLocators("SubmitDataButton")).click(); //Click on Accept
            $(elementLocators("ConfirmButton")).should(exist).click(); //Click on Confirm
            $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
            $(elementLocators("FormState")).shouldHave(Condition.text("In Approval"));
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
            $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId ))
                    .should(exist).$(elementLocators("ButtonPreview")).should(appear, Duration.ofSeconds(15)).click(); //Click on Preview
            $(elementLocators("DataContainer")).should(exist);
            $("#textField_form-user-343baf17-ff5e-42db-a382-77df0216a7f3").should(exist);
            $(elementLocators("SubmitDataButton")).click(); //Click on Accept
            $(elementLocators("ConfirmButton")).should(exist).click(); //Click on Confirm
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
            $(elementLocators("FormState")).shouldHave(Condition.text("Completed")); //Verify the Final Data Capture State

    }
}
