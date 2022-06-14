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
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsChecklistCreation.createNewChecklist;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the components actions in checklist flow")
public class VerifyComponentsActionsInChecklistFlowTest extends BaseTest {

    @Test
    @DisplayName("Verify Edit actions in Checklist components")
    public void verifyComponentsActions(){
        createNewChecklist();
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
        actions().clickAndHold(sourceFormEle).moveToElement(targetChecklistFlow).build().perform();
        int Xoffset = (targetXOffset-sourceFormXOffset);
        int Yoffset = (targetYOffset-sourceFormYOffset);
        actions().moveByOffset(Xoffset,Yoffset).release().build().perform();

        $(byText(elementLocators("SelectAForm"))).should(appear);
        $(byText(elementLocators("ChooseFromLibrary"))).click();
        $(elementLocators("FormsGridContainer")).should(appear); //Forms available in Library will appear
        $(elementLocators("FormsAvailableInTable")).should(exist).getSize();
        String selectedFormName = $(elementLocators("FirstFormNameInTheTable")).getText();
        System.out.println(selectedFormName);
        String formDataID = $(elementLocators("FirstFormAvailableInTable")).should(exist).getAttribute("data-id");
        $(elementLocators("FirstFormAvailableInTable")).click(); //Select the first form available in the list
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(selectedFormName)); //Verify whether the selected form is available in the Checklist flow or not
        assert formDataID != null;
        $(elementLocators("TargetListInChecklistFlow")).find(byAttribute("id",formDataID)).$(".MuiListItemText-root").hover();

        $(elementLocators("PenIconToEditSecondaryText")).shouldBe(visible).click();
        $(elementLocators("SecondaryTextInputField")).should(appear).setValue("Actions Test").pressEnter();
        $(elementLocators("TargetListInChecklistFlow")).find(byAttribute("id",formDataID))
                .$(elementLocators("SecondaryTextField")).shouldHave(text("Actions Test"));

        //Drag and Drop LABEL field to Checklist flow
        actions().clickAndHold(sourceLabel).moveToElement(targetChecklistFlow).build().perform();
        int labelXOffset = (targetXOffset-sourceLabelXOffset)+50;
        int labelYOffset= (sourceLabelYOffset-targetYOffset)+40;
        actions().moveByOffset(labelXOffset,labelYOffset).release().build().perform();
        $(elementLocators("SecondComponentInChecklistFlow")).should(appear, Duration.ofSeconds(2)).shouldHave(text("LABEL")).hover();
        $(elementLocators("PenIconToEditPrimaryText")).shouldBe(visible).click();
        $(elementLocators("PrimaryTextInputField")).should(appear).setValue(" 01").pressEnter();
        $(elementLocators("SecondComponentInChecklistFlow")).shouldHave(text("LABEL 01"));
        $(elementLocators("PenIconToEditSecondaryText")).shouldBe(visible).click();
        $(elementLocators("SecondaryTextInputField")).shouldBe(visible).setValue("Sample Test Description").pressEnter();
        $(elementLocators("SecondComponentSecondaryTextField")).shouldHave(text("Sample Test Description"));

    }
}
