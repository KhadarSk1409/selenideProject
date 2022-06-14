package com.vo.checklist;

import com.vo.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsChecklistCreation.createNewChecklist;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the checklist version")
public class ChecklistVersionCheckTest extends BaseTest {

    @Test
    @DisplayName("Create new checklist and verify the checklist versions")
    public void verifyChecklistVersion() throws InterruptedException {

        createNewChecklist();
        //Adding one form the checklist flow and verifying the form version
        $(elementLocators("SourceFormElement")).should(exist).doubleClick();
        $(byText(elementLocators("SelectAForm"))).should(appear);
        $(byText(elementLocators("ChooseFromLibrary"))).click();
        $(elementLocators("FormsGridContainer")).should(appear); //Forms available in Library will appear
        $(elementLocators("FormsAvailableInTable")).should(exist).getSize();
        String selectedFormName1 = $(elementLocators("FirstFormNameInTheTable")).getText();
        String formDataID1 = $(elementLocators("FirstFormAvailableInTable")).should(exist).getAttribute("data-id");
        $(elementLocators("FirstFormAvailableInTable")).click(); //Select the first form available in the list
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(selectedFormName1)); //Verify whether the selected form is available in the Checklist flow or not
        assert formDataID1 != null;
        $(elementLocators("TargetListInChecklistFlow")).find(byAttribute("id",formDataID1)).should(exist);
        $(elementLocators("PublishChecklistTemplateButton")).should(exist).shouldBe(enabled).click();
        $(elementLocators("ConfirmPublish")).should(appear).click();
        Thread.sleep(5000);
        $(byText("Loading Checklists")).should(disappear);
        $(elementLocators("StartChecklistButton")).should(appear).shouldBe(enabled).click();

        $(byText("CheckList")).should(appear);
        $(elementLocators("ChecklistComponentList")).should(exist).shouldHave(text(selectedFormName1)).click();
        $(elementLocators("ChecklistData")).should(exist).shouldHave(text(selectedFormName1));
        $(elementLocators("CloseChecklistButton")).should(exist).click();
        $(elementLocators("GridContainer")).should(exist);
        String versionID1 = $(elementLocators("FirstRowInTheGridContainer")).should(appear).getAttribute("data-id");
        $(elementLocators("TaskStatus/Overview")).should(appear,Duration.ofSeconds(3)).shouldHave(text("0/1"));
        assert versionID1 != null;
        $(elementLocators("GridContainer")).find(byAttribute("data-id", versionID1))
                .$(elementLocators("ShowChecklistButton")).should(exist).click();
        $(byText("CheckList")).should(appear);
        $(elementLocators("ChecklistComponentList")).should(exist).shouldHave(text(selectedFormName1));
        $(elementLocators("CloseChecklistButton")).should(exist).click();
        $(elementLocators("MoreOptionsMenuButton")).should(exist).click();
        $(elementLocators("EditChecklistTemplate")).should(appear).click();

        //Adding second form to the checklist flow
        $(byText("Checklist Flow")).should(appear);
        $(elementLocators("SourceFormElement")).should(exist).doubleClick();
        $(byText(elementLocators("SelectAForm"))).should(appear);
        $(byText(elementLocators("ChooseFromLibrary"))).click();
        $(elementLocators("FormsGridContainer")).should(appear); //Forms available in Library will appear
        $(elementLocators("FormsAvailableInTable")).should(exist).getSize();
        String selectedFormName2 = $(elementLocators("SecondFormNameInTheTable")).getText();
        String formDataID2 = $(elementLocators("SecondFormAvailableInTable")).should(exist).getAttribute("data-id");
        $(elementLocators("SecondFormAvailableInTable")).click(); //Select the Second form available in the list
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(selectedFormName2));//Verify whether the selected form is available in the Checklist flow or not
        assert formDataID2 != null;
        $(elementLocators("TargetListInChecklistFlow")).find(byAttribute("id",formDataID2)).should(exist);
        $(elementLocators("PublishChecklistTemplateButton")).should(exist).shouldBe(enabled).click();
        $(elementLocators("ConfirmPublish")).should(appear).click();
        Thread.sleep(5000);
        $(byText("Loading Checklists")).should(disappear);
        $(elementLocators("StartChecklistButton")).should(appear).shouldBe(enabled).click();

        $(byText("CheckList")).should(appear);
        $(elementLocators("ChecklistComponentList")).should(exist).shouldHave(text(selectedFormName2)).click();
        $(elementLocators("ChecklistData")).should(exist).shouldHave(text(selectedFormName2));
        $(elementLocators("CloseChecklistButton")).should(exist).click();
        $(elementLocators("GridContainer")).should(exist);
        Thread.sleep(3000);
        String versionID2 = $(elementLocators("FirstRowInTheGridContainer")).should(appear).getAttribute("data-id");
        $(elementLocators("TaskStatus/Overview")).should(appear,Duration.ofSeconds(3)).shouldHave(text("0/2"));
        assert versionID2 != null;
        $(elementLocators("GridContainer")).find(byAttribute("data-id", versionID2))
                .$(elementLocators("ShowChecklistButton")).should(exist).click();
        $(byText("CheckList")).should(appear);
        $(elementLocators("ChecklistComponentList")).should(exist).shouldHave(text(selectedFormName2));
        $(elementLocators("CloseChecklistButton")).should(exist).click();

        //Again checking the previous version to confirm if only one form is present or not
        $(elementLocators("GridContainer")).should(exist);
        $(elementLocators("GridContainer")).find(byAttribute("data-id", versionID1))
                .$(elementLocators("ShowChecklistButton")).should(exist).click();
        $(byText("CheckList")).should(appear);
        $(elementLocators("ChecklistComponentList")).should(exist).shouldHave(text(selectedFormName1));
    }
}
