package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Checklists Demo Test 2")
public class VerifyChecklistCreationWithNewFormTest extends BaseTest {

    @Test
    @DisplayName("Open the form in Checklists")
    @Order(1)
    public void openChecklistDesigner(){
        open("/checklistdesigner2/demo");
    }

    @Test
    @DisplayName("Verify Create checklist with new form")
    @Order(2)
    public void createChecklistWithNewForm() {

        SelenideElement SourceEle = $("[data-rbd-draggable-id='FORM']"); //Form field
        SelenideElement sourceLabel = $("[data-rbd-draggable-id='LABEL']"); //Label field
        SelenideElement TargetEle = $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID']"); //Target area

        //Get the X and Y offset's of both source and target elements
        int targetXOffset = TargetEle.getLocation().getX();
        int targetYOffset = TargetEle.getLocation().getY();
        int sourceXOffset = SourceEle.getLocation().getX();
        int sourceYOffset = SourceEle.getLocation().getY();
        int sourceLabelXOffset = sourceLabel.getLocation().getX();
        int sourceLabelYOffset = sourceLabel.getLocation().getY();

        $("button[title='Preview Checklist']").should(appear); //Preview button should exist
        $("#btnCheckListTemplatePublish").should(exist); //Publish button should exist

        actions().clickAndHold(SourceEle).moveToElement(TargetEle).build().perform(); //Click n hold the source element and move to target element

        //Find the xOffset and yOffset difference to find x and y offset needed in which from object required to dragged and dropped
        int Xoffset = (targetXOffset-sourceXOffset)+30;
        int Yoffset = (targetYOffset-sourceYOffset)-20;

        actions().moveByOffset(Xoffset,Yoffset).release().build().perform(); //Release the source element as per the offset's provided

        $(byText("Select a Form")).should(appear); // Form selection wizard will appear
        $(byText("Create a new Form")).click(); //Click on Create a New Form
        $("#dlgSearchForm_Content").should(appear); //New form creation wizard will appear
        $("#wizard-formTitle").should(exist).setValue("DemoForm"); //Enter Form Title
        $("#wizard-formHelp").setValue("This is a sample test form"); //Enter Form Description
        $("#btnCreateForm").should(exist).shouldBe(enabled).click(); //Click on Create Form
        $("[data-rbd-droppable-id='TARGET_FORM_LIST_ID'] nav div div:nth-child(3)").shouldHave(text("DemoForm")); //Verify whether the created form is available in the Checklist flow or not

        //Drag and Drop LABEL field to Checklist flow
        actions().clickAndHold(sourceLabel).moveToElement(TargetEle).build().perform();
        int labelXOffset = (targetXOffset-sourceLabelXOffset)+50;
        int labelYOffset= (sourceLabelYOffset-targetYOffset)+40;
        actions().moveByOffset(labelXOffset,labelYOffset).release().build().perform();


    }
}
