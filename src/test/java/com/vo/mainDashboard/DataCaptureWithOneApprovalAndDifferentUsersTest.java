package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.vo.BaseTest.shouldLogin;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture with One Approval and Different Users")
public class DataCaptureWithOneApprovalAndDifferentUsersTest extends BaseTest {

        @Test
        @DisplayName("Open At Form Approval with One Approver")
        @Order(1)
        public void openFormDashboard() {

            open("/dashboard/ASdii60Gt");
        }

    @Test
    @DisplayName("Data Capture with One Approval should create a Form Fill Task with Different Users")
    @Order(2)
    public void dataCaptureProcessWithOneApprovalByDifferentUsers() {
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
        open("/dashboard/ASdii60Gt");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard .MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far fa-edit']").shouldBe(visible).click();
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-fbea34a0-bf35-45eb-9f42-d586230f9cf6").should(exist);
        $("#textField_form-user-fbea34a0-bf35-45eb-9f42-d586230f9cf6").setValue("TEST");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").click();

        //Should Login as GUI TESTER 02
        shouldLogin(BaseTest.UserType.USER_02);
        open("/dashboard/ASdii60Gt");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard .MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='fas fa-check']").shouldBe(visible).click();
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty

        //Should Login as GUI Tester
        shouldLogin(BaseTest.UserType.MAIN_TEST_USER);
        open("/dashboard/ASdii60Gt");
        $("#formDashboardHeaderAppBar").should(exist);
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed"));

    }
}
