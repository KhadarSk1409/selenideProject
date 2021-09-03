package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture with One Approval including Reject and Different Users")
public class DataCaptureWithOneApprovalIncludingRejectAndDifferentUsersTest extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture with one approval including reject and different users")
    @Order(1)
    public void openFormDashboard() {

        open("/dashboard/DataCapture-OneApproval-Including-Reject-Different-Users");
    }

    @Test
    @DisplayName("Data Capture with One approval and different users should create a Form Fill Task and verify reject process")
    @Order(2)
    public void dataCaptureWithOneApprovalIncludingRejectByTwoUsers() {
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
                .shouldHave(Condition.text("Started Data Capture process for the form: DataCapture-OneApproval-Including-Reject-Different-Users and version 1.0"));
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $("#tasksCard tbody tr:nth-of-type(2)").should(exist).getAttribute("id");

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/DataCapture-OneApproval-Including-Reject-Different-Users");
        $("#formDashboardHeaderLeft").should(appear);
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonFillForm").should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $("#dataContainer").should(exist);
        $("#textField_form-user-a634c0ef-35ad-4259-9540-764f1e9030da").should(exist);
        $("#textField_form-user-a634c0ef-35ad-4259-9540-764f1e9030da").setValue("TEST");
        $("#btnAcceptTask").should(exist).click();
        $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click(); //Click on Confirm

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/DataCapture-OneApproval-Including-Reject-Different-Users");
        $("#formDashboardHeaderLeft").should(appear);
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonPreview").should(exist).click(); //Click on Preview
        $("#data-card-dialog_actions").should(appear).$("#btnRejectDataTask").click(); //Click on Reject
        $("#textfield_RejectReason").should(appear).setValue("Form is being Rejected"); //Comment for Rejection
        $("#data-approve-reject-dialog").$("#btnConfirm").should(exist).click(); //Click on Confirm

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/DataCapture-OneApproval-Including-Reject-Different-Users");
        $("#formDashboardHeaderLeft").should(appear);
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonFillForm").should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-a634c0ef-35ad-4259-9540-764f1e9030da").should(exist);
        $("#textField_form-user-a634c0ef-35ad-4259-9540-764f1e9030da").setValue(" VERIFIED");
        $("#btnAcceptTask").should(exist).click();
        $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/DataCapture-OneApproval-Including-Reject-Different-Users");
        $("#formDashboardHeaderLeft").should(appear);
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonPreview").waitUntil(appears,10000).click(); //Click on Preview
        $("#data-card-dialog_actions").should(appear).$("#btnAcceptTask").click(); //Click on Accept
        $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click();

        //Should Login as GUI Tester
        shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        open("/dashboard/DataCapture-OneApproval-Including-Reject-Different-Users");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed")); //Verify the Final Data Capture State

    }
}
