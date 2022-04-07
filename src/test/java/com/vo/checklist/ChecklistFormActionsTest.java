package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.actions;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Checklists Form Actions Verification Tests")
public class ChecklistFormActionsTest extends BaseTest {

    @Test
    @DisplayName("Open the form in Checklists")
    @Order(1)
    public void openExistingForm(){

        open("/checklistdesigner2/demo");
    }

    @Test
    @DisplayName("Verify checklist form actions")
    @Order(2)
    public void verifyChecklistFormActions() {
        $("button[title='Preview Checklist']").should(appear); //Preview button should exist
        $("#btnCheckListTemplatePublish").should(exist);

        SelenideElement sourceFormEle = $("[data-rbd-draggable-id='FORM']");
        SelenideElement targetChecklistFlow = $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID']");
        SelenideElement sourceLabel = $("[data-rbd-draggable-id='LABEL']"); //Label field

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
        String selectedFormName = $(".MuiDataGrid-row:nth-of-type(1) [data-field='formName'] h6").getText();
        System.out.println(selectedFormName);
        String formDataID = $(".MuiDataGrid-row:nth-of-type(1)").should(exist).getAttribute("data-id");
        $(".MuiDataGrid-row:nth-of-type(1)").click(); //Select the first form available in the list
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").shouldHave(text(selectedFormName)); //Verify whether the selected form is available in the Checklist flow or not
        assert formDataID != null;
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").find(byAttribute("id",formDataID))
                .should(exist).$(".previewItem").should(exist).click(); //Find the form with same ID and click on Preview Button
        $("#btnCloseDataFillForm").should(exist).click();
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").find(byAttribute("id",formDataID))
                .should(exist).$(".deleteItem").should(exist).click();
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").find(byAttribute("id",formDataID)).shouldNot(exist, Duration.ofSeconds(3));

        //Drag and Drop LABEL field to Checklist flow
        actions().clickAndHold(sourceLabel).moveToElement(targetChecklistFlow).build().perform();
        int labelXOffset = (targetXOffset-sourceLabelXOffset)+50;
        int labelYOffset= (sourceLabelYOffset-targetYOffset)+40;
        actions().moveByOffset(labelXOffset,labelYOffset).release().build().perform();

    }
}
