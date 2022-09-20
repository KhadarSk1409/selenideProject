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
import java.util.function.IntFunction;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify form publication with two approval and reject process by different users")
public class FormPublicationProcessWithTwoApprovalAndDifferentUsersIncludingRejectTest extends BaseTest {

    @Test
    @DisplayName("Create a from and publish with two approval and verify reject process with different users")
    public void formPublicationWithOneApproverIncludingReject() throws InterruptedException {

        Pair<String, String> formName = createNewForm();
        String actualFormName = formName.getKey();
        System.out.println("Created form name is: " +actualFormName);
        $(elementLocators("CreateFormButton")).should(exist).shouldBe(enabled).click(); //Click on Create Form
        $(elementLocators("LeftFormDashboardHeader")).should(appear);
        $(elementLocators("BlockR1C1")).should(exist).click(); //Click on + to add a field
        $(elementLocators("TemplateCard")).should(appear).$(elementLocators("TextField")).click(); //Add one field
        $(elementLocators("ElementProperties")).should(exist);
        $(elementLocators("DesignerMenu")).should(exist).click();
        $(elementLocators("ConfigPublication")).should(exist).click(); //Should click on Configure publication process
        $(elementLocators("EnablePublicationProcessCheckBox")).should(exist).click();
        $(elementLocators("nextButton")).should(exist).click();
        $(elementLocators("PublicationWithTwoApproval")).should(exist).click(); //Publication with two approval should be checked
        $(elementLocators("nextButton")).should(exist).click(); //Click on Next
        $(elementLocators("FreeUserSelection")).should(exist).click(); //Click on Free User Selection
        $(elementLocators("UserSelectionInput")).should(exist).click(); //Click on SelUser to select the user
        $(elementLocators("Popover")).should(appear);
        $$(elementLocators("ListOfOptions")).findBy(text("GUI Tester 01")).click(); //Click on the selected user
        $(elementLocators("EndUserCanOverwrite")).should(exist).shouldBe(enabled).click();
        $(elementLocators("EndUserCanOverwrite")).shouldHave(Condition.value("false"));
        $(elementLocators("nextButton")).should(exist).click(); //Click on Next
        $(elementLocators("UserSelectionInput")).should(exist).click(); //Click on SelUser to select the user
        $(elementLocators("Popover")).should(appear);
        $$(elementLocators("ListOfOptions")).findBy(text("GUI Tester 02")).click(); //Click on the selected user
        $(elementLocators("SecondEndUserCanOverwrite")).should(exist).shouldBe(enabled).click();
        $(elementLocators("SecondEndUserCanOverwrite")).shouldHave(Condition.value("false"));
        $(elementLocators("nextButton")).should(exist).click(); //Click on Next
        String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText(); //Fetch version before publishing
        $(elementLocators("ButtonSave")).should(exist).shouldBe(enabled).click(); //Click on Save
        $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr)); //Verify that version previous version is not present
        $(elementLocators("PublishButton")).should(exist).click();
        $(elementLocators("ConfirmPublish")).should(exist).shouldBe(enabled).click();
        $(elementLocators("ConfirmationMessage")).should(appear).shouldHave(Condition.text("The form requires approval before publishing. It will be published once approved"));

        //Login as GUI Tester 01 should review the form and approve
        shouldLogin(UserType.USER_01);
        $(elementLocators("TasksCardInDashboard")).should(exist);
        $(elementLocators("TasksCardInDashboard")).find(byAttribute("data-form-name", actualFormName )).should(exist)
                .$(elementLocators("ButtonPreview")).should(exist).click(); //Click on quick preview
        $(elementLocators("DataFillForm")).should(appear);
        $(elementLocators("ReviewChangesButton")).should(exist).click(); //Click on Review Changes
        $(elementLocators("acceptButton")).should(exist).shouldBe(enabled).click(); //Click on Accept
        Thread.sleep(5000);
        $(elementLocators("ConfirmButton")).should(exist).shouldBe(enabled).click(); //Click on confirm
        $(elementLocators("ConfirmationMessage")).should(appear)
                .shouldHave(Condition.text("Approval saved. Process requires additional approval and is therefore not yet complete. Form will be published as soon as all approvals are available."));
        $(elementLocators("Launchpad")).should(exist).click();

        //Login as GUI Tester 02 should review the form and reject
        shouldLogin(UserType.USER_02);
        $(elementLocators("TasksCardInDashboard")).should(exist);
        $(elementLocators("TasksCardInDashboard")).find(byAttribute("data-form-name", actualFormName )).should(exist)
                .$(elementLocators("ButtonPreview")).should(exist).click(); //Click on quick preview
        $(elementLocators("DataFillForm")).should(appear);
        $(elementLocators("ReviewChangesButton")).should(exist).click(); //Click on Review Changes
        $(elementLocators("rejectButton")).should(exist).click(); //Click on Reject
        $(elementLocators("RejectReasonInputField")).should(appear).setValue("Form is being rejected"); //Enter the reason for rejection
        $(elementLocators("ConfirmButton")).should(exist).shouldBe(enabled).click(); //Click on confirm
        $(elementLocators("Launchpad")).should(exist).click();

        //Login as GUI Tester and verify the form state after rejection
        shouldLogin(UserType.MAIN_TEST_USER);
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("DataCapture")).should(exist).hover();
        SelenideElement formListTable = $(elementLocators("FormsList")).shouldBe(visible);
        ElementsCollection formRows = formListTable.$$(elementLocators("FormsAvailableInTable"));
        System.out.println(" Form Count is " + formRows.size());

        int rowsSize = formRows.size();
        System.out.println(" Form Count is " + rowsSize);

        if (rowsSize == 0) {
            System.out.println("No Forms available");
            return;
        }

        IntFunction<SelenideElement> getRow = (int idx) -> $(".MuiDataGrid-row:nth-of-type(" + idx + ")");

        for (int i = 1; i <= rowsSize; i++) {
            SelenideElement rowEl = getRow.apply(i);

            String finalFormName = rowEl.$(elementLocators("FinalFormName")).getText();
            if (finalFormName.equals(actualFormName)) {
                rowEl.$(elementLocators("FormsStateInTable")).shouldHave(Condition.text("in draft"));
            }
        }
    }
}
