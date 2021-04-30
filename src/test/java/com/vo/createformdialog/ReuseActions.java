package com.vo.createformdialog;

import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class ReuseActions extends BaseTest {

    public static void createForm() {
        //Create Form:
        $("#toDashboard").click(); //Go back to Dashboard
        $("#btnCreateForm").should(exist).click(); //Click on Create Form button
        $("#wizardFormDlg").should(appear); //Create Form wizard appears
        String formTitle = RandomStringUtils.randomAlphanumeric(4);
        $("#wizard-formTitle").setValue(formTitle); //Set Title name
        String formDesc = RandomStringUtils.randomAlphanumeric(5);
        $("#wizard-formHelp").setValue(formDesc); //Setting form Description
        applyLabelForTestForms();
        $("#btnCreateForm").shouldBe(enabled); //Create Form button should be enabled
    }

    public static void validationsAfterCheckingDirectManager() {
        $("#ckb_first_ApproverManager").shouldBe(checked); //Direct manager of form publisher checkbox is checked
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-addlOptionsButton").shouldBe(enabled); //Next button
    }

    public static void validationsAfterCheckingMembersOfMSgroup() throws InterruptedException {
        $("#ckb_first_ApproverGroupInMS").should(exist).click(); //Members of MS Group is checked
        $("#selUser").should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled

        $("#fc_first_MSGroupSelect #selUser").click();
        String optionDropdown = $("#selUser-option-0 div:first-of-type").should(exist).getText(); //User selects first option from the dropdown
        $("#selUser-option-0").click();
        $("#fc_first_MSGroupSelect #selUser").shouldHave(value(optionDropdown));
        Thread.sleep(3000);
        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled
    }

    public static void validationsAfterCheckingMembersOfVisualOrbit() throws InterruptedException {
        $("#ckb_first_ApproverGroupInVO").should(exist).click(); //Members of VisualOrbit Group checkbox
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").should(exist).shouldBe(disabled); //Create Form button is diabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(disabled); //Next button is disabled
        $("#fc_first_VOGroupSelect #selUser").should(exist); //Dropdown for Approver exists
        $("#fc_first_VOGroupSelect #selUser").click(); //Click on dropdown
        String optionDropdown1 = $("#selUser-option-0 div:first-of-type").shouldBe(visible).getText();
        $("#selUser-option-0").click();
        Thread.sleep(3000);
        $("#wizard-createFormButton").should(exist).shouldBe(enabled); //Create Form button is enabled
        $("#wizard-addlOptionsButton").should(exist).shouldBe(enabled); //Next button is enabled
    }

    public static void validationsAfterCheckingFreeUserSelection() {
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
