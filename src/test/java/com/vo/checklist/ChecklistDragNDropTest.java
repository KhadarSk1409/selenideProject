package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsChecklistCreation.createChecklistWithNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Checklists Drag and Drop functionality Demo Tests")
public class ChecklistDragNDropTest extends BaseTest {

    @Test
    @DisplayName("Verify Drag and Drop Functionality")
    @Order(1)
    public void verifyDragNDrop() {

        createChecklistWithNewForm(); //A new form will be created during new checklist creation

        $(elementLocators("PreviewChecklistButton")).should(appear);
        $(elementLocators("PublishChecklistTemplateButton")).should(exist);

        SelenideElement sourceFormEle = $(elementLocators("SourceFormElement"));
        SelenideElement sourceLabel = $(elementLocators("SourceLabelElement"));
        SelenideElement sourceTask = $(elementLocators("SourceTaskElement"));
        SelenideElement sourceEventTrigger = $(elementLocators("SourceEventTrigger"));
        SelenideElement sourcePDFGenerator = $(elementLocators("SourcePDFGenerator"));
        SelenideElement targetChecklistFlow = $(elementLocators("TargetChecklistFlow"));
        SelenideElement targetPostProcessingFlow = $(elementLocators("TargetPostProcessingFlow"));

        //Get the initial locations of Source and Target elements
        int sourceFormXOffset = sourceFormEle.getLocation().getX();
        int sourceFormYOffset = sourceFormEle.getLocation().getY();
        int sourceLabelXOffset = sourceLabel.getLocation().getX();
        int sourceLabelYOffset = sourceLabel.getLocation().getY();
        int sourceTaskXOffset = sourceTask.getLocation().getX();
        int sourceTaskYOffset = sourceTask.getLocation().getY();
        int sourceETXOffset = sourceEventTrigger.getLocation().getX();
        int sourceETYOffset = sourceEventTrigger.getLocation().getY();
        int targetXOffset = targetChecklistFlow.getLocation().getX();
        int targetYOffset = targetChecklistFlow.getLocation().getY();

        //Drag and Drop FORM to checklist flow
        actions().clickAndHold(sourceFormEle).moveToElement(targetChecklistFlow).build().perform();
        int Xoffset = (targetXOffset-sourceFormXOffset);
        int Yoffset = (targetYOffset-sourceFormYOffset);
        actions().moveByOffset(Xoffset,Yoffset).release().build().perform();

        $(byText(elementLocators("SelectAForm"))).should(appear);
        $(byText(elementLocators("ChooseFromLibrary"))).click();
        $(elementLocators("FormsGridContainer")).should(appear); //Forms available in Library will appear
        $(elementLocators("FormsAvailableInTable")).should(exist).getSize();
        String selectedFormName = $(elementLocators("FirstFormNameInTheTable")).getText();
        System.out.println(selectedFormName);
        $(elementLocators("FirstFormAvailableInTable")).click(); //Select the first form available in the list
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(selectedFormName)); //Verify whether the selected form is available in the Checklist flow or not

        //Drag and Drop LABEL field to Checklist flow
        actions().clickAndHold(sourceLabel).moveToElement(targetChecklistFlow).build().perform();
        int labelXOffset = (targetXOffset-sourceLabelXOffset)+50;
        int labelYOffset= (sourceLabelYOffset-targetYOffset)+40;
        actions().moveByOffset(labelXOffset,labelYOffset).release().build().perform();

        //Drag and Drop TASK field to Checklist flow
        actions().clickAndHold(sourceTask).moveToElement(targetChecklistFlow).build().perform();
        int taskXOffset = (targetXOffset-sourceTaskXOffset)+10;
        int taskYOffset= (sourceTaskYOffset-targetYOffset)-50;
        actions().moveByOffset(taskXOffset,taskYOffset).release().build().perform();

        //Drag n Drop second form to the flow
        actions().clickAndHold(sourceFormEle).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(Xoffset,Yoffset).release().build().perform();

        $(byText(elementLocators("SelectAForm"))).should(appear);
        $(byText(elementLocators("ChooseFromLibrary"))).click();
        $(elementLocators("FormsGridContainer")).should(appear); //Forms available in Library will appear
        $(elementLocators("FormsAvailableInTable")).should(exist).getSize();
        String selectedForm2 = $(elementLocators("SecondFormNameInTheTable")).getText();
        System.out.println(selectedForm2);
        $(elementLocators("SecondFormAvailableInTable")).click(); //Select the Second form available in the list
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(selectedForm2)); //Verify whether the selected form is available in the Checklist flow or not

        //Drag n Drop Event Trigger
        actions().clickAndHold(sourceEventTrigger).moveToElement(targetChecklistFlow).build().perform();
        int ETXOffset = (targetXOffset-sourceETXOffset)+50;
        int ETYOffset=  (sourceETYOffset-targetYOffset)-100;
        actions().moveByOffset(ETXOffset,ETYOffset).release().build().perform();
        $(elementLocators("OkButton")).should(appear).click();

        //POST PROCESSING FLOW
        //Drag n Drop PDF Generator to Post Processing flow and verify cancel
        $(elementLocators("PDFGenerator")).doubleClick();
        $(byText(elementLocators("UploadPDFTemplateInUSLetterOrA4PageFormat"))).should(appear);
        $(elementLocators("CancelBtn")).should(exist).click(); //Click on Cancel Button

    }
}
