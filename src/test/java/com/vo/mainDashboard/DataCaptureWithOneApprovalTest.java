package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.util.function.IntFunction;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exist;
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
    public void dataCaptureWithoutApproval() {
        $("#formDashboardHeaderLeft").should(appear);
        $(".fa-ellipsis-v").closest(("button")).shouldBe(enabled).click();
        $("#optionsMenu ul li:nth-child(3)").should(exist).shouldHave(Condition.text("Data Capture")).click();
        $("#selUser").should(appear);
        $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Testerguitester@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester")).click();
        $("#selUser").click();
        $("#btnStartProcess").click(); //Start Data Capture Process
        $("#gridItemTasks").should(exist);
        $$("#gridItemUserDataList .MuiTab-root").findBy(text("Data Capture")).click();
        $("#tasksCard tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
        $("#gridItemTasks").should(exist);
        $("#FormDashboardTasksCard .MuiCardContent-root").should(exist);
        $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far fa-edit']").shouldBe(visible).click();
        $("#data-card-dialog_actions").should(appear);
        $("#dataContainer").should(exist);
        $("#textField_form-user-cd4b7447-4047-4b50-9495-2fd44a9f2321").should(exist);
        $("#textField_form-user-cd4b7447-4047-4b50-9495-2fd44a9f2321").setValue("TEST");
        $("#btnAcceptTask").click();
        $("#data-approve-reject-dialog").$("#btnConfirm").click();
        $("#tasksCard tr[index='0'] td:nth-of-type(5)").shouldHave(value("In Approval")); //Verify the Data Capture state as In Approval
        $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='fas fa-check']").shouldBe(visible).click(); //Click on Approve
        $("#tasksCard tr[index='0'] td:nth-of-type(5)").shouldHave(value("Completed"));
        $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty

    }
}
