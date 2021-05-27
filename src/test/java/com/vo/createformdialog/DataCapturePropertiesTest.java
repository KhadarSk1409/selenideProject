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
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Click on Additional Options

        $("#dlgFormFormWizard .mtable_toolbar button:first-of-type").should(exist); //+ button in Add Language - confirmation that user has navigated
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Next button
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Next button

        //Verify that user is navigated to Datacapture screen
        $("#lDataCapturreRequired").should(exist); //"Yes, Enable data capture flow for this form" - Ensures user has navigated to Data Capture page
        $("#ckbDataCapturreRequired").shouldNotBe(checked); //"Yes, Enable data capture flow for this form" checkbox should be unchecked initially
        $("#wizard-backButton").shouldBe(enabled); //Back button is enabled
        $("#wizard-createFormButton").shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").shouldBe(enabled); //Next button is enabled
        $("#wizard-cancelButton").shouldBe(enabled); //Cancel button is enabled
    }

    @Test
    @DisplayName("Validations after checking Enable data capture checkbox")
    @Order(2)
    public void validationsAfterCheckingEnableDataCapture() {
        $("#ckbDataCapturreRequired").should(exist).click(); //"Yes, Enable data capture flow for this form" Checkbox is checked

        if (!($("#rb_Basic_Fill_Form_Process_WithApproval_OneStep").isSelected())) //If Data Capture with one approval is not checked
            $("#rb_Basic_Fill_Form_Process_WithApproval_OneStep").click();
        $("#ckbDataCapturreRequired").shouldBe(checked); //Direct manager of form publisher checkbox is checked
        $("#ckb_first_ApproverGroupInMS").shouldNotBe(checked); //Members of MS Group checkbox
        $("#ckb_first_ApproverGroupInVO").shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $("#ckb_first_tApproverFreeUserSelection").shouldNotBe(checked); //Free User Selection checkbox
        $("#sw_first_UserCanOverwrite").shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off
        $("#rb_Basic_Fill_Form_Process_WithApproval_OneStep").shouldBe(selected); //Data Capture with one approval radio button is selected
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
    public void validationsAfterCheckingMembersOfMsGroup() throws InterruptedException {
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
        $("#rb_Basic_Fill_Form_Process_WithApproval_TwoStep").should(exist).click(); //with two approvals

        ReuseApproverSelection.ApproverOrder order = ReuseApproverSelection.ApproverOrder.FIRST;

        $("#data_capture_process_container").$(byText(order.getLabelText())).should(exist).click(); //Click on First Approvals
        selectApprovers("#data_capture_process_container", order);
    }

    @Test
    @DisplayName("Data capture with two approvals Second approval")
    @Order(8)
    public void validateDataCaptureTwoApprovalsSecondApprovals() throws InterruptedException {
        if (!$("#rb_Basic_Fill_Form_Process_WithApproval_TwoStep").isSelected())
            $("#rb_Basic_Fill_Form_Process_WithApproval_TwoStep").should(exist).click(); //Click on Data capture with two approvals

        ReuseApproverSelection.ApproverOrder order = ReuseApproverSelection.ApproverOrder.SECOND;
        $("#data_capture_process_container").$(byText(order.getLabelText())).should(exist).click(); //Click Second approval
        selectApprovers("#data_capture_process_container", order);
    }

}
