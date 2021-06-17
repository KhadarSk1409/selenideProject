package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture with Two Approval including Reject with Different Users")
public class DataCaptureWithTwoApprovalIncludingRejectandDifferentUsersTest extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture with two approval including reject and different users")
    @Order(1)
    public void openFormDashboard(){
        open("/dashboard/sflEpUvhO");
    }

    @Test
    @DisplayName("Data Capture with Two Approvals including Address Rejection and Final Approval with Different Users")
    @Order(2)
    public void dataCaptureWithTwoApprovalIncludingRejectByTwoUsers() {
        $("#formDashboardHeaderLeft").should(appear);
        $(".fa-ellipsis-v").closest(("button")).shouldBe(enabled).click();
        $("#optionsMenu ul li:nth-child(3)").should(exist).shouldHave(Condition.text("Data Capture")).click();
        $("#selUser").should(appear);
        $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Tester 01guitester01@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester 01")).click(); //Click on GUI Tester 01
        $("#selUser").click();
        $("#btnStartProcess").click(); //Start Data Capture Process
        $("#gridItemTasks").should(exist);
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("In Progress")); //Verify the Data Capture state

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/sflEpUvhO");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard .MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far fa-edit']").shouldBe(visible).click();
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-65d469f8-0db9-45a4-90f2-ca983c738d75").should(exist);
        $("#textField_form-user-65d469f8-0db9-45a4-90f2-ca983c738d75").setValue("TEST");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/sflEpUvhO");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard div:nth-child(1) span[iconname='fas fa-check']").shouldBe(visible).shouldBe(enabled).click(); //Click on Reject
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty

        //Should Login as GUI TESTER 03
        shouldLogin(UserType.USER_03);
        open("/dashboard/sflEpUvhO");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard div:nth-child(1) span[iconname='far far fa-eye']").shouldBe(visible).shouldBe(enabled).click(); //Click on Reject
        $("#btnRejectDataTask").click(); //Click on Reject
        $("#textfield_RejectReason").should(appear).setValue("Form is being Rejected by Second Approver"); //Comment for Rejection
        $("#data-approve-reject-dialog").$("#btnConfirm").click();
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/sflEpUvhO");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard .MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far fa-edit']").shouldBe(visible).click();
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-65d469f8-0db9-45a4-90f2-ca983c738d75").should(exist);
        $("#textField_form-user-65d469f8-0db9-45a4-90f2-ca983c738d75").setValue(" VERIFIED");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/sflEpUvhO");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard div:nth-child(1) span[iconname='fas fa-check']").shouldBe(visible).shouldBe(enabled).click(); //Click on Reject
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty

        //Should Login as GUI TESTER 03
        shouldLogin(UserType.USER_03);
        open("/dashboard/sflEpUvhO");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard div:nth-child(1) span[iconname='far far fa-eye']").shouldBe(visible).shouldBe(enabled).click(); //Click on Reject
        $("#btnAcceptTask").click(); //Click on Accept
        $("#data-approve-reject-dialog").$("#btnConfirm").click();
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty

        //Should Login as GUI Tester
        shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        open("/dashboard/sflEpUvhO");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed")); //Verify the Final Data Capture State
    }
}
