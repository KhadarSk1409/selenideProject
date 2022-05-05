package com.vo.checklist;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Checklist creation with new form")
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

        $(elementLocators("PreviewChecklistButton")).should(appear);
        $(elementLocators("PublishChecklistTemplateButton")).should(exist);

        SelenideElement sourceFormEle = $(elementLocators("SourceFormElement"));
        SelenideElement sourceLabel = $(elementLocators("SourceLabelElement"));
        SelenideElement targetChecklistFlow = $(elementLocators("TargetChecklistFlow"));

        //Get the X and Y offset's of both source and target elements
        int targetXOffset = targetChecklistFlow.getLocation().getX();
        int targetYOffset = targetChecklistFlow.getLocation().getY();
        int sourceXOffset = sourceFormEle.getLocation().getX();
        int sourceYOffset = sourceFormEle.getLocation().getY();
        int sourceLabelXOffset = sourceLabel.getLocation().getX();
        int sourceLabelYOffset = sourceLabel.getLocation().getY();


        actions().clickAndHold(sourceFormEle).moveToElement(targetChecklistFlow).build().perform(); //Click n hold the source element and move to target element

        //Find the xOffset and yOffset difference to find x and y offset needed in which from object required to dragged and dropped
        int Xoffset = (targetXOffset-sourceXOffset)+30;
        int Yoffset = (targetYOffset-sourceYOffset)-20;

        actions().moveByOffset(Xoffset,Yoffset).release().build().perform(); //Release the source element as per the offset's provided

        $(byText(elementLocators("SelectAForm"))).should(appear); // Form selection wizard will appear
        $(byText(elementLocators("CreateANewForm"))).click(); //Click on Create a New Form
        $(elementLocators("FormCreationWizard")).should(appear); //New form creation wizard will appear
        $(elementLocators("FormTitleInputField")).should(exist).setValue("DemoForm"); //Enter Form Title
        $(elementLocators("DescriptionInputField")).setValue("This is a sample test form"); //Enter Form Description
        $(elementLocators("CreateNewFormButton")).should(exist).shouldBe(enabled).click(); //Click on Create Form
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text("DemoForm")); //Verify whether the created form is available in the Checklist flow or not

        //Drag and Drop LABEL field to Checklist flow
        actions().clickAndHold(sourceLabel).moveToElement(targetChecklistFlow).build().perform();
        int labelXOffset = (targetXOffset-sourceLabelXOffset)+50;
        int labelYOffset= (sourceLabelYOffset-targetYOffset)+40;
        actions().moveByOffset(labelXOffset,labelYOffset).release().build().perform();


    }
}
