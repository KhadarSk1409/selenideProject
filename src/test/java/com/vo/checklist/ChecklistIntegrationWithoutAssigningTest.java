package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsChecklistCreation.createNewChecklist;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the checklist Integration without assigning to a user")
public class ChecklistIntegrationWithoutAssigningTest extends BaseTest {

    @Test
    @DisplayName("Create a new checklist and verify integration process")
    public void createNewChecklistAndVerifyIntegration() throws InterruptedException {

        createNewChecklist();
        SelenideElement sourceEventTrigger = $(elementLocators("SourceEventTrigger"));
        SelenideElement targetChecklistFlow = $(elementLocators("TargetChecklistFlow"));

        //Get the initial locations of Source and Target elements
        int sourceETXOffset = sourceEventTrigger.getLocation().getX();
        int sourceETYOffset = sourceEventTrigger.getLocation().getY();
        int targetXOffset = targetChecklistFlow.getLocation().getX();
        int targetYOffset = targetChecklistFlow.getLocation().getY();
        $(elementLocators("SourceFormElement")).should(exist).doubleClick();
        $(byText(elementLocators("SelectAForm"))).should(appear);
        $(byText(elementLocators("ChooseFromLibrary"))).click();
        $(elementLocators("FormsGridContainer")).should(appear); //Forms available in Library will appear
        $(elementLocators("FormsAvailableInTable")).should(exist).getSize();
        String formToBeSelected = "IbanTestForm";
        $(elementLocators("SearchInputField")).should(exist).setValue(formToBeSelected);
        String formDataID1 = $(elementLocators("FormsAvailableInTable")).find(byAttribute("data-form-name",formToBeSelected))
                .should(exist).getAttribute("data-form-uuid");
        $(elementLocators("FormsAvailableInTable")).find(byAttribute("data-form-name",formToBeSelected)).should(appear).click();
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(formToBeSelected)); //Verify whether the selected form is available in the Checklist flow or not
        assert formDataID1 != null;
        $(elementLocators("TargetListInChecklistFlow")).find(byAttribute("id",formDataID1)).should(exist);

        //Drag n Drop Event Trigger to checklist flow
        actions().clickAndHold(sourceEventTrigger).moveToElement(targetChecklistFlow).build().perform();
        int ETXOffset = (targetXOffset-sourceETXOffset)+50;
        int ETYOffset=  (sourceETYOffset-targetYOffset)-250;
        actions().moveByOffset(ETXOffset,ETYOffset).release().build().perform();
        $(elementLocators("EventTriggerEditor")).should(appear);
        $(elementLocators("EventTriggerEditor")).should(appear);
        $(elementLocators("IntegrationInputField")).should(exist).click();
        $(elementLocators("SecondIntegrationInTheList")).should(appear).click();
        SelenideElement Integration1 = $(elementLocators("IntegrationInputField")).should(exist).shouldHave(text("IBAN Number Verification"));
        String SelectedIntegration = Integration1.getText();
        $("#sel_source").should(exist).click();
        $("#menu- ul.MuiList-root").should(appear);
        $("#menu- ul.MuiList-root  li:nth-child(2)").should(exist).click();
        $("#sel_source").shouldHave(text("IbanTestForm > Iban"));
        $(elementLocators("OkButton")).should(exist).click();
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(SelectedIntegration)); //Verify whether the selected form is available in the Checklist flow or not

        //Drag n Drop PDF Generator to Post Processing flow
        $(elementLocators("PDFGenerator")).doubleClick();
        $(byText(elementLocators("UploadPDFTemplateInUSLetterOrA4PageFormat"))).should(appear);
        $(elementLocators("UploadPdfInput")).uploadFromClasspath("samplePDF.pdf");
        $(elementLocators("AttachFileIcon")).should(appear);
        $(elementLocators("FileUploadedMessage")).should(appear);
        $(elementLocators("SubmitButton")).click(); //Click on Submit Button

        SelenideElement Iban = $("#IBAN");
        SelenideElement Bic = $("#BIC");
        SelenideElement Bank = $("#BankName");
        $(elementLocators("EventData")).shouldBe(visible).click();
        $(elementLocators("AvailableIntegrations")).find(byText("IBAN Number Verification")).should(appear).click();
        $(Iban).should(exist);
        $(Bic).should(exist);
        $(Bank).should(exist);

        //Make hidden add buttons visible for the test user to add multiple components into the pdf
        String js = "var els = document.querySelectorAll('.btn_AddSrc')\n" +
                "for(var idx = 0; idx < els.length; idx ++) {els[idx].style.visibility = 'inherit' }";
        executeJavaScript(js);
        //Add IBAN Component from IBAN Number Verification Integration to the PDF Editor
        $(elementLocators("AddIBANFromIntegration")).shouldBe(visible).click();
        SelenideElement IbanBox = $(elementLocators("PDFContainer")).shouldBe(visible).find(byText("IBAN")).should(appear);
        $(elementLocators("LeftPosition")).should(appear).setValue("75").pressTab();
        $(elementLocators("TopPosition")).should(appear).setValue("100").pressTab();
        $(elementLocators("OkButton")).should(exist).click();

        $(elementLocators("PublishChecklistTemplateButton")).should(exist).shouldBe(enabled).click();
        $(elementLocators("ConfirmPublish")).should(appear).click();
        Thread.sleep(5000);
        $(byText("Loading Checklists")).should(disappear);
        $(elementLocators("StartChecklistButton")).should(appear).shouldBe(enabled).click();

        $(byText("CheckList")).should(appear);
        $(elementLocators("ChecklistComponentList")).should(exist).shouldHave(text(formToBeSelected)).click();
        $(elementLocators("ChecklistData")).should(exist).shouldHave(text(formToBeSelected));
        String validIban = "DE88 5005 0000 0001 0002 31";
        $("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").should(exist).sendKeys(validIban);
        if(!$("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").has(value(validIban))){
            $("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").should(exist).sendKeys(validIban);
        }
        $("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").shouldHave(value(validIban)).pressTab();
        $("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").shouldHave(value(validIban));
        $("#textField_form-user-d379183d-295f-40ce-9e97-6232d595cf6c").shouldHave(value("HELADEFFXXX"));
        Thread.sleep(2000);
        $(elementLocators("StartWorkFlowButton")).should(exist).click();
        $(elementLocators("ConfirmationMessage")).should(appear).should(disappear);
        $(byText("samplePDF.pdf")).click();
        $(byText("Loading PDF Preview")).should(disappear);
        $(elementLocators("IbanBlock")).shouldHave(text(validIban));
      }
}
