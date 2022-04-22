package com.vo.checklist;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.commands.UploadFile;
import com.codeborne.selenide.commands.UploadFileFromClasspath;
import com.vo.BaseTest;
import com.vo.formdesign.FileUploadFieldTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Interaction;
import org.openqa.selenium.interactions.PointerInput;

import java.io.File;
import java.time.Duration;

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
        $(elementLocators("PreviewChecklistButton")).should(appear);
        $(elementLocators("PublishChecklistTemplateButton")).should(exist);

        SelenideElement sourceFormEle = $(elementLocators("SourceFormElement"));
        SelenideElement sourceLabel = $(elementLocators("SourceLabelElement"));
        SelenideElement targetChecklistFlow = $(elementLocators("TargetChecklistFlow"));

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
        $(byText(elementLocators("SelectAForm"))).should(appear);
        $(byText(elementLocators("ChooseFromLibrary"))).click();
        $(elementLocators("FormsGridContainer")).should(appear); //Forms available in Library will appear
        $(elementLocators("FormsAvailableInTable")).should(exist).getSize();
        String ibanTestFormName = "IbanTestForm";
        SelenideElement ibanFormRow = $$(".MuiDataGrid-row [data-field='formName']").findBy( text(ibanTestFormName));
        String selectedForm1 = ibanFormRow.getText();
        System.out.println(selectedForm1);


        String form1DataID = ibanFormRow.parent().should(exist).getAttribute("data-id");
        ibanFormRow.parent().click(); //Select the first form available in the list
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").shouldHave(text(ibanTestFormName)); //Verify whether the selected form is available in the Checklist flow or not
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
        SelenideElement sourcePDF = $(elementLocators("PDFGenerator"));
        SelenideElement targetPDF = $(elementLocators("TargetPostProcessing"));
        int sourcePDFXOffset = sourcePDF.getLocation().getX();
        int sourcePDFYOffset = sourcePDF.getLocation().getY();
        int targetPDFXOffset = targetPDF.getLocation().getX();
        int targetPDFYOffset = targetPDF.getLocation().getY();

        int PDFXOffset = (targetPDFXOffset-sourcePDFXOffset)+50;
        int PDFYOffset=  (targetPDFYOffset-sourcePDFYOffset);

        actions().clickAndHold(sourcePDF).moveToElement(targetPDF).build().perform();
        actions().moveByOffset(PDFXOffset,PDFYOffset).release().build().perform();

        $(byText(elementLocators("UploadPDFTemplateInUSLetterOrA4PageFormat"))).should(appear);

        $(elementLocators("UploadPdfInput")).uploadFromClasspath("samplePDF.pdf");
        $(elementLocators("AttachFileIcon")).should(appear);
        $(elementLocators("FileUploadedMessage")).should(appear);
        $(elementLocators("SubmitButton")).click(); //Click on Submit Button
        $(elementLocators("CancelBtn")).should(exist).click(); //Click on Cancel Button

        SelenideElement Iban = $(elementLocators("Iban"));
        SelenideElement Bic = $(elementLocators("Bic"));
        SelenideElement Bank = $(elementLocators("Bank"));
        SelenideElement targetLocation = $(elementLocators("targetPDFTemplateSurface"));
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

        /*
        all of following variants do not work, so we decided to workaround drag and drop issues with explicit gui actions like click on button and fill positions into text fields
        PointerInput p = new PointerInput(PointerInput.Kind.MOUSE, "MouseOnPDFMapper");
        Interaction i = p.createPointerMove(Duration.ofSeconds(1), PointerInput.Origin.fromElement(Iban), 2, 2);

        //actions().tick(i).clickAndHold().moveToElement(targetLocation).build().perform();
        //actions().moveByOffset(targetIbanX, targetIbanY).release().build().perform();

        actions().tick(p.createPointerMove(Duration.ofSeconds(1), PointerInput.Origin.fromElement(Iban), 2, 2))
                .tick(p.createPointerDown(PointerInput.MouseButton.LEFT.ordinal()))
                .tick(p.createPointerMove(Duration.ofSeconds(5),
                        PointerInput.Origin.fromElement(Iban), 300, 50))
                .release()
                .build().perform();

        int targetBicX = (targetLocX-BicSourceX)+50;
        int targetBicY = (BicSourceY-targetLocY)+20;
        actions().clickAndHold(Bic).moveToElement(targetLocation).build().perform();
        actions().moveByOffset(targetBicX, targetBicY).release().build().perform();

        */

        //make hidden add buttons visible for the test user
        String js = "var els = document.querySelectorAll('.btn_AddSrc')\n" +
                "for(var idx = 0; idx < els.length; idx ++) {els[idx].style.visibility = 'inherit' }";
        executeJavaScript(js);
        $("#Iban .btn_AddSrc").shouldBe(visible).click();
        SelenideElement pageBox = $(".vo-page-container").shouldBe(visible).find(byText("Iban")).should(appear);
        actions().moveToElement(pageBox).click().build().perform();
        $("#txt_PosLeft").should(appear).setValue("30").pressTab();
        $("#txt_PosTop").should(appear).setValue("100").pressTab();

    }
}
