package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture with One Approval including Reject and Different Users")
public class DataCaptureWithOneApprovalIncludingRejectAndDifferentUsersTest extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture with one approval including reject and different users")
    @Order(1)
    public void openFormDashboard() {

        open("/dashboard/D8ipXoYf-");
    }

    @Test
    @DisplayName("Data Capture with One approval and different users should create a Form Fill Task and verify reject process")
    @Order(2)
    public void dataCaptureWithOneApprovalIncludingRejectByTwoUsers() {
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
        open("/dashboard/D8ipXoYf-");
        $("#formDashboardHeaderLeft").should(appear);
        $("#FormDashboardTasksCard .MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far fa-edit']").shouldBe(visible).click();
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-4993983d-0f44-4ae0-a4e7-84b5f550b86f").should(exist);
        $("#textField_form-user-4993983d-0f44-4ae0-a4e7-84b5f550b86f").setValue("TEST");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/D8ipXoYf-");
        $("#formDashboardHeaderLeft").should(appear);
        $("#FormDashboardTasksCard div:nth-child(1) span[iconname='fas fa-times']").shouldBe(visible).shouldBe(enabled).click(); //Click on Reject
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty

        //Should Login as GUI TESTER 01
        shouldLogin(BaseTest.UserType.USER_01);
        open("/dashboard/D8ipXoYf-");
        $("#formDashboardHeaderLeft").should(appear);
        $("#FormDashboardTasksCard .MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far fa-edit']").shouldBe(visible).click();
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-4993983d-0f44-4ae0-a4e7-84b5f550b86f").should(exist);
        $("#textField_form-user-4993983d-0f44-4ae0-a4e7-84b5f550b86f").setValue(" VERIFIED");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").shouldBe(enabled).click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/D8ipXoYf-");
        $("#formDashboardHeaderLeft").should(appear);
        $("#FormDashboardTasksCard div:nth-child(1) span[iconname='fas fa-check']").shouldBe(visible).shouldBe(enabled).click();
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty

        //Should Login as GUI Tester
        shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        open("/dashboard/D8ipXoYf-");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed")); //Verify the Final Data Capture State

    }
}
