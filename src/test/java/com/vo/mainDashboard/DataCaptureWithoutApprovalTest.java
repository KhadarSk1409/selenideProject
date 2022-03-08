package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.elementLocators;

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
            $(elementLocators("SubMenu")).should(exist).shouldBe(enabled).click();
            $(elementLocators("DataCaptureInSubMenu")).should(exist).shouldHave(Condition.text("Data Capture")).click();
            $(elementLocators("UserSelectionInput")).should(appear);
            $(elementLocators("DropDownButton")).should(exist).click();
            $(elementLocators("Popover")).should(appear, Duration.ofSeconds(30));
            $$(elementLocators("ListOfOptions")).findBy(text("GUI Tester")).should(appear, Duration.ofSeconds(30)).click();
            $(elementLocators("UserSelectionInput")).click();
            $(elementLocators("StartDataCaptureButton")).click(); //Start Data Capture Process
            $(elementLocators("ConfirmationMessage")).should(appear)
                .shouldHave(Condition.text("Started Data Capture process for the form: DATA-CAPTURE-WO-PROCESS and version 3.0"));
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
            $(elementLocators("FormState")).shouldHave(Condition.text("In Progress"), Duration.ofSeconds(30));//DataCapture State
            String formDataCaptureId= $(elementLocators("NewFormID")).should(exist).getAttribute("id");
            System.out.println(formDataCaptureId);
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("MyTasks")).should(exist).click(); //Click on My Tasks
            $(elementLocators("FormsAvailable")).find(byAttribute("data-process-instance-id", formDataCaptureId )).should(exist)
              .$(elementLocators("FillForm")).should(exist).shouldBe(enabled).click(); //Click on Fill Form with matching id's
            $(elementLocators("DataCardActionsPage")).should(appear);
            $(elementLocators("DataContainer")).should(exist);
            $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").should(exist);
            $("#textField_form-user-160cfec0-aef2-4927-a8a8-aff595813f53").setValue("TEST");
            //LEI Number: Required field to be filled
            $(elementLocators("LeiInputField")).setValue("5299001Q62EEBWDKOO80").sendKeys(Keys.TAB);//Set LEI value
            $(elementLocators("SubmitDataButton")).click();
            $(elementLocators("ConfirmButton")).should(exist).click();
            $(elementLocators("UserDataList")).should(exist);
            $(elementLocators("DataCapture")).should(exist).click(); //Click on Data Capture
            $(elementLocators("FormState")).shouldHave(Condition.text("Completed"));//Verify the final Data Capture state
    }
}
