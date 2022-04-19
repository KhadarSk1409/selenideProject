package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the components actions in checklist flow")
public class VerifyComponentsActionsInChecklistFlowTest extends BaseTest {

    @Test
    @DisplayName("Open the exisitng form in checklist flow")
    @Order(1)
    public void openExistingForm(){
        open("/checklistdesigner2/demo");
    }

    @Test
    @DisplayName("Verify Edit actions in Checklist components")
    public void verifyComponentsActions(){
        $("button[title='Preview Checklist']").should(appear); //Preview button should exist
        $("#btnCheckListTemplatePublish").should(exist);

        SelenideElement sourceFormEle = $("[data-rbd-draggable-id='FORM']"); //Form field
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
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").find(byAttribute("id",formDataID)).$(".MuiListItemText-root").hover();

        $(".editSecondaryText").shouldBe(visible).click();
        $("#textField_secondaryTxt").should(appear).setValue("Actions Test").pressEnter();
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] .MuiList-root").find(byAttribute("id",formDataID))
                .$(".MuiListItemText-root p").shouldHave(text("Actions Test"));

        //Drag and Drop LABEL field to Checklist flow
        actions().clickAndHold(sourceLabel).moveToElement(targetChecklistFlow).build().perform();
        int labelXOffset = (targetXOffset-sourceLabelXOffset)+50;
        int labelYOffset= (sourceLabelYOffset-targetYOffset)+40;
        actions().moveByOffset(labelXOffset,labelYOffset).release().build().perform();
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] div[draggable='false']:nth-of-type(2) .MuiListItemText-root").should(appear, Duration.ofSeconds(2))
                .shouldHave(text("LABEL")).hover();
        $(".editPrimaryText").shouldBe(visible).click();
        $("#textField_primaryTxt").should(appear).setValue(" 01").pressEnter();
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] div[draggable='false']:nth-of-type(2) .MuiListItemText-root").shouldHave(text("LABEL 01"));
        $(".editSecondaryText").shouldBe(visible).click();
        $("#textField_secondaryTxt").shouldBe(visible).setValue("Sample Test Description").pressEnter();
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] div[draggable='false']:nth-of-type(2) .MuiListItemText-root p span").shouldHave(text("Sample Test Description"));

    }
}
