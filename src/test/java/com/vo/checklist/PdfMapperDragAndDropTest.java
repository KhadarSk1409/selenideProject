package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Dimension;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
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
        SelenideElement ibanFormRow = $$(elementLocators("FormRows")).findBy( text(ibanTestFormName));
        String selectedForm1 = ibanFormRow.getText();
        System.out.println("Selected Form is: " +selectedForm1);

        String form1DataID = ibanFormRow.parent().should(exist).getAttribute("data-id");
        ibanFormRow.parent().click(); //Select the first form available in the list
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(ibanTestFormName)); //Verify whether the selected form is available in the Checklist flow or not
        assert form1DataID != null;
        SelenideElement form1 = $(elementLocators("TargetListInChecklistFlow")).find(byAttribute("id",form1DataID)).should(exist);


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
        $(Iban).should(exist);
        $(Bic).should(exist);
        $(Bank).should(exist);

        //Make hidden add buttons visible for the test user to add multiple components into the pdf
        String js = "var els = document.querySelectorAll('.btn_AddSrc')\n" +
                "for(var idx = 0; idx < els.length; idx ++) {els[idx].style.visibility = 'inherit' }";
        executeJavaScript(js);
        //Add Component: 1
        $(elementLocators("AddIbanButton")).shouldBe(visible).click();
        SelenideElement IbanBox = $(elementLocators("PDFContainer")).shouldBe(visible).find(byText("Iban")).should(appear);
        $(elementLocators("LeftPosition")).should(appear).setValue("75").pressTab();
        $(elementLocators("TopPosition")).should(appear).setValue("100").pressTab();

        //Component: 2
        String js1 = "var els = document.querySelectorAll('.btn_AddSrc')\n" +
                "for(var idx = 0; idx < els.length; idx ++) {els[idx].style.visibility = 'inherit' }";
        executeJavaScript(js1);
        $(elementLocators("AddBankButton")).shouldBe(visible).click();
        SelenideElement BankBox = $(elementLocators("PDFContainer")).shouldBe(visible).find(byText("Bank")).should(appear);
        actions().moveToElement(BankBox).click().build().perform();
        $(elementLocators("LeftPosition")).should(appear).setValue("75").pressTab();
        $(elementLocators("TopPosition")).should(appear).setValue("150").pressTab();

        //Component: 3
        String js2 = "var els = document.querySelectorAll('.btn_AddSrc')\n" +
                "for(var idx = 0; idx < els.length; idx ++) {els[idx].style.visibility = 'inherit' }";
        executeJavaScript(js2);
        $(elementLocators("AddBicButton")).shouldBe(visible).click();
        SelenideElement BicBox = $(elementLocators("PDFContainer")).shouldBe(visible).find(byText("Bic")).should(appear);
        actions().moveToElement(BicBox).click().build().perform();
        $(elementLocators("LeftPosition")).should(appear).setValue("75").pressTab();
        $(elementLocators("TopPosition")).should(appear).setValue("200").pressTab();

        //Verify deletion of added components in the pdf
        //Delete IbanBox from PDF
        $(IbanBox).should(exist).click();
        $(elementLocators("DeleteButton")).should(appear).click();
        $(IbanBox).shouldNot(exist);
        $(BicBox).should(exist);
        $(BankBox).should(exist);
        $(elementLocators("OkButton")).should(exist).click();

        //Re-Open the checklist form designer to verify whether Iban disappears or not
        $(elementLocators("ListOfPDFsAvailable")).shouldHave(text("samplePDF.pdf")).$(elementLocators("PenIconToEditPDF")).click();
        $(IbanBox).shouldNot(exist);
        Dimension BicBoxSize= $(BicBox).getRect().getDimension(); //Get the dimensions of Bic Box
        System.out.println("Initial Width and Height of BicBox is " +BicBoxSize);

        //Clear the existing values of BicBox and Re-Size by giving some random values
        $(BicBox).should(exist).click();
        selectAndClear(elementLocators("WidthField")).should(appear).setValue("400").pressTab();
        selectAndClear(elementLocators("HeightField")).setValue("60").pressTab();
        $(elementLocators("OkButton")).should(exist).click();

        //Re-Open the designer to verify re-sized block have same width and height
        $(elementLocators("ListOfPDFsAvailable")).shouldHave(text("samplePDF.pdf")).$(elementLocators("PenIconToEditPDF")).click();
        $(BicBox).should(exist);
        $(BankBox).should(exist);
        int width = BicBox.getRect().getWidth();
        int height = BicBox.getRect().getHeight();
        System.out.println("Changed Width is "+ width+" Changed Height is "+height );

    }
}
