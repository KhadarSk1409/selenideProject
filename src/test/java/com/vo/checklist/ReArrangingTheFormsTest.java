package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.actions;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the re-arranging of multiple forms in the checklist flow")
public class ReArrangingTheFormsTest extends BaseTest {

    @Test
    @DisplayName("Open the existing form in checklists")
    @Order(1)
    public void openExistingChecklist(){
        open("/checklistdesigner2/demo");
    }

    @Test
    @DisplayName("Verify the forms re-arrangement of forms")
    @Order(2)
    public void verifyReArrangingTheFormsInChecklist(){

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
        actions().clickAndHold(sourceFormEle).moveToElement(targetChecklistFlow).build().perform();
        int Xoffset = (targetXOffset-sourceFormXOffset);
        int Yoffset = (targetYOffset-sourceFormYOffset);
        actions().moveByOffset(Xoffset,Yoffset).release().build().perform();

        $(byText("Select a Form")).should(appear);
        $(byText("Choose from Library")).click();
        $(".MuiDataGrid-main").should(appear); //Forms available in Library will appear
        $(".MuiDataGrid-row").should(exist).getSize();
        String selectedForm1 = $(".MuiDataGrid-row:nth-of-type(1) [data-field='formName'] h6").getText();
        System.out.println(selectedForm1);
        String form1DataID = $(".MuiDataGrid-row:nth-of-type(1)").should(exist).getAttribute("data-id");
        $(".MuiDataGrid-row:nth-of-type(1)").click(); //Select the first form available in the list
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").shouldHave(text(selectedForm1)); //Verify whether the selected form is available in the Checklist flow or not

        //Drag and Drop LABEL field to Checklist flow
        actions().clickAndHold(sourceLabel).moveToElement(targetChecklistFlow).build().perform();
        int labelXOffset = (targetXOffset-sourceLabelXOffset)+50;
        int labelYOffset= (sourceLabelYOffset-targetYOffset)+40;
        actions().moveByOffset(labelXOffset,labelYOffset).release().build().perform();

        //Drag n Drop second form to the flow
        actions().clickAndHold(sourceFormEle).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(Xoffset,Yoffset).release().build().perform();

        $(byText("Select a Form")).should(appear);
        $(byText("Choose from Library")).click();
        $(".MuiDataGrid-main").should(appear); //Forms available in Library will appear
        $(".MuiDataGrid-row").should(exist).getSize();
        String selectedForm2 = $(".MuiDataGrid-row:nth-of-type(2) [data-field='formName'] h6").getText();
        System.out.println(selectedForm2);
        String form2DataID = $(".MuiDataGrid-row:nth-of-type(1)").should(exist).getAttribute("data-id");
        $(".MuiDataGrid-row:nth-of-type(2)").click(); //Select the Second form available in the list
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").shouldHave(text(selectedForm2)); //Verify whether the selected form is available in the Checklist flow or not


    }
}
