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

import java.util.List;
import java.util.function.IntFunction;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
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
                .shouldHave(Condition.text("Started Data Capture process for the form: DATA-CAPTURE-WO-PROCESS and version 1.0"));
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
            $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").should(exist);
            $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").setValue("TEST");
            $("#btnAcceptTask").click();
            $("#data-approve-reject-dialog #btnConfirm").should(exist).click();
            $("#gridItemUserDataList").should(exist);
            $("#tabDataCapture").should(exist).click(); //Click on Data Capture
            $("#tasksCard tbody tr:nth-child(2) td:nth-child(5)").shouldHave(value("Completed"));
    }
}
