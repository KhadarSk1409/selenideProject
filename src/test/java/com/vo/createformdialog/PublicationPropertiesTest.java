package com.vo.createformdialog;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.vo.createformdialog.ReuseApproverSelection.selectApprovers;
import static utils.ReuseActions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Publication Properties Tests")
public class PublicationPropertiesTest extends BaseTest {

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

        ReuseApproverSelection.ApproverOrder order = ReuseApproverSelection.ApproverOrder.FIRST;
        $("#publication_process_container").$(byText(order.getLabelText())).should(exist).click(); //Click on First Approvals
        selectApprovers("#publication_process_container", order);
    }

    @Test
    @DisplayName("Publication with two approvals Second approval")
    @Order(8)
    public void validatePublicationTwoApprovalsSecondApprovals() throws InterruptedException {
        $("#rb_Basic_Approve_Form_Process_TwoSteps").should(exist).click(); //Publication with two approvals

        ReuseApproverSelection.ApproverOrder order = ReuseApproverSelection.ApproverOrder.SECOND;
        $("#publication_process_container").$(byText(order.getLabelText())).should(exist).click(); //Click on Second Approvals
        selectApprovers("#publication_process_container", order);
    }
}
