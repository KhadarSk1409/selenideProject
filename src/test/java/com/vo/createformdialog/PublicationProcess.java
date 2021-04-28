package com.vo.createformdialog;

import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static utils.ReuseActions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Publication Process")
public class PublicationProcess extends BaseTest {

    @Test
    @DisplayName("Verify Initial Setup")
    @Order(1)
    public void validateIntialSetup() {
        //Create Form:
        createForm();
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Click on Additional Options
        $("#wizardFormDlg .mtable_toolbar button:first-of-type").should(exist); //+ button in Add Language - confirmation that user has navigated
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Next button
        //Verify that user is navigated to Publication Process
        $("#ckbApprovalProcessRequired").should(exist).shouldNotBe(checked); //Checkbox - "Yes, Enable publication workflow for this form" is unchecked
        $("#wizard-backButton").shouldBe(enabled); //Back button is disabled
        $("#wizard-createFormButton").shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").shouldBe(enabled); //Next button is enabled
        $("#wizard-cancelButton").shouldBe(enabled); //Cancel button is enabled
    }

    @Test
    @DisplayName("Validations after checking Enable Publication checkbox")
    @Order(2)
    public void validationsAfterCheckingEnablePublication() {
        $("#wizardFormDlg  input").should(exist).click(); //Checkbox is checked

        if (!($("#rb_Basic_Approve_Form_Process").isSelected())) //If Publication with one approval is not checked
            $("#rb_Basic_Approve_Form_Process").click(); //Select Publication with one approval
        $("#ckb_first_ApproverManager").shouldBe(checked); //Direct manager of form publisher checkbox is checked
        $("#ckb_first_ApproverGroupInMS").shouldNotBe(checked); //Members of MS Group checkbox
        $("#ckb_first_ApproverGroupInVO").shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $("#ckb_first_tApproverFreeUserSelection").shouldNotBe(checked); //Free User Selection checkbox
        $("#sw_first_UserCanOverwrite").shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off
        $("#rb_Basic_Approve_Form_Process").shouldBe(selected); //Publication with one approval radio button is selected

    }

    @Test
    @DisplayName("Validations after checking the checkbox Direct manager of from publisher")
    @Order(3)
    public void validationsAfterCheckingDirectManagerOfFromPublisher() {
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
    public void validationsAfterCheckingFreeUserSelectionCheckbox() {
        validationsAfterCheckingFreeUserSelection();
    }

    @Test
    @DisplayName("Publication with two approvals First approval")
    @Order(7)
    public void validatePublicationTwoApprovalsFirstApprovals() throws InterruptedException {

        $("#rb_Basic_Approve_Form_Process_TwoSteps").should(exist).click(); //Publication with two approvals
        $("#publication_process_container").$(byText("First Approval")).should(exist).click(); //Click on First Approvals

        $("#ckb_first_ApproverManager").should(exist).click(); //Direct manager of form publisher checkbox is checked
        $("#ckb_first_ApproverGroupInMS").shouldNotBe(checked); //Members of MS Group checkbox
        $("#ckb_first_ApproverGroupInVO").shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $("#ckb_first_tApproverFreeUserSelection").shouldNotBe(checked); //Free User Selection checkbox
        $("#sw_first_UserCanOverwrite").shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off

        $("#ckb_first_ApproverGroupInMS").should(exist).click(); //Members of MS Group is checked
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#fc_first_MSGroupSelect #selUser").should(appear); //Select approver for MS Group dropdown is enabled
        $("#publication_process_container").$(byText("First Approval")).should(exist).click(); //Click on First Approvals
        $("#fc_first_MSGroupSelect #selUser").click();
        String optionDropdown = $("#selUser-option-0 div:first-of-type").should(exist).getText(); //User selects first option from the dropdown
        $("#selUser-option-0").click();
        $("#fc_first_MSGroupSelect #selUser").shouldHave(value(optionDropdown));
        Thread.sleep(3000);

        $("#ckb_first_ApproverGroupInVO").should(exist).click(); //Members of VisualOrbit Group
        $("#publication_process_container").$(byText("First Approval")).should(exist).click(); //Click on First Approvals
        $("#fc_first_VOGroupSelect #selUser").should(appear); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled

        $("#l_first_ApproverGroupInVO").should(exist);
        $("#l_first_ApproverGroupInVO").click(); //Click on Members Of Visual Orbit Group
        $("#fc_first_VOGroupSelect #selUser").shouldBe(visible);
        $("#fc_first_VOGroupSelect #selUser").click(); //Click on Candidate approver dropdown
        String optionDropdown1 = $("#selUser-option-0 div:first-of-type").shouldBe(visible).getText();
        $("#selUser-option-0").click();
        $("#fc_first_VOGroupSelect #selUser").shouldHave(value(optionDropdown1));
        Thread.sleep(3000);

        $("#publication_process_container").$(byText("First Approval")).should(exist).click(); //Click on First Approvals
        $("#ckb_first_tApproverFreeUserSelection").should(exist).click(); //Free User Selection
        $("#fc_first_UserSelect #selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#fc_first_UserSelect #selUser").should(exist).click(); //Click on Free User Selection dropdown
        String optionDropdown2 = $("#selUser-option-0 div:first-of-type").should(exist).getText();
        $("#selUser-option-0").click(); //User selects first option from the dropdown
        $("#fc_first_UserSelect").shouldHave(text(optionDropdown2));
        $("#wizard-createFormButton").should(exist).shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(disabled); //Next button is disabled
    }

    @Test
    @DisplayName("Publication with two approvals Second approval")
    @Order(8)
    public void validatePublicationTwoApprovalsSecondApprovals() throws InterruptedException {
        $("#publication_process_container").$(byText("Second Approval")).should(exist).click(); //Click on Second Approvals

        $("#rb_Basic_Approve_Form_Process_TwoSteps").should(exist).click(); //Publication with two approvals
        $("#ckb_second_ApproverManager").should(exist).click(); //Direct manager of form publisher checkbox is checked
        $("#ckb_second_ApproverGroupInMS").shouldNotBe(checked); //Members of MS Group checkbox
        $("#ckb_second_ApproverGroupInVO").shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $("#ckb_second_tApproverFreeUserSelection").shouldNotBe(checked); //Free User Selection checkbox
        $("#sw_second_UserCanOverwrite").shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off

        $("#ckb_second_ApproverGroupInMS").should(exist).click(); //Members of MS Group is checked
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#fc_second_MSGroupSelect #selUser").should(appear); //Select approver for MS Group dropdown is enabled
        $("#publication_process_container").$(byText("Second Approval")).should(exist).click(); //Click on Second Approvals
        $("#fc_second_MSGroupSelect #selUser").click();
        String optionDropdown = $("#selUser-option-0 div:first-of-type").should(exist).getText();
        $("#selUser-option-0").click();
        $("#fc_second_MSGroupSelect #selUser").shouldHave(value(optionDropdown));
        Thread.sleep(3000);

        $("#ckb_second_ApproverGroupInVO").should(exist).click(); //Members of VisualOrbit Group
        $("#publication_process_container").$(byText("Second Approval")).should(exist).click(); //Click on Second Approvals
        $("#fc_second_VOGroupSelect #selUser").should(appear); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled

        $("#l_second_ApproverGroupInVO").should(exist);
        $("#l_second_ApproverGroupInVO").click(); //Click on Members Of Visual Orbit Group
        $("#fc_second_VOGroupSelect #selUser").shouldBe(visible);
        $("#fc_second_VOGroupSelect #selUser").click(); //Click on Candidate approver dropdown
        String optionDropdown1 = $("#selUser-option-0 div:first-of-type").shouldBe(visible).getText();
        $("#selUser-option-0").click();
        $("#fc_second_VOGroupSelect #selUser").shouldHave(value(optionDropdown1));
        Thread.sleep(3000);

        $("#publication_process_container").$(byText("Second Approval")).should(exist).click(); //Click on Second Approvals
        $("#ckb_second_tApproverFreeUserSelection").should(exist).click(); //Free User Selection
        $("#fc_second_UserSelect #selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#fc_second_UserSelect #selUser").click(); //Click on Free User Selection dropdown
        String optionDropdown2 = $("#selUser-option-0 div:first-of-type").should(exist).getText();
        $("#selUser-option-0").click(); //User selects first option from the dropdown
        $("#fc_second_UserSelect").shouldHave(text(optionDropdown2));

        $("#rb_Basic_Approve_Form_Process_TwoSteps").should(exist).click(); //Publication with two approvals radio button
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(enabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(enabled); //Next button is disabled
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

//    @Test
//    @DisplayName("Cleanup as last step")
//    @Order(9)
//    public void cleanup() {
//        open("/dashboard");
//        deleteForm();
//    }
        //#wizardFormDlg h6

    }
}
