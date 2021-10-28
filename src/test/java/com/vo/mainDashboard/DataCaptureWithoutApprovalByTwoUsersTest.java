package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture without Approval by Two Users")
public class DataCaptureWithoutApprovalByTwoUsersTest extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture without approval")
    @Order(1)
    public void openFormDashboard() {

        open("/dashboard/DATA-CAPTURE-WO-PROCESS");
    }

    @Test
    @DisplayName("Data Capture without approval should create a Form Fill Task with Two Users")
    @Order(2)
    public void dataCaptureProcessWithoutApproval() {

        $("#formDashboardHeaderLeft").should(appear);
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
                .shouldHave(Condition.text("Started Data Capture process for the form: DATA-CAPTURE-WO-PROCESS and version 1.0"));
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard .MuiChip-label:nth-child(1)").shouldHave(Condition.text("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $("#tasksCard .MuiCardContent-root .MuiDataGrid-main div:nth-child(2) div:nth-child(8) div").should(exist).getAttribute("id");

        //Should Login as GUI TESTER 01
        shouldLogin(UserType.USER_01);
        open("/dashboard/DATA-CAPTURE-WO-PROCESS");
        $("#formDashboardHeaderLeft").should(appear);
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonFillForm").should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").should(exist);
        $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").setValue("TEST");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").click();

        //Should Login as GUI Tester
        shouldLogin(UserType.MAIN_TEST_USER);
        open("/dashboard/DATA-CAPTURE-WO-PROCESS");
        $("#formDashboardHeaderLeft").should(appear);
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard .MuiChip-label:nth-child(1)").shouldHave(Condition.text("Completed"));

    }
}
