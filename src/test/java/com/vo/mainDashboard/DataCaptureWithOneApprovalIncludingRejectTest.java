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
        $(elementLocators("LeftFormDashboardHeader")).should(appear, Duration.ofSeconds(30));
        $(elementLocators("SubMenu")).should(exist).shouldBe(enabled).click();
        $(elementLocators("DataCaptureInSubMenu")).should(exist).shouldHave(Condition.text("Data Capture")).click();
        $(elementLocators("UserSelectionInput")).should(appear);
        $(elementLocators("DropDownButton")).should(exist).click();
        $(elementLocators("Popover")).should(appear);
        $$(elementLocators("ListOfOptions")).shouldHave(itemWithText("GUI Testerguitester@visualorbit.com"), 5000);
        $$(elementLocators("ListOfOptions")).findBy(text("GUI Tester")).click();
        $(elementLocators("UserSelectionInput")).click();
        $(elementLocators("StartDataCaptureButton")).click(); //Start Data Capture Process
        $(elementLocators("ConfirmationMessage")).should(appear)
                .shouldHave(Condition.text("Started Data Capture process for the form: DATA-CAPTURE-WITH-ONE-PROCESS and version 1.0"));
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
        $(elementLocators("FormState")).shouldHave(Condition.text("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $(elementLocators("NewFormID")).should(exist).getAttribute("id");
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("FillForm")).should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $(elementLocators("DataCardActionsPage")).should(appear);
        $(elementLocators("DataContainer")).should(exist);
        $("#textField_form-user-9a32f0df-fe26-4fa1-924a-c8d1eacdac91").should(exist);
        $("#textField_form-user-9a32f0df-fe26-4fa1-924a-c8d1eacdac91").setValue("TEST");
        $(elementLocators("SubmitDataButton")).click();
        $(elementLocators("ConfirmButton")).should(exist).click();
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
        $(elementLocators("FormState")).shouldHave(Condition.text("In Approval")); //Verify the Data Capture state as In Approval
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("ButtonPreview")).should(exist).click(); //Click on Preview
        $(elementLocators("DataCardActionsPage")).should(appear);
        $(elementLocators("RejectButton")).click(); //Click on Reject
        $(elementLocators("RejectReasonInputField")).should(appear).setValue("Form is being Rejected"); //Comment for Rejection
        $(elementLocators("ConfirmButton")).should(exist).click(); //Click on Confirm
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
        $(elementLocators("FormState")).shouldHave(Condition.text("In Progress")); //Verify the Data Capture state
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("FillForm")).should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $(elementLocators("DataContainer")).should(exist);
        $("#textField_form-user-9a32f0df-fe26-4fa1-924a-c8d1eacdac91").should(exist);
        $("#textField_form-user-9a32f0df-fe26-4fa1-924a-c8d1eacdac91").setValue(" Approved ");
        $(elementLocators("SubmitDataButton")).click();
        $(elementLocators("ConfirmButton")).should(exist).click();
        $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
        $(elementLocators("FormState")).shouldHave(Condition.text("In Approval")); //Verify the Data Capture state as In Approval
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("ButtonPreview")).should(exist).click(); //Click on Preview
        $(elementLocators("DataCardActionsPage")).should(appear);
        $(elementLocators("SubmitDataButton")).click();
        $(elementLocators("ConfirmButton")).should(exist).click();
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
        $(elementLocators("FormState")).shouldHave(Condition.text("Completed"));

    }
}