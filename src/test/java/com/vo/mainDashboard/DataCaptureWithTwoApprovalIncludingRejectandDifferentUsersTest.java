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

        $(elementLocators("LeftFormDashboardHeader")).should(appear, Duration.ofSeconds(30));
        $(elementLocators("SubMenu")).should(exist).shouldBe(enabled).click();
        $(elementLocators("DataCaptureInSubMenu")).should(exist).shouldHave(Condition.text("Data Capture")).click();
        $(elementLocators("UserSelectionInput")).should(appear);
        $(elementLocators("DropDownButton")).should(exist).click();
        $(elementLocators("Popover")).should(appear);
        $$(elementLocators("ListOfOptions")).shouldHave(itemWithText("GUI Tester 01guitester01@visualorbit.com"), Duration.ofSeconds(30));
        $$(elementLocators("ListOfOptions")).findBy(text("GUI Tester 01")).click(); //Click on GUI Tester 01
        $(elementLocators("UserSelectionInput")).click();
        $(elementLocators("StartDataCaptureButton")).click(); //Start Data Capture Process
        $(elementLocators("ConfirmationMessage")).should(appear)
                .shouldHave(Condition.text("Started Data Capture process for the form: TA-TWO-APPROVAL-DIFF-USERS and version 2.0"));
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
        $(elementLocators("FormState")).shouldHave(Condition.text("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $(elementLocators("NewFormID")).should(exist).getAttribute("id");

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $(elementLocators("LeftFormDashboardHeader")).should(appear, Duration.ofSeconds(30));
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("FillForm")).should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $(elementLocators("DataCardActionsPage")).should(appear);
        $(elementLocators("DataContainer")).should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").setValue("TEST");
        $(elementLocators("SubmitDataButton")).click();
        $(elementLocators("ConfirmButton")).should(exist).click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $(elementLocators("LeftFormDashboardHeader")).should(appear, Duration.ofSeconds(30));
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("QuickApprove")).should(exist).click(); //Click on quick approve
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(disappear);

        //Should Login as GUI TESTER 03
        shouldLogin(UserType.USER_03);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $(elementLocators("LeftFormDashboardHeader")).should(appear);
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("ButtonPreview")).should(exist).click(); //Click on Preview
        $(elementLocators("DataCardActionsPage")).should(appear);
        $(elementLocators("RejectButton")).click(); //Click on Reject
        $(elementLocators("RejectReasonInputField")).should(appear).setValue("Form is being Rejected by Second Approver"); //Comment for Rejection
        $(elementLocators("ConfirmButton")).should(exist).click();

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $(elementLocators("LeftFormDashboardHeader")).should(appear);
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("FillForm")).should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $(elementLocators("DataCardActionsPage")).should(appear);
        $(elementLocators("DataContainer")).should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").should(exist);
        $("#textField_form-user-530d85bf-490a-4c54-8c13-fc3571b46a46").setValue(" VERIFIED");
        $(elementLocators("SubmitDataButton")).click();
        $(elementLocators("ConfirmButton")).should(exist).click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $(elementLocators("LeftFormDashboardHeader")).should(appear);
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("QuickApprove")).should(exist).click(); //Click on quick approve
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(disappear);

        //Should Login as GUI TESTER 03
        shouldLogin(UserType.USER_03);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $(elementLocators("LeftFormDashboardHeader")).should(appear);
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("ButtonPreview")).should(exist).click(); //Click on Preview
        $(elementLocators("DataCardActionsPage")).should(appear);
        $(elementLocators("SubmitDataButton")).click();
        $(elementLocators("ConfirmButton")).should(exist).click();

        //Should Login as GUI Tester
        shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        open("/dashboard/TA-TWO-APPROVAL-DIFF-USERS");
        $(elementLocators("LeftFormDashboardHeader")).should(exist);
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
        $(elementLocators("FormState")).shouldHave(Condition.text("Completed")); //Verify the Final Data Capture State
    }
}
