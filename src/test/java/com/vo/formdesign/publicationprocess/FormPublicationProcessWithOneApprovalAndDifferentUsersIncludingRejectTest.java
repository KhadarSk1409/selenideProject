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

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify form publication with one approval and reject process by different users")
public class FormPublicationProcessWithOneApprovalAndDifferentUsersIncludingRejectTest extends BaseTest {

    @Test
    @DisplayName("Create a from and publish with one approval and verify reject process with different users")
    public void formPublicationWithOneApproverIncludingReject() {

        Pair<String, String> formName = createNewForm();
        String actualFormName = formName.getKey();
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
        $("#rb_Basic_Approve_Form_Process").should(exist).shouldBe(checked); //Publication with one approval should be checked
        $("#btnNext").should(exist).click(); //Click on Next
        $("#ckb_first_tApproverFreeUserSelection").should(exist).click(); //Click on Free User Selection
        $("#fc_first_UserSelect").$("#selUser").should(exist).click(); //Click on SelUser to select the user
        //Select GUI Tester to Approve and Publish
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Tester 01guitester01@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester 01guitester01@visualorbit.com")).click(); //Click on the selected user
        $("#sw_first_UserCanOverwrite").should(exist).shouldBe(enabled).click();
        $("#btnNext").should(exist).click(); //Click on Next
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch version before publishing
        $("#btnSave").should(exist).shouldBe(enabled).click(); //Click on Save
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version previous version is not present
        $("#btnFormDesignPublish").should(exist).click();
        $("#form-publish-dialog").$("#btnConfirm").should(exist).shouldBe(enabled).click();
        $("#client-snackbar").should(appear).shouldHave(Condition.text("The form requires approval before publishing. It will be published once approved"));

        //Login as GUI Tester 01 and review the form
        shouldLogin(UserType.USER_01);
        $("#tasksCard").find(byAttribute("data-form-name", actualFormName )).should(exist)
                .$(".buttonPreview").should(exist).click(); //Click on quick preview
        $("#dashboard-data-card-dialog_actions").should(appear);
        $("#btnViewForm").should(exist).click(); //Click on Review Changes
        $("#btnRejectTask").should(exist).click(); //Click on Reject
        $("#textfield_RejectReason").should(appear)
                .setValue("Form is being rejected"); //Enter the reason for rejection
        $("#data-approve-reject-dialog").$("#btnConfirm")
                .should(exist).shouldBe(enabled).click(); //Click on confirm
        $("#navMainDashboard").should(exist).click(); //Click on Launchpad

        //Login as GUI Tester and verify the form state after rejection
        shouldLogin(UserType.MAIN_TEST_USER);
        $("#btnLibrary").should(exist).hover().click(); //Hover and click on Library to navigate to formlist table
        $("#tabDataCapture").should(exist).hover();
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
    }
}
