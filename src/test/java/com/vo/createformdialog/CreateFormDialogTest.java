package com.vo.createformdialog;

import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Create Form Dialog Test")
public class CreateFormDialogTest extends BaseTest {

    @Test
    @DisplayName("Dashboard should contain the button 'Create Form'")
    @Order(1)
    public void dashboardShouldContainCreateFormButton() {
        $("#btnCreateForm").should(exist);

    }

    @Test
    @DisplayName("Click on 'Create Form' should open Form Wizard Creation Dialog")
    @Order(2)
    public void clickOnEditFormShouldOpenDesigner() {
        $("#btnCreateForm").should(appear)
                                      .click();

        $("#wizardFormDlg").should(appear);

    }

    @Test
    @DisplayName("Check if the form title field exists, is enabled and empty")
    @Order(3)
    public void checkFormTitleField() {

        $("#wizard-formTitle").should(exist);
        $("#wizard-formTitle").shouldBe(enabled);
        $("#wizard-formTitle").shouldBe(empty);

    }

    @Test
    @DisplayName("Check if the form description field exists, is enabled and empty")
    @Order(4)
    public void checkFormDescriptionField() {

        $("#wizard-formHelp").should(exist);
        $("#wizard-formHelp").shouldBe(enabled);
        $("#wizard-formHelp").shouldBe(empty);

    }

    @Test
    @DisplayName("Check if the form ID field exists, is enabled and contains an ID")
    @Order(5)
    public void checkFormIDField() {

        $("#wizard-formId").should(exist);
        $("#wizard-formId").shouldBe(enabled);
        $("#wizard-formId").shouldNotBe(empty);

    }

    @Test
    @DisplayName("Check if the direct link to the form dashboard exists, is disabled and contains a URL")
    @Order(6)
    public void checkFormDashboardLinkField() {

        $("#wizard-formUrl").should(exist);
        $("#wizard-formUrl").shouldNotBe(enabled);
        $("#wizard-formUrl").shouldNotBe(empty);

    }

    @Test
    @DisplayName("Check if the 'BACK' button exists and is disabled")
    @Order(7)
    public void checkAllButtons() {

        $("#wizard-backButton").should(exist);
        $("#wizard-backButton").shouldBe(disabled);

        $("#wizard-createFormButton").should(exist);
        $("#wizard-createFormButton").shouldBe(disabled);

        $("#wizard-addlOptionsButton").should(exist);
        $("#wizard-addlOptionsButton").shouldBe(disabled);

        $("#wizard-cancelButton").should(exist);
        $("#wizard-cancelButton").shouldBe(enabled);

        $("#wizard-formTitle").setValue("Hello");
        $("#wizard-formTitle").shouldNotBe(empty);

        $("#wizard-createFormButton").shouldBe(enabled);
        $("#wizard-addlOptionsButton").shouldBe(enabled);

       // $("#wizard-addlOptionsButton").click();
        $("#wizard-createFormButton").click();

        // TODO Directly going to dashboard as closing the designer is currently not working
        /*$("#btnCloseEditForm").click();
        $("#toDashboard").click();


        $("#usageStatistics").should(appear);
        $("#platformNews").should(appear);
        $("#tasksCard").should(appear);
        $("#datacaptureOverview").should(appear);
        $("#formRelatedTabs").should(appear);*/



    }



}
