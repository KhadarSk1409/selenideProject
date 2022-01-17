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

        $(elementLocators("LeftFormDashboardHeader")).should(appear, Duration.ofSeconds(30));
        $(elementLocators("SubMenu")).should(exist).shouldBe(enabled).click();
        $(elementLocators("DataCaptureInSubMenu")).should(exist).shouldHave(Condition.text("Data Capture")).click();
        $(elementLocators("UserSelectionInput")).should(appear);
        $(elementLocators("DropDownButton")).should(exist).click();
        $(elementLocators("Popover")).should(appear, Duration.ofSeconds(30));
        $$(elementLocators("ListOfOptions")).findBy(text("GUI Tester 01")).should(appear, Duration.ofSeconds(30)).click(); //Click on GUI Tester 01
        $(elementLocators("UserSelectionInput")).click();
        $(elementLocators("StartDataCaptureButton")).click(); //Start Data Capture Process
        $(elementLocators("ConfirmationMessage"))
                .shouldHave(Condition.text("Started Data Capture process for the form: DATA-CAPTURE-WO-PROCESS and version 2.0"));
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
        $(elementLocators("FormState")).shouldHave(Condition.text("In Progress")); //Verify the Data Capture state
        String formDataCaptureId= $(elementLocators("NewFormID")).should(exist).getAttribute("id");

        //Should Login as GUI TESTER 01
        shouldLogin(UserType.USER_01);
        open("/dashboard/DATA-CAPTURE-WO-PROCESS");
        $(elementLocators("LeftFormDashboardHeader")).should(appear, Duration.ofSeconds(30));
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
        $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
                .$(elementLocators("FillForm")).should(exist).shouldBe(enabled).click(); //Click on Fill Form
        $(elementLocators("DataCardActionsPage")).should(appear);
        $(elementLocators("DataContainer")).should(exist);
        $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").should(exist);
        $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").setValue("TEST");
        $(elementLocators("SubmitDataButton")).click();
        $(elementLocators("ConfirmButton")).should(exist).click();

        //Should Login as GUI Tester
        shouldLogin(UserType.MAIN_TEST_USER);
        open("/dashboard/DATA-CAPTURE-WO-PROCESS");
        $(elementLocators("LeftFormDashboardHeader")).should(appear, Duration.ofSeconds(30));
        $(elementLocators("UserDataList")).should(exist);
        $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
        $(elementLocators("FormState")).shouldHave(Condition.text("Completed"));

    }
}
