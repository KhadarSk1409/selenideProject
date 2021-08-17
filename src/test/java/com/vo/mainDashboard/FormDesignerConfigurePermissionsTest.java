package com.vo.mainDashboard;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the Form Designer Configure Permissions")
public class FormDesignerConfigurePermissionsTest extends BaseTest {

    @Test
    @DisplayName("Open the Form Properties Test form")
    @Order(1)
    public void openFormDashboard(){
        open("/dashboard/sqJiKRUdB");
    }

    @Test
    @DisplayName("Verify the Configure Permissions in Form Properties Test form")
    @Order(2)
    public void configureFormPermissions() {
        $("#formDashboardHeaderLeft").should(appear);
        $(".fa-ellipsis-v").closest("button").should(exist).click();
        $("#optionsMenu ul li:nth-child(1) ").should(exist).click(); //Click on Edit Form Design
        $("#formtree_card").should(exist);
        $("#formelement_properties_card").should(exist);
        $("#nav_button").should(exist).click();
        $("#designer_panel_menu ul li:nth-child(5)").click(); //Should click on Configure permissions
        $("#designer_tab_FormPermissions").should(exist);
        $("#designer_tab_FormPermissions .fa-plus").should(exist).click(); //Click on "+"
        $("#alert-dialog-title").should(exist);
        $("#autocomplete_select_user").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Tester 01"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester 01")).click(); //Click on the selected user
        $("#btnOk").should(exist).click(); //Click on Confirm
        $("#designer_tab_FormPermissions tbody tr:nth-child(2)").should(exist);
        $("#designer_tab_FormPermissions tbody tr:nth-child(2) button:nth-child(1) .fa-pen").should(exist).shouldBe(enabled).click();
        $("#permissions_edit_dialog").should(exist);
        $("#transfer-list-all-item-EDIT_FORM-label").should(exist).click(); //Click on Edit Form
        $("#btnMoveSelectedLeft").should(exist).click(); //Move selected element to the left
        $("#btnOk").should(exist).shouldBe(enabled).click(); //Click on Confirm
        $("#designer_panel_menu_container ~ button[text='Save']").should(exist).click(); //Click on Save

        //Re-Opening the form in Designer mode
        $("#toDashboard").click(); //Click on Launchpad
        open("/dashboard/sqJiKRUdB");//Open the Form
        $("#formDashboardHeaderLeft").should(exist);
        $(".fa-ellipsis-v").closest("button").should(exist).click();
        $("#optionsMenu ul li:nth-child(1) ").should(exist).click(); //Click on Edit Form Design
        $("#nav_button").should(exist).click();
        $("#designer_panel_menu ul li:nth-child(5)").click(); //Should click on Configure permissions
        $("#designer_tab_FormPermissions tbody tr:nth-child(2) td:nth-child(2)").shouldNotHave(value("EDIT_FORM"));
        //Delete the selected User
        $("#designer_tab_FormPermissions tbody tr:nth-child(2) button:nth-child(2) .fa-trash-alt").should(exist).click();
        $("#designer_panel_menu_container ~ button[text='Save']").should(exist).click(); //Click on Save

    }
}
