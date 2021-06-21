package com.vo.formDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the Configure Permissions with Multiple Users")
public class ConfigurePermissionsWithMultipleUsersTest extends BaseTest {

    @Test
    @DisplayName("Verify the form configure permissions by Multiple Users")
    @Order(1)
    public void verifyFormConfigurePermissions() {
        shouldLogin(UserType.USER_01); //Should Login as GUI Tester 01
        open("/dashboard/PERMISSIONS_TEST");
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).click(); //Menu button on Form Dashboard
        $("#optionsMenu ul li:nth-child(2)").should(exist).shouldHave(Condition.text("Edit Form Permissions")).click(); //Edit permissions submenu
        $("#form-wizard-dialog-title").should(appear);
        $(".fa-plus").should(exist).click();
        $("#alert-dialog-title").should(appear);
        $("#autocomplete_select_user").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("GUI Tester 02"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("GUI Tester 02")).click(); //Click on the selected user
        $("#btnOk").should(exist).click(); //Click on Confirm
        $("#form_authorization_container").should(appear);
        $("#form_authorization_container tbody tr:nth-child(4) td:nth-child(1)").shouldHave(value("GUI Tester 02")).shouldBe(visible);
        $("#wizard-createFormButton").should(exist).click(); //Click on Apply and close
        $("#formDashboardHeaderAppBar").should(exist);

        shouldLogin(UserType.USER_02); //Should Login as GUI Tester 02
        $("#bpmRelatedTabsCard").should(exist);
        $("#formRelatedTabsCard").should(exist);
        $("#formRelatedTabsCard td:nth-child(2)").shouldHave(Condition.text("PERMISSIONS TEST")).should(exist);

        //Verify the deletion of previously given Permissions
        shouldLogin(UserType.USER_01); // Should login as  GUI Tester 01
        open("/dashboard/PERMISSIONS_TEST");
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).click(); //Menu button on Form Dashboard
        $("#optionsMenu ul li:nth-child(2)").should(exist).shouldHave(Condition.text("Edit Form Permissions")).click(); //Edit permissions submenu
        $("#form-wizard-dialog-title").should(appear);
        $("#form_authorization_container tbody tr:nth-child(4) button:nth-child(2)").should(exist).click(); //Delete GUI Tester 02
        $("#wizard-createFormButton").should(exist).click();
        $("#formDashboardHeaderAppBar").should(exist);

    }
}
