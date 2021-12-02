package com.vo.createformdialog;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.vo.createformdialog.ReuseApproverSelection.selectApprovers;
import static reusables.ReuseActions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Publication Properties Tests")
public class PublicationPropertiesTest extends BaseTest {

    @Test
    @DisplayName("Verify Initial Setup")
    @Order(1)
    public void validateIntialSetup() {
        //Create Form:
        createNewForm();
        $(elementLocators("AdditionalOptionsButton")).shouldBe(enabled).click(); //Click on Additional Options
        $(elementLocators("AddLanguageButton")).should(exist); //+ button in Add Language - confirmation that user has navigated
        $(elementLocators("NextButton")).shouldBe(enabled).click(); //Next button
        //Verify that user is navigated to Publication Process
        $(elementLocators("EnablePublicationProcessDialog")).should(exist).shouldNotBe(checked); //Checkbox - "Yes, Enable publication workflow for this form" is unchecked
        $(elementLocators("BackButton")).shouldBe(enabled); //Back button is disabled
        $(elementLocators("CreateFormButton")).shouldBe(enabled); //Create Form button is enabled
        $(elementLocators("NextButton")).shouldBe(enabled); //Next button is enabled
        $(elementLocators("CancelButton")).shouldBe(enabled); //Cancel button is enabled
    }

    @Test
    @DisplayName("Validations after checking Enable Publication checkbox")
    @Order(2)
    public void validationsAfterCheckingEnablePublication() {
        $(elementLocators("EnablePublicationProcessCheckBox")).should(exist).click(); //Checkbox is checked

        if (!($(elementLocators("PublicationWithOneApproval")).isSelected())) //If Publication with one approval is not checked
            $(elementLocators("PublicationWithOneApproval")).click(); //Select Publication with one approval
        $(elementLocators("DirectManagerOfDataFiller")).shouldBe(checked); //Direct manager of form publisher checkbox is checked
        $(elementLocators("MembersOfMSGroup")).shouldNotBe(checked); //Members of MS Group checkbox
        $(elementLocators("MembersOfVOGroup")).shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $(elementLocators("FreeUserSelection")).shouldNotBe(checked); //Free User Selection checkbox
        $(elementLocators("EndUserCanOverwrite")).shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off
        $(elementLocators("PublicationWithOneApproval")).shouldBe(selected); //Publication with one approval radio button is selected

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
    public void validationsAfterCheckingFreeUserSelectionCheckbox() {
        validationsAfterCheckingFreeUserSelection();
    }

    @Test
    @DisplayName("Publication with two approvals First approval")
    @Order(7)
    public void validatePublicationTwoApprovalsFirstApprovals() throws InterruptedException {

        $(elementLocators("PublicationWithTwoApproval")).should(exist).click(); //Publication with two approvals

        ReuseApproverSelection.ApproverOrder order = ReuseApproverSelection.ApproverOrder.FIRST;
        $(elementLocators("PublicationProcessContainer")).$(byText(order.getLabelText())).should(exist).click(); //Click on First Approvals
        selectApprovers(elementLocators("PublicationProcessContainer"), order);
    }

    @Test
    @DisplayName("Publication with two approvals Second approval")
    @Order(8)
    public void validatePublicationTwoApprovalsSecondApprovals() throws InterruptedException {
        $(elementLocators("PublicationWithTwoApproval")).should(exist).click(); //Publication with two approvals

        ReuseApproverSelection.ApproverOrder order = ReuseApproverSelection.ApproverOrder.SECOND;
        $(elementLocators("PublicationProcessContainer")).$(byText(order.getLabelText())).should(exist).click(); //Click on Second Approvals
        selectApprovers("#publication_process_container", order);
    }
}
