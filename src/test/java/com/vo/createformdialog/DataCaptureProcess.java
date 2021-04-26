package com.vo.createformdialog;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import java.util.List;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Data Capture Process")
public class DataCaptureProcess extends BaseTest{

    @Test
    @DisplayName("Verify initial setup in Add Language screen")
    @Order(1)
    public void validateIntialSetup() {
        //Create Form:
        $("#toDashboard").click(); //Go back to Dashboard
        $("#btnCreateForm").should(exist).click(); //Click on Create Form button
        $("#wizardFormDlg").should(appear); //Create Form wizard appears
        String formTitle = RandomStringUtils.randomAlphanumeric(4);
        $("#wizard-formTitle").setValue(formTitle); //Set Title name
        String formDesc = RandomStringUtils.randomAlphanumeric(5);
        $("#wizard-formHelp").setValue(formDesc); //Setting form Description
        $("#btnCreateForm").shouldBe(enabled); //Create Form button should be enabled
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Click on Additional Options

        $("#wizardFormDlg .mtable_toolbar button:first-of-type").should(exist); //+ button in Add Language - confirmation that user has navigated
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Next button
        $("#wizard-addlOptionsButton").shouldBe(enabled).click(); //Next button

        //Verify that user is navigated to Publication Process
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
    public void validationsAfterCheckingEnableDataCapture()
    {
        $("#ckbDataCapturreRequired").should(exist).click(); //"Yes, Enable data capture flow for this form" Checkbox is checked

        if(!($("#rb_Basic_Fill_Form_Process_WithApproval_OneStep").isSelected())) //If Data Capture with one approval is not checked
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
    public void validationsAfterCheckingDirectManagerOfDataFilter()
    {
        $("#ckb_first_ApproverManager").shouldBe(checked); //Direct manager of form publisher checkbox is checked
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-addlOptionsButton").shouldBe(enabled); //Next button
    }

    @Test
    @DisplayName("Validations after checking Members of MS Group checkbox")
    @Order(4)
    public void validationsAfterCheckingMembersOfMsGroup()
    {
        $("#ckb_first_ApproverGroupInMS").should(exist).click(); //Members of MS Group is checked
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled

        $("#selUser").click(); //Click on Candidate approver dropdown
        $("#selUser-option-0").should(exist).click(); //User selects first option from the dropdown
        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled

    }


    @Test
    @DisplayName("Validations after checking Members of VisualOrbit Group")
    @Order(5)
    public void validationsAfterCheckingMembersOfVisualOrbitGroup()
    {
        $("#ckb_first_ApproverGroupInVO").should(exist).click(); //Members of VisualOrbit Group
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#selUser").click(); //Click on Candidate approver dropdown
        $("#selUser-option-0").should(exist).click(); //User selects first option from the dropdown
        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled
    }

    @Test
    @DisplayName("Validations after Free User Selection")
    @Order(6)
    public void validationsAfterCheckingFreeUserSelection()
    {
        $("#ckb_first_tApproverFreeUserSelection").should(exist).click(); //Free User Selection
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled

        $("#selUser").click(); //Click on Candidate approver dropdown
        $("#selUser-option-0").should(exist).click(); //User selects first option from the dropdown
        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled
    }

    @Test
    @DisplayName("Data Capture with two approvals First approval")
    @Order(7)
    public void validateDataCaptureTwoApprovalsFirstApprovals()
    {
        $("#rb_Basic_Fill_Form_Process_WithApproval_TwoStep").should(exist).click(); // with two approvals
    //    $("#publication_process_container").$(byText("First Approval")).should(exist).click(); //Click on First Approvals - id not found ??

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
        $("#selUser").click(); //Click on Candidate approver dropdown
        $("#selUser-option-0").should(exist).click(); //User selects first option from the dropdown

        $("#publication_process_container").click();
        $("#ckb_first_ApproverGroupInVO").should(exist).click(); //Members of VisualOrbit Group
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#selUser").click(); //Click on Candidate approver dropdown
        $("#selUser-option-0").should(exist).click(); //User selects first option from the dropdown

        $("#ckb_first_tApproverFreeUserSelection").should(exist).click(); //Free User Selection
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#selUser").click(); //Click on Candidate approver dropdown
        $("#selUser-option-0").should(exist).click(); //User selects first option from the dropdown
        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled

    }

    @Test
    @DisplayName("Data capture with two approvals Second approval")
    @Order(8)
    public void validateDataCaptureTwoApprovalsSecondApprovals()
    {
        if(!$("#rb_Basic_Fill_Form_Process_WithApproval_TwoStep").isSelected())
            $("#rb_Basic_Fill_Form_Process_WithApproval_TwoStep").should(exist).click(); //Click on Data capture with two approvals

        $("#ckb_first_ApproverManager").should(exist).click(); //Direct manager of form publisher checkbox is checked
        $("#ckb_first_ApproverGroupInMS").shouldNotBe(checked); //Members of MS Group checkbox
        $("#ckb_first_ApproverGroupInVO").shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $("#ckb_first_tApproverFreeUserSelection").shouldNotBe(checked); //Free User Selection checkbox
        $("#sw_first_UserCanOverwrite").shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off
        $("#rb_Basic_Approve_Form_Process").shouldBe(selected); //Publication with one approval radio button is selected

        $("#ckb_first_ApproverGroupInMS").should(exist).click(); //Members of MS Group is checked
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#selUser").click(); //Click on Candidate approver dropdown
        $("#selUser-option-0").should(exist).click(); //User selects first option from the dropdown
        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled

        $("#ckb_first_ApproverGroupInVO").should(exist).click(); //Members of VisualOrbit Group
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#selUser").click(); //Click on Candidate approver dropdown
        $("#selUser-option-0").should(exist).click(); //User selects first option from the dropdown
        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled

        $("#ckb_first_tApproverFreeUserSelection").should(exist).click(); //Free User Selection
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $("#selUser").click(); //Click on Candidate approver dropdown
        $("#selUser-option-0").should(exist).click(); //User selects first option from the dropdown
        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled
    }

}
