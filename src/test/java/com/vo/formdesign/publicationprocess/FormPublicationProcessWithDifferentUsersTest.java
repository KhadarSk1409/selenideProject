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
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the form publication process with multiple users")
public class FormPublicationProcessWithDifferentUsersTest extends BaseTest {

    @Test
    @DisplayName("Verify Create a form and publish with multiple users")
    public void formPublicationProcessWithDifferentUsers() {
        shouldLogin(UserType.USER_01);
        //Create a New Form
        Pair<String, String> formName=createNewForm();
        String actualFormName= formName.getKey();
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
        //Select GUI Tester 02 to Approve and Publish
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Tester 02guitester02@visualorbit.com"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester 02guitester02@visualorbit.com")).click(); //Click on the selected user
        $("#sw_first_UserCanOverwrite").should(exist).shouldBe(enabled).click();
        $("#btnNext").should(exist).click(); //Click on Next
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Fetch version before publishing
        $("#btnSave").should(exist).click(); //Click on Save
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version previous version is not present
        $("#btnFormDesignPublish").should(exist).click();
        $("#form-publish-dialog").$("#btnConfirm").should(exist).shouldBe(enabled).click();
        $("#client-snackbar").should(appear).shouldHave(Condition.text("The form requires approval before publishing. It will be published once approved"));

        shouldLogin(UserType.USER_02); //Should login as GUI Tester 02
        $("#tasksCard").find(byAttribute("data-form-name", actualFormName )).should(exist)
                .$(".buttonQuickApprove").should(exist).click(); //Click on quick approve
        $("#tasksCard").find(byAttribute("data-form-name", actualFormName )).waitUntil(disappear, 15000);
        $("#client-snackbar").should(appear)
                .shouldHave(Condition.text("New form version was successfully published."));

        //Verify the form approved by GUI Tester 02 is Published or not
        shouldLogin(UserType.USER_01); //Should login as GUI Tester 01
        $("#navLibrary").should(exist).hover().click(); //Hover and click on Library to navigate to formlist table
        $("#btnCreateForm").should(exist);
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
                rowEl.$("td:nth-child(3)").shouldHave(Condition.text("Published"));
            }
        });
    }
}
