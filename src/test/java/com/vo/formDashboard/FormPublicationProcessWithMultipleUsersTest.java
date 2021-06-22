package com.vo.formDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the form publication process with multiple users")
public class FormPublicationProcessWithMultipleUsersTest extends BaseTest {

    @Test
    @DisplayName("Verify Create a form and publish with multiple users")
    public void publicationProcess() {
        shouldLogin(UserType.USER_01);
        $("#btnCreateForm").should(exist).click(); //Click on Create New Form
        $("#dlgFormFormWizard").should(appear);
        $("#wizard-formTitle").should(exist).setValue("Form Publication Process Test"); //Enter the Title
        $("#wizard-createFormButton").should(exist).shouldBe(enabled).click(); //Click on Create Form
        $("#formDashboardHeaderLeft").should(appear);
        $("#block-loc_en-GB-r_1-c_1").should(exist).click(); //Click on + to add a field
        $("#template_card").should(appear).$("#li-template-Textfield-04").click(); //Add one field
        $("#btnFormDesignPublish").should(exist).click(); //Click on Publish
        $("#form-publish-dialog").$("#btnConfirm").should(exist).shouldBe(enabled).click(); //Click on Confirm
        $("#formDashboardHeaderLeft").should(exist);
        $("#btnEditFormDesign").should(exist).shouldBe(enabled).click(); //Click on Edit Form Design
        $("#formtree_card").should(exist);
        $("#formelement_properties_card").should(exist);
        $("#nav_button").should(exist).click();
        $("#designer_panel_menu ul li:nth-child(4)").click(); //Should click on Configure publication process
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
        $("#sw_first_UserCanOverwrite").should(exist).click();
        $("#btnNext").should(exist).click(); //Click on Next
        $("#btnSave").should(exist).click(); //Click on Save
        $("#btnFormDesignPublish").should(exist).click();
        $("#form-publish-dialog").$("#btnConfirm").should(exist).shouldBe(enabled).click();

        shouldLogin(UserType.USER_02); //Should login as GUI Tester 02
        $("#tasksCard tbody tr:nth-child(3) td:nth-child(3)").shouldHave(value("Form Publication Process Test")).should(exist);
        $("#tasksCard  tbody tr:nth-child(3) td:nth-child(6) span:nth-child(2) .fa-check").click(); //Click on Quick Approve

    }
}
