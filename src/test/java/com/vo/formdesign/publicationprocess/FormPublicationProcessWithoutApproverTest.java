package com.vo.formdesign.publicationprocess;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify Form Publication Process Without Approver")
public class FormPublicationProcessWithoutApproverTest extends BaseTest {

    @Test
    @DisplayName("Create a form and publish without any approvers")
    public void formPublicationWithoutApproval() {

        //Create a New Form
        Pair<String, String> formName=createNewForm();
        String actualFormName= formName.getKey();
        $(elementLocators("CreateFormButton")).should(exist).shouldBe(enabled).click(); //Click on Create Form
        $(elementLocators("LeftFormDashboardHeader")).should(appear);
        $(elementLocators("BlockR1C1")).should(exist).click(); //Click on + to add a field
        $(elementLocators("TemplateCard")).should(appear).$(elementLocators("TextField")).click(); //Add one field
        $(elementLocators("ElementProperties")).should(exist);
        $(elementLocators("DesignerMenu")).should(exist).click();
        $(elementLocators("ConfigPublication")).should(exist).click(); //Should click on Configure publication process
        $(elementLocators("nextButton")).should(exist).click(); //Click on Next
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch version before publishing
        $(elementLocators("ButtonSave")).should(exist).shouldBe(enabled).click(); //Click on Save
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version previous version is not present
        $(elementLocators("PublishButton")).should(exist).click();
        $(elementLocators("ConfirmPublish")).should(exist).shouldBe(enabled).click();
        $(elementLocators("ConfirmationMessage")).should(appear).shouldHave(Condition.text("The form was published successfully"), Duration.ofSeconds(5));
        $(elementLocators("LeftFormDashboardHeader")).should(appear);
        $(elementLocators("FillFormButton")).should(exist);
        $(elementLocators("Launchpad")).should(exist).click();
        $(elementLocators("ButtonLibrary")).should(exist).hover().click();
        $(elementLocators("DataCapture")).should(exist).hover();

        //Verify the created form is published or not
        SelenideElement formListTable = $(elementLocators("FormsList")).shouldBe(visible);
        ElementsCollection formRows = formListTable.$$(elementLocators("FormsAvailableInTable"));
        System.out.println(" Form Count is " + formRows.size());

        if (formRows.size() == 0) {
            System.out.println("No Forms available");
            return;
        }
        formRows.forEach(rowEl -> {
            String finalFormName = rowEl.$(elementLocators("FinalFormName")).getText();
            if (finalFormName.equals(actualFormName)) {
                rowEl.$(elementLocators("FormsStateInTable")).shouldHave(Condition.text("Published"));
            }
        });
    }
}
