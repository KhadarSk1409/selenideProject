package com.vo.mainDashboard;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.conditions.Attribute;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.util.function.IntFunction;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture without Approval")
public class DataCaptureWithoutApprovalTest extends BaseTest {

    @Test
    @DisplayName("Open the Data Capture without approval")
    @Order(1)
    public void openFormDashboard(){

        open("/dashboard/DATA-CAPTURE-WO-PROCESS");
    }

    @Test
    @DisplayName("Data Capture without approval should create a Form Fill Task")
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
            $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("In Progress")); //Verify the Data Capture state
            $("#gridItemTasks").should(exist);
            $("#FormDashboardTasksCard .MuiCardContent-root").should(exist);
            $(".MuiCardContent-root div[class*='MuiPaper-rounded']:nth-of-type(1) span[iconname='far fa-edit']").shouldBe(visible).click();
            $("#data-card-dialog_actions").should(appear);
            $("#dataContainer").should(exist);
            $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").should(exist);
            $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").setValue("TEST");
            $("#btnAcceptTask").click();
            $("#data-approve-reject-dialog").$("#btnConfirm").click();
            $("#FormDashboardTasksCard .voEmptySpaceFiller").shouldBe(visible); //My Tasks should be empty
            $("div[role='tablist'] button:nth-child(3)").click();
            $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed"));
    }
}
