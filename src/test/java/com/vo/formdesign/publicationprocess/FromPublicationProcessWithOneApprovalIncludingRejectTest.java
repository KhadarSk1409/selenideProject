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
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify Form Publication Process with One Approval Including Reject")
public class FromPublicationProcessWithOneApprovalIncludingRejectTest extends BaseTest {

    @Test
    @DisplayName("Create a from and publish with one approval and verify Reject process")
    public void formPublicationWithOneApproverIncludingReject() {

        Pair<String, String> formName=createNewForm();
        String actualFormName= formName.getKey();
        System.out.println(actualFormName);
        $("#wizard-createFormButton").should(exist).shouldBe(enabled).click(); //Click on Create Form
        $("#formDashboardHeaderLeft").should(appear);
        $("#block-loc_en-GB-r_1-c_1").should(exist).click(); //Click on + to add a field
        $("#template_card").should(appear).$("#li-template-Textfield-05").click(); //Add one field
        $("#formtree_card").should(exist);
        $("#formelement_properties_card").should(exist);
        $("#nav_button").should(exist).click();
        $("#designer_panel_menu ul").$(byText("Configure publication process"))
                .should(exist).click(); //Should click on Configure publication process
        $("#ckbApprovalProcessRequired").should(exist).click();
        $("#btnNext").should(exist).click();
        $("#rb_Basic_Approve_Form_Process").shouldBe(checked); //Publication with one approval should be checked
        $("#btnNext").should(exist).click(); //Click on Next
        $("#ckb_first_tApproverFreeUserSelection").should(exist).click(); //Click on Free User Selection
        $("#fc_first_UserSelect").$("#selUser").should(exist).click(); //Click on SelUser to select the user
        //Select GUI Tester to Approve and Publish
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Testerguitester@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Testerguitester@visualorbit.com")).click(); //Click on the selected user
        $("#sw_first_UserCanOverwrite").should(exist).click();
        $("#btnNext").should(exist).click(); //Click on Next
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch version before publishing
        $("#btnSave").should(exist).shouldBe(enabled).click(); //Click on Save
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version previous version is not present
        $("#btnFormDesignPublish").should(exist).click();
        $("#form-publish-dialog").$("#btnConfirm").should(exist).shouldBe(enabled).click();
        $("#client-snackbar").should(appear).shouldHave(Condition.text("The form requires approval before publishing. It will be published once approved"));
        $("#btnLibrary").should(exist).hover().click(); //Hover and click on Library to navigate to formlist table
        $("#tabDataCapture").should(exist).hover();

        //Verify the initial state of created form should be in draft
        SelenideElement formListTable = $("#formListTable .MuiTableBody-root").shouldBe(visible);
        ElementsCollection formRows = formListTable.$$("tr");
        System.out.println(" Form Count is " + formRows.size());

        if (formRows.size() == 0) {
            System.out.println("No Forms available");
            return;
        }
        formRows.forEach(rowEl -> {
            String finalFormName = rowEl.$("td:nth-child(2)").getText();
            if (finalFormName.equals(actualFormName)) {
                rowEl.$("td:nth-child(3)").shouldHave(Condition.text("in draft"));
            }
        });

        //Reviewing the task assigned before approve or reject
        $("#navMainDashboard").should(exist).click();
        $("#tasksCard").should(exist);
        $("#tasksCard").find(byAttribute("data-form-name", actualFormName )).should(exist)
                .$(".buttonPreview").should(exist).click(); //Click on quick preview
        $("#dashboard-data-card-dialog_actions").should(appear);
        $("#btnViewForm").should(exist).click(); //Click on Review Changes
        $("#btnRejectTask").should(exist).click(); //Click on Reject
        $("#textfield_RejectReason").should(appear)
                .setValue("Form is being rejected"); //Enter the reason for rejection
        $("#data-approve-reject-dialog").$("#btnConfirm")
                .should(exist).shouldBe(enabled).click(); //Click on confirm
        $("#navMainDashboard").should(exist).click();
        $("#btnLibrary").should(exist).hover().click(); //Hover and click on Library to navigate to formlist table

        //Verify the final form state is in draft or not as the form is rejected
        $("#tabDataCapture").should(exist).hover();
        SelenideElement formsListTable = $("#formListTable .MuiTableBody-root").shouldBe(visible);
        ElementsCollection formListRows = formListTable.$$("tr");
        System.out.println(" Form Count is " + formListRows.size());

        if (formListRows.size() == 0) {
            System.out.println("No Forms available");
            return;
        }
        formListRows.forEach(rowEl -> {
            String finalFormName = rowEl.$("td:nth-child(2)").getText();
            if (finalFormName.equals(actualFormName)) {
                rowEl.$("td:nth-child(3)").shouldHave(Condition.text("in draft"));
            }
        });
    }
}
