package com.vo.checklist;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.commands.UploadFile;
import com.codeborne.selenide.commands.UploadFileFromClasspath;
import com.vo.BaseTest;
import com.vo.formdesign.FileUploadFieldTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.io.File;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify drag and drop functionality in PDF Mapper")
public class PdfMapperDragAndDropTest extends BaseTest {

    @Test
    @DisplayName("Open the existing form in Checklist")
    @Order(1)
    public void openExistingChecklistForm(){

        open("/checklistdesigner2/demo");

    }

    @Test
    @DisplayName("Verify PDF Mapper Drag and Drop")
    @Order(2)
    public void verifyPdfMapperDragNDrop(){
        $("button[title='Preview Checklist']").should(appear); //Preview button should exist
        $("#btnCheckListTemplatePublish").should(exist);

        SelenideElement sourceFormEle = $("[data-rbd-draggable-id='FORM']");
        SelenideElement sourceLabel = $("[data-rbd-draggable-id='LABEL']");
        SelenideElement targetChecklistFlow = $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID']");

        //Get the initial locations of Source and Target elements
        int sourceFormXOffset = sourceFormEle.getLocation().getX();
        int sourceFormYOffset = sourceFormEle.getLocation().getY();
        int sourceLabelXOffset = sourceLabel.getLocation().getX();
        int sourceLabelYOffset = sourceLabel.getLocation().getY();
        int targetXOffset = targetChecklistFlow.getLocation().getX();
        int targetYOffset = targetChecklistFlow.getLocation().getY();

        //Drag and Drop FORM to checklist flow
        int Xoffset = (targetXOffset-sourceFormXOffset);
        int Yoffset = (targetYOffset-sourceFormYOffset);
        actions().clickAndHold(sourceFormEle).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(Xoffset,Yoffset).release().build().perform();
        //Select a form available in the list
        $(byText("Select a Form")).should(appear);
        $(byText("Choose from Library")).click();
        $(".MuiDataGrid-main").should(appear); //Forms available in Library will appear
        $(".MuiDataGrid-row").should(exist).getSize();
        String selectedForm1 = $(".MuiDataGrid-row:nth-of-type(1) [data-field='formName'] h6").getText();
        System.out.println(selectedForm1);
        String form1DataID = $(".MuiDataGrid-row:nth-of-type(1)").should(exist).getAttribute("data-id");
        $(".MuiDataGrid-row:nth-of-type(1)").click(); //Select the first form available in the list
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").shouldHave(text(selectedForm1)); //Verify whether the selected form is available in the Checklist flow or not
        assert form1DataID != null;
        SelenideElement form1 = $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").find(byAttribute("id",form1DataID)).should(exist);


        //Drag and Drop LABEL field to Checklist flow
        int labelXOffset = (targetXOffset-sourceLabelXOffset)+50;
        int labelYOffset= (sourceLabelYOffset-targetYOffset)+40;
        actions().clickAndHold(sourceLabel).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(labelXOffset,labelYOffset).release().build().perform();

        //POST PROCESSING FLOW
        //Drag n Drop PDF Generator to Post Processing flow and verify cancel
        //Get the Source and Target offset's from changed positions
        SelenideElement sourcePDF = $("#PDF_GENERATOR_undefined");
        SelenideElement targetPDF = $(".MuiCardContent-root [data-rbd-droppable-id='TARGET_POST_PROCESSING_LIST_ID']");
        int sourcePDFXOffset = sourcePDF.getLocation().getX();
        int sourcePDFYOffset = sourcePDF.getLocation().getY();
        int targetPDFXOffset = targetPDF.getLocation().getX();
        int targetPDFYOffset = targetPDF.getLocation().getY();

        int PDFXOffset = (targetPDFXOffset-sourcePDFXOffset)+50;
        int PDFYOffset=  (targetPDFYOffset-sourcePDFYOffset);

        actions().clickAndHold(sourcePDF).moveToElement(targetPDF).build().perform();
        actions().moveByOffset(PDFXOffset,PDFYOffset).release().build().perform();

        $(byText("Upload PDF Template in US Letter or A4 Page Format")).should(appear);
        SelenideElement UploadPDFDialogue = $("#dlg_PDFFileUpload [data-testid='CloudUploadIcon']");

        $("#dlg_PDFFileUpload input").uploadFromClasspath("samplePDF.pdf");
        $("[data-testid='AttachFileIcon']").should(appear);
        $("#client-snackbar").should(appear);
        $("#dlg_PDFFileUpload button:nth-child(1)").click(); //Click on Submit Button
//#dlg_PDFEditor
        $("#dlg_PDFFileUpload button:nth-child(2)").should(exist).click(); //Click on Cancel Button
//        $("[data-rbd-draggable-id='samplePDF.pdf'] [title='Edit PDF Mapping']").should(exist).click();

        SelenideElement Iban = $("ul li li li li [aria-label='Iban ELEMENT TextField']");
        SelenideElement Bic = $("ul li li li li [aria-label='Bic ELEMENT TextField']");
        SelenideElement Bank = $("ul li li li li [aria-label='Bank ELEMENT TextField']");
        SelenideElement targetLocation = $("#vo_pdf_template_designer_surface");
        Iban.should(exist);
        int IbanSourceX = Iban.getLocation().getX();
        int IbanSourceY = Iban.getLocation().getY();
        int BicSourceX = Bic.getLocation().getX();
        int BicSourceY = Bic.getLocation().getY();
        int BankSourceX = Bank.getLocation().getX();
        int BankSourceY = Bank.getLocation().getY();
        int targetLocX = targetLocation.getLocation().getX();
        int targetLocY = targetLocation.getLocation().getY();


        int targetIbanX = (targetLocX-IbanSourceX)+20;
        int targetIbanY = (IbanSourceY+targetLocY);

        actions().clickAndHold(Iban).moveToElement(targetLocation).build().perform();
        actions().moveByOffset(targetIbanX, targetIbanY).release().build().perform();

        int targetBicX = (targetLocX-BicSourceX)+50;
        int targetBicY = (BicSourceY-targetLocY)+20;
        actions().clickAndHold(Bic).moveToElement(targetLocation).build().perform();
        actions().moveByOffset(targetBicX, targetBicY).release().build().perform();

    }
}
