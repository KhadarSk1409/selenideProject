package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.util.function.IntFunction;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture with One Approval")
public class DataCaptureWithOneApprovalTest extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture with one approval")
    @Order(1)
    public void openFormDashboard(){

        open("/dashboard/DATA-CAPTURE-WITH-ONE-PROCESS");
    }

    @Test
    @DisplayName("Data Capture with One approval should create a Form Fill Task")
    @Order(2)
    public void dataCaptureWithOneApproval() {
        $("#formDashboardHeaderLeft").should(appear);
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).shouldBe(enabled).click();
        $("#optionsMenu ul li:nth-child(4)").should(exist).shouldHave(Condition.text("Data Capture")).click();
        $("#selUser").should(appear);
        $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Testerguitester@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester")).click();
        $("#selUser").click();
        $("#btnStartProcess").click(); //Start Data Capture Process
        $("#client-snackbar").should(appear)
                .shouldHave(Condition.text("Started Data Capture process for the form: DATA-CAPTURE-WITH-ONE-PROCESS and version 1.0"));
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $("#tasksCard tbody tr:nth-of-type(2)").should(exist).getAttribute("id");
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonFillForm").should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-789e1199-464b-4ee2-8afc-50f678947fa7").should(exist);
        $("#textField_form-user-789e1199-464b-4ee2-8afc-50f678947fa7").setValue("TEST");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").click();
        $("#gridItemUserDataList").should(exist);
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval")); //Verify the Data Capture state as In Approval
        $("#gridItemUserDataList").should(exist);
        $("#tabMyTasks").should(exist).click(); //Click on My Tasks
        $("#tasksCard").find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(".buttonQuickApprove").waitUntil(appears,10000).click(); //Click on Fill Form
        $("#tabDataCapture").should(exist).click(); //Click on Data Capture
        $("#tasksCard tbody tr[index='0'] td:nth-of-type(5)").shouldHave(value("Completed"));

    }
}
