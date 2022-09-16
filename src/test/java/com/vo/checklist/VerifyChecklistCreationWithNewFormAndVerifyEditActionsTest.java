package com.vo.checklist;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Locale;
import java.util.Set;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsChecklistCreation.createNewChecklist;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify new form creation and edit actions in checklist")
public class VerifyChecklistCreationWithNewFormAndVerifyEditActionsTest extends BaseTest {

    @Test
    @DisplayName("Verify Create checklist with new form and verify edit actions")
    @Order(1)
    public void createAndVerifyChecklistWithNewForm() {

        createNewChecklist();
        $(elementLocators("PreviewChecklistButton")).should(appear);
        $(elementLocators("PublishChecklistTemplateButton")).should(exist);

        //Move the Form Element from components to checklist flow and create a new form
        $(elementLocators("SourceFormElement")).should(exist).doubleClick();

        $(byText(elementLocators("SelectAForm"))).should(appear); // Form selection wizard will appear
        $(byText(elementLocators("CreateANewForm"))).click(); //Click on Create a New Form
        $(elementLocators("FormCreationWizard")).should(appear); //New form creation wizard will appear
        $(elementLocators("FormTitleInputField")).should(exist).setValue("DemoForm"); //Enter Form Title
        $(elementLocators("DescriptionInputField")).setValue("This is a sample test form"); //Enter Form Description
        $(elementLocators("CreateNewFormButton")).should(exist).shouldBe(enabled).click(); //Click on Create Form
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text("DemoForm")); //Verify whether the created form is available in the Checklist flow or not

        //Drag and Drop LABEL field to Checklist flow
        $(elementLocators("SourceLabelElement")).should(exist).doubleClick();

        //Verify newly created form is available in checklist flow or not
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text("DemoForm")).$(elementLocators("EditFormButton")).click();

        new WebDriverWait(getWebDriver(), Duration.ofSeconds(20)).until(v -> {
            Set<String> handles = getWebDriver().getWindowHandles();
            return handles.size() > 1;
        });

        //Switch to form designer window and do necessary actions
        switchTo().window(1);
        $(elementLocators("LeftFormDashboardHeader")).should(appear).shouldHave(Condition.text("DemoForm"));
        $(elementLocators("PublishButton")).should(exist);
        $(elementLocators("BlockR1C1")).should(appear).click();
        $(elementLocators("TemplateList")).should(exist);
        $(elementLocators("TextField")).should(appear).click();
        $(elementLocators("BlockR1C1PenIcon")).should(exist).click();
        $(elementLocators("TextFieldLabel")).setValue(" Added");
        $(elementLocators("PublishButton")).should(exist).click();
        $(elementLocators("ConfirmPublish")).should(exist).click(); //Click on Confirm button
        $(elementLocators("ConfirmationMessage")).should(appear).shouldHave(Condition.text("The form was published successfully"), Duration.ofSeconds(5));

        //Switch back to Checklist window to verify whether the form state is changed to published or not
        switchTo().window(0);
        refresh();
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text("DemoForm")).$(elementLocators("PreviewItemButton")).should(exist).click();
        $(elementLocators("DataContainer")).should(appear);
        $(elementLocators("AddedBlock")).should(exist);
        $(elementLocators("CloseFillFormButton")).should(exist).click();

    }
}
