package com.vo.checklist;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsChecklistCreation.createNewChecklist;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the checklist Integration with assigning to a user")
public class ChecklistIntegrationByAssigningToAUserTest extends BaseTest {

    @Test
    @DisplayName("Create a new checklist and verify integration process")
    public void createNewChecklistAndVerifyIntegration() throws InterruptedException {

        createNewChecklist(); //Creates new checklist and opens the checklist designer
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
        $(elementLocators("FormsAvailableInTable")).find(byAttribute("data-form-name",formToBeSelected)).should(appear).click();
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(formToBeSelected)); //Verify whether the selected form is available in the Checklist flow or not

        //Drag n Drop Event Trigger to checklist flow
        actions().clickAndHold(sourceEventTrigger).moveToElement(targetChecklistFlow).build().perform();
        int ETXOffset = (targetXOffset - sourceETXOffset) + 50;
        int ETYOffset = (sourceETYOffset - targetYOffset) - 250;
        actions().moveByOffset(ETXOffset, ETYOffset).release().build().perform();
        $(elementLocators("EventTriggerEditor")).should(appear);
        $(elementLocators("EventTriggerEditor")).should(appear);
        $(elementLocators("IntegrationInputField")).should(exist).click();
        $(elementLocators("SecondIntegrationInTheList")).should(appear).click();
        SelenideElement Integration1 = $(elementLocators("IntegrationInputField")).should(exist).shouldHave(text("IBAN Number Verification"));
        String SelectedIntegration = Integration1.getText();
        $(elementLocators("SourceSelectionField")).should(exist).click();
        $(elementLocators("SourceList")).should(appear);
        $(elementLocators("SecondSourceFromTheList")).should(exist).click();
        $(elementLocators("SourceSelectionField")).shouldHave(text("IbanTestForm > Iban"));
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
        $(elementLocators("ChecklistComponentList")).should(exist).shouldHave(text(formToBeSelected)).$(byText("Assign to")).click();
        $(elementLocators("UserSelectionInput")).should(appear);
        $(elementLocators("DropDownButton")).should(exist).click();
        $(elementLocators("Popover")).should(appear, Duration.ofSeconds(30));
        $$(elementLocators("ListOfOptions")).findBy(text("GUI Tester")).should(appear, Duration.ofSeconds(30)).click();
        $(elementLocators("UserSelectionInput")).shouldHave(value("GUI Tester")).pressTab();
        $(byText("Assign")).should(exist).click();
        $(elementLocators("ConfirmationMessage")).should(appear);
        $(elementLocators("FormStateHelperText")).shouldHave(text("In Progress  |"));
        $(elementLocators("CloseChecklistButton")).should(exist).click();
        String versionID = $(elementLocators("FirstRowInTheGridContainer")).should(appear).getAttribute("data-id");
        $(elementLocators("TaskStatus/Overview")).should(appear,Duration.ofSeconds(3)).shouldHave(text("0/1"));
        assert versionID != null;
        $(elementLocators("GridContainer")).find(byAttribute("data-id", versionID)).should(exist);

        //Open the form with matching form name from MY Tasks table in the Launchpad in a new browser
        Selenide.executeJavaScript("window.open()");
        Selenide.switchTo().window(1);
        open("https://visualorbit.fireo.net");
        $(elementLocators("TasksCardInDashboard")).should(exist);
        $(elementLocators("TasksCardInDashboard")).find(byAttribute("data-form-name", formToBeSelected )).should(exist).
                $(elementLocators("FillForm")).click();
        $(elementLocators("DataFillForm")).should(appear);
        String validIban = "DE88 5005 0000 0001 0002 31";
        $("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").should(exist).setValue(validIban);
        if(!$("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").has(value(validIban))){
            $("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").should(exist).sendKeys(validIban);
        }
        $("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").shouldHave(value(validIban)).pressTab();
        $("#textField_form-user-b420757b-4c94-49ff-9c01-64bfb9afdd27").shouldHave(value(validIban));
        $("#textField_form-user-d379183d-295f-40ce-9e97-6232d595cf6c").shouldHave(value("HELADEFFXXX"));
        Thread.sleep(2000);
        $(elementLocators("SubmitDataButton")).should(exist).click();
        $(elementLocators("ConfirmButton")).should(appear).click();
         $(elementLocators("TasksCardInDashboard")).find(byAttribute("data-form-name", formToBeSelected )).should(disappear);
        closeWindow();

        //Switch to default tab and verify the form state and do start workflow
        Selenide.switchTo().window(0);
        refresh();
        $(elementLocators("GridContainer")).find(byAttribute("data-id", versionID)).should(exist)
                .$(elementLocators("ShowChecklistButton")).click();
        $(elementLocators("FormStateHelperText")).shouldHave(text("Completed  |  "));
        $(elementLocators("StartWorkFlowButton")).should(exist).click();
        $(elementLocators("ConfirmationMessage")).should(appear);
        $(byText("samplePDF.pdf")).click();
        $(byText("Loading PDF Preview")).should(disappear);
        $(elementLocators("IbanBlock")).shouldHave(text(validIban));
    }
}
