package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Checklists Drag and Drop functionality Demo Tests")
public class ChecklistDragNDropTest extends BaseTest {

    @Test
    @DisplayName("Open the form in CheckLists")
    @Order(1)
    public void openExistingForm(){

        open("/checklistdesigner2/demo");
    }

    @Test
    @DisplayName("Verify Drag and Drop Functionality")
    @Order(2)
    public void verifyDragNDrop() {

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

        //POST PROCESSING FLOW
        //Drag n Drop PDF Generator to Post Processing flow and verify cancel
        //Get the Source and Target offset's from changed positions
        SelenideElement sourcePDF = $(elementLocators("PDFGenerator"));
        SelenideElement targetPDF = $(elementLocators("TargetPostProcessing"));
        int sourcePDFXOffset = sourcePDF.getLocation().getX();
        int sourcePDFYOffset = sourcePDF.getLocation().getY();
        int targetPDFXOffset = targetPDF.getLocation().getX();
        int targetPDFYOffset = targetPDF.getLocation().getY();

        int PDFXOffset = (targetPDFXOffset-sourcePDFXOffset)+50;
        int PDFYOffset=  (targetPDFYOffset-sourcePDFYOffset)+20;

        System.out.println("sourcePDF xoffset is " +sourcePDFXOffset);
        System.out.println("sourcePDF yoffset is " +sourcePDFYOffset);
        System.out.println("targetPostProcessingFlow xoffset is " +targetPDFXOffset);
        System.out.println("targetPostProcessingFlow yoffset is " +targetPDFYOffset);
        System.out.println("PDF xoffset is " +PDFXOffset);
        System.out.println("PDF yoffset is " +PDFYOffset);

        actions().clickAndHold(sourcePDF).moveToElement(targetPDF).build().perform();

        actions().moveByOffset(PDFXOffset,PDFYOffset).release().build().perform();

      //Element is getting closer to the target @359, 215 but not able to drop at correct region
        $(byText(elementLocators("UploadPDFTemplateInUSLetterOrA4PageFormat"))).should(appear);
        $(elementLocators("CancelBtn")).should(exist).click(); //Click on Cancel Button

    }
}
