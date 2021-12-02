package com.vo.createformdialog;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.vo.createformdialog.ReuseApproverSelection.selectApprovers;
import static reusables.ReuseActions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture Properties")
public class DataCapturePropertiesTest extends BaseTest {

    @Test
    @DisplayName("Verify initial setup in Add Language screen")
    @Order(1)
    public void validateIntialSetup() {
        //Create Form:
        createNewForm();
        $(elementLocators("AdditionalOptionsButton")).shouldBe(enabled).click(); //Click on Additional Options

        $(elementLocators("AddLanguageButton")).should(exist); //+ button in Add Language - confirmation that user has navigated
        $(elementLocators("NextButton")).shouldBe(enabled).click(); //Next button
        $(elementLocators("NextButton")).shouldBe(enabled).click(); //Next button

        //Verify that user is navigated to Datacapture screen
        $(elementLocators("EnableDataCaptureDialog")).should(exist); //"Yes, Enable data capture flow for this form" - Ensures user has navigated to Data Capture page
        $(elementLocators("EnableDataCaptureCheckBox")).shouldNotBe(checked); //"Yes, Enable data capture flow for this form" checkbox should be unchecked initially
        $(elementLocators("BackButton")).shouldBe(enabled); //Back button is enabled
        $(elementLocators("CreateFormButton")).shouldBe(enabled); //Create Form button is enabled
        $(elementLocators("NextButton")).shouldBe(enabled); //Next button is enabled
        $(elementLocators("CancelButton")).shouldBe(enabled); //Cancel button is enabled
    }

    @Test
    @DisplayName("Validations after checking Enable data capture checkbox")
    @Order(2)
    public void validationsAfterCheckingEnableDataCapture() {
        $(elementLocators("EnableDataCaptureCheckBox")).click(); //"Yes, Enable data capture flow for this form" Checkbox is checked

        if (!($(elementLocators("DatacaptureWithOneApproval")).isSelected())) //If Data Capture with one approval is not checked
            $(elementLocators("DatacaptureWithOneApproval")).click();
        $(elementLocators("DirectManagerOfDataFiller")).shouldBe(checked); //Direct manager of form publisher checkbox is checked
        $(elementLocators("MembersOfMSGroup")).shouldNotBe(checked); //Members of MS Group checkbox
        $(elementLocators("MembersOfVOGroup")).shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $(elementLocators("FreeUserSelection")).shouldNotBe(checked); //Free User Selection checkbox
        $(elementLocators("EndUserCanOverwrite")).shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off
        $(elementLocators("DatacaptureWithOneApproval")).shouldBe(selected); //Data Capture with one approval radio button is selected
    }

    @Test
    @DisplayName("Validations after checking the checkbox Direct manager of data filter")
    @Order(3)
    public void validationsAfterCheckingDirectManagerOfDataFilter() {
        validationsAfterCheckingDirectManager();
    }

    @Test
    @DisplayName("Validations after checking Members of MS Group checkbox")
    @Order(4)
    public void validationsAfterCheckingMembersOfMsGroup() {
        validationsAfterCheckingMembersOfMSgroup();
    }

    @Test
    @DisplayName("Validations after checking Members of VisualOrbit Group")
    @Order(5)
    public void validationsAfterCheckingMembersOfVisualOrbitGroup() throws InterruptedException {
        validationsAfterCheckingMembersOfVisualOrbit();
    }

    @Test
    @DisplayName("Validations after Free User Selection")
    @Order(6)
    public void validationsAfterCheckingFreeUserSelectionCheckBox() {
        validationsAfterCheckingFreeUserSelection();
    }

    @Test
    @DisplayName("Data Capture with two approvals First approval")
    @Order(7)
    public void validateDataCaptureTwoApprovalsFirstApprovals() throws InterruptedException {
        $(elementLocators("DatacaptureWithTwoApprovals")).should(exist).click(); //with two approvals

        ReuseApproverSelection.ApproverOrder order = ReuseApproverSelection.ApproverOrder.FIRST;

        $(elementLocators("DataCaptureProcessContainer")).$(byText(order.getLabelText())).should(exist).click(); //Click on First Approvals
        selectApprovers(elementLocators("DataCaptureProcessContainer"), order);
    }

    @Test
    @DisplayName("Data capture with two approvals Second approval")
    @Order(8)
    public void validateDataCaptureTwoApprovalsSecondApprovals() throws InterruptedException {
        if (!$(elementLocators("DatacaptureWithTwoApprovals")).isSelected())
            $(elementLocators("DatacaptureWithTwoApprovals")).should(exist).click(); //Click on Data capture with two approvals

        ReuseApproverSelection.ApproverOrder order = ReuseApproverSelection.ApproverOrder.SECOND;
        $(elementLocators("DataCaptureProcessContainer")).$(byText(order.getLabelText())).should(exist).click(); //Click Second approval
        selectApprovers(elementLocators("DataCaptureProcessContainer"), order);
    }

}
