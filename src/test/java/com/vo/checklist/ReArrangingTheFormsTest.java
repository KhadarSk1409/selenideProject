package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.actions;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsChecklistCreation.createNewChecklist;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the re-arranging of multiple forms in the checklist flow")
public class ReArrangingTheFormsTest extends BaseTest {

    @Test
    @DisplayName("Verify the re-arrangement of forms in checklist flow")
    @Order(1)
    public void verifyReArrangingTheFormsInChecklist(){
        createNewChecklist();
        $(elementLocators("PreviewChecklistButton")).should(appear);
        $(elementLocators("PublishChecklistTemplateButton")).should(exist);

        SelenideElement sourceFormEle = $(elementLocators("SourceFormElement"));
        SelenideElement sourceLabel = $(elementLocators("SourceLabelElement"));
        SelenideElement sourceCondition = $(elementLocators("SourceConditionElement"));
        SelenideElement sourceEventTrigger = $(elementLocators("SourceEventTrigger"));
        SelenideElement targetChecklistFlow = $(elementLocators("TargetChecklistFlow"));

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
        $(byText(elementLocators("SelectAForm"))).should(appear);
        $(byText(elementLocators("ChooseFromLibrary"))).click();
        $(elementLocators("FormsGridContainer")).should(appear); //Forms available in Library will appear
        $(elementLocators("FormsAvailableInTable")).should(exist).getSize();
        String selectedForm1 = $(elementLocators("FirstFormNameInTheTable")).getText();
        System.out.println(selectedForm1);
        String form1DataID = $(elementLocators("FirstFormAvailableInTable")).should(exist).getAttribute("data-id");
        $(elementLocators("FirstFormAvailableInTable")).click(); //Select the first form available in the list
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(selectedForm1)); //Verify whether the selected form is available in the Checklist flow or not
        assert form1DataID != null;
        SelenideElement form1 = $(elementLocators("TargetListInChecklistFlow")).find(byAttribute("id",form1DataID)).should(exist);

        //Drag and Drop LABEL field to Checklist flow
        int labelXOffset = (targetXOffset-sourceLabelXOffset)+50;
        int labelYOffset= (sourceLabelYOffset-targetYOffset)+40;
        actions().clickAndHold(sourceLabel).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(labelXOffset,labelYOffset).release().build().perform();

        //Drag n Drop second form to the flow
        actions().clickAndHold(sourceFormEle).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(Xoffset,Yoffset).release().build().perform();
        //Select a form available in the list
        $(byText(elementLocators("SelectAForm"))).should(appear);
        $(byText(elementLocators("ChooseFromLibrary"))).click();
        $(elementLocators("FormsGridContainer")).should(appear); //Forms available in Library will appear
        $(elementLocators("FormsAvailableInTable")).should(exist).getSize();
        String selectedForm2 = $(elementLocators("SecondFormNameInTheTable")).getText();
        System.out.println(selectedForm2);
        String form2DataID =  $(elementLocators("SecondFormAvailableInTable")).should(exist).getAttribute("data-id");
        $(elementLocators("SecondFormAvailableInTable")).click(); //Select the Second form available in the list
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(selectedForm2)); //Verify whether the selected form is available in the Checklist flow or not
        assert form2DataID != null;
        SelenideElement form2 = $(elementLocators("TargetListInChecklistFlow")).find(byAttribute("id",form2DataID)).should(exist);

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
        $(elementLocators("OkButton")).should(appear).click();

        //RE-ARRANGE FORM-1
        actions().clickAndHold(form1).moveToElement(targetChecklistFlow).build().perform();
        actions().moveByOffset(FinalXoffset+20,FinalYoffset+10).release().build().perform();

    }
}
