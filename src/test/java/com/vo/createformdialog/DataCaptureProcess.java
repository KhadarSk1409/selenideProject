package com.vo.createformdialog;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import utils.ReuseActions;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static utils.ReuseActions.*;
import static utils.ReuseActions.validationsAfterCheckingMembersOfMSgroup;

import utils.ReuseActions;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture Process")
public class DataCaptureProcess extends BaseTest {

    @Test
    @DisplayName("Verify initial setup in Add Language screen")
    @Order(1)
    public void validateIntialSetup() {
        //Create Form:
        createForm();
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Click on Additional Options

        $("#wizardFormDlg .mtable_toolbar button:first-of-type").should(exist); //+ button in Add Language - confirmation that user has navigated
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

        $("#ckb_first_ApproverManager").should(exist).click(); //Direct manager of data filter checkbox is checked
        $("#ckb_first_ApproverGroupInMS").shouldNotBe(checked); //Members of MS Group checkbox
        $("#ckb_first_ApproverGroupInVO").shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $("#ckb_first_tApproverFreeUserSelection").shouldNotBe(checked); //Free User Selection checkbox
        $("#sw_first_UserCanOverwrite").shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off

        $("#ckb_first_ApproverGroupInMS").should(exist).click(); //Members of MS Group is checked
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#fc_first_MSGroupSelect #selUser").click();
        String optionDropdown = $("#selUser-option-0 div:first-of-type").should(exist).getText(); //User selects first option from the dropdown
        $("#selUser-option-0").click();
        $("#fc_first_MSGroupSelect #selUser").shouldHave(value(optionDropdown));
        Thread.sleep(3000);
        //validationsAfterCheckingMembersOfMSgroup();

        $("#ckb_first_ApproverGroupInVO").should(exist).click(); //Members of VisualOrbit Group
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#fc_first_VOGroupSelect #selUser").click(); //Click on dropdown
        String optionDropdown1 = $("#selUser-option-0 div:first-of-type").shouldBe(visible).getText();
        $("#selUser-option-0").click();
        $("#fc_first_VOGroupSelect #selUser").shouldHave(value(optionDropdown1));
        Thread.sleep(3000);

        $("#ckb_first_tApproverFreeUserSelection").should(exist).click(); //Free User Selection
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#fc_first_UserSelect #selUser").click();
        String optionDropdown2 = $("#selUser-option-0 div:first-of-type").should(exist).getText();
        $("#selUser-option-0").click(); //User selects first option from the dropdown
        $("#fc_first_UserSelect").shouldHave(text(optionDropdown2));
        $("#wizard-createFormButton").should(exist).shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(disabled); //Next button is disabled
    }

    @Test
    @DisplayName("Data capture with two approvals Second approval")
    @Order(8)
    public void validateDataCaptureTwoApprovalsSecondApprovals() throws InterruptedException {
        if (!$("#rb_Basic_Fill_Form_Process_WithApproval_TwoStep").isSelected())
            $("#rb_Basic_Fill_Form_Process_WithApproval_TwoStep").should(exist).click(); //Click on Data capture with two approvals

        $("#data_capture_process_container").$(byText("Second Approval")).should(exist).click(); //Click Second approval
        $("#ckb_second_ApproverGroupInMS").shouldNotBe(checked); //Members of MS Group checkbox
        $("#ckb_second_ApproverGroupInVO").shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $("#sw_second_UserCanOverwrite").shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off
        $("#ckb_second_tApproverFreeUserSelection").shouldBe(checked).click(); //Free User Selection checkbox

        $("#ckb_second_ApproverGroupInMS").click(); //Members of MS Group is checked
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#selUser").should(exist).click(); //Select approver dropdown is enabled
        String optionDropdown = $("#selUser-option-0 div:first-of-type").shouldBe(visible).getText();
        $("#selUser-option-0").click();
        $("#fc_second_MSGroupSelect #selUser").shouldHave(value(optionDropdown));
        Thread.sleep(3000);
        //validationsAfterCheckingMembersOfMSgroup();

        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled

        $("#ckb_second_ApproverGroupInVO").should(exist).click(); //Members of VisualOrbit Group
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#selUser").should(exist).click(); //Select approver dropdown is enabled
        String optionDropdown1 = $("#selUser-option-0 div:first-of-type").shouldBe(visible).getText();
        $("#selUser-option-0").click();
        $("#fc_second_VOGroupSelect #selUser").shouldHave(value(optionDropdown1));
        Thread.sleep(3000);
        // validationsAfterCheckingMembersOfVisualOrbit();

        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled

        $("#ckb_second_tApproverFreeUserSelection").should(exist).click(); //Free User Selection
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#selUser").should(exist).click(); //Select approver dropdown is enabled
        String optionDropdown2 = $("#selUser-option-0 div:first-of-type").shouldBe(visible).getText();
        $("#selUser-option-0").click(); //User selects first option from the dropdown
        $("#fc_second_UserSelect").should(exist).shouldHave(text(optionDropdown2)); //Changed from #fc_second_UserSelect #selUser, referring to the element focused

        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled
        // validationsAfterCheckingFreeUserSelection();
    }

    @Test
    @DisplayName("Cleanup as last step")
    @Order(9)
    public void cleanup() {
        if ($("#wizard-cancelButton").exists()) {
            $("#wizard-cancelButton").click(); //Click on Cancel button
            $("#confirmation-dialog-title").should(exist); //Confirmation for Cancellation is shown
            $("#btnConfirm").should(exist).click();
            $("#confirmation-dialog-content").shouldNot(appear); //Click on Confirm button
        }

    }
}
