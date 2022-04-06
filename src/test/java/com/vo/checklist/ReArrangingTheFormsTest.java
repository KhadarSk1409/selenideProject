package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
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
        SelenideElement sourceCondition = $("[data-rbd-draggable-id='CONDITION']");
        SelenideElement sourceEventTrigger = $("[data-rbd-droppable-id='EVENT_TRIGGER']");
        SelenideElement targetChecklistFlow = $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID']");

        //Get the initial locations of Source and Target elements
        int sourceFormXOffset = sourceFormEle.getLocation().getX();
        int sourceFormYOffset = sourceFormEle.getLocation().getY();
        int sourceLabelXOffset = sourceLabel.getLocation().getX();
        int sourceLabelYOffset = sourceLabel.getLocation().getY();
        int sourceConditionXOffset = sourceCondition.getLocation().getX();
        int sourceConditionYOffset = sourceCondition.getLocation().getY();
        int sourceETXOffset = sourceEventTrigger.getLocation().getX();
        int sourceETYOffset = sourceEventTrigger.getLocation().getY();
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

        //Drag n Drop second form to the flow
        actions().clickAndHold(sourceFormEle).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(Xoffset,Yoffset).release().build().perform();
        //Select a form available in the list
        $(byText("Select a Form")).should(appear);
        $(byText("Choose from Library")).click();
        $(".MuiDataGrid-main").should(appear); //Forms available in Library will appear
        $(".MuiDataGrid-row").should(exist).getSize();
        String selectedForm2 = $(".MuiDataGrid-row:nth-of-type(2) [data-field='formName'] h6").getText();
        System.out.println(selectedForm2);
        String form2DataID = $(".MuiDataGrid-row:nth-of-type(2)").should(exist).getAttribute("data-id");
        $(".MuiDataGrid-row:nth-of-type(2)").click(); //Select the Second form available in the list
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").shouldHave(text(selectedForm2)); //Verify whether the selected form is available in the Checklist flow or not
        assert form2DataID != null;
        SelenideElement form2 = $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").find(byAttribute("id",form2DataID)).should(exist);

        //RE-ARRANGE FORM-1
        int form1Xoffset = form1.getLocation().getX();
        int form1Yoffset = form1.getLocation().getY();
        int FinalXoffset = (targetXOffset-form1Xoffset)+100;
        int FinalYoffset = (form1Yoffset-targetYOffset);
        actions().clickAndHold(form1).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(FinalXoffset,FinalYoffset).release().build().perform();

        //Drag and Drop Condition to Checklist flow
        int ConditionXOffset = (targetXOffset-sourceConditionXOffset);
        int ConditionYOffset= (sourceConditionYOffset-targetYOffset);
        actions().clickAndHold(sourceCondition).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(ConditionXOffset,ConditionYOffset).release().build().perform();

        //RE-ARRANGE FORM-2
        int form2Xoffset = form2.getLocation().getX();
        int form2Yoffset = form2.getLocation().getY();
        int targetXoffset = (targetXOffset-form2Xoffset)+100;
        int targetYoffset = (form2Yoffset-targetYOffset)+50;
        actions().clickAndHold(form2).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(targetXoffset,targetYoffset).release().build().perform();

        //Drag n Drop Event Trigger
        int ETXOffset = (targetXOffset-sourceETXOffset)+50;
        int ETYOffset=  (sourceETYOffset-targetYOffset)-100;
        actions().clickAndHold(sourceEventTrigger).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(ETXOffset,ETYOffset).release().build().perform();

        //RE-ARRANGE FORM-1
        actions().clickAndHold(form1).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(FinalXoffset+20,FinalYoffset+10).release().build().perform();

    }
}
