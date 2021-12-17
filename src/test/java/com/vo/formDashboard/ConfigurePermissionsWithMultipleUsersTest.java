package com.vo.formDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the Configure Permissions with Multiple Users")
public class ConfigurePermissionsWithMultipleUsersTest extends BaseTest {

    @Test
    @DisplayName("Verify the form configure permissions by Multiple Users")
    @Order(1)
    public void verifyFormConfigurePermissions() {
        shouldLogin(UserType.USER_01); //Should Login as GUI Tester 01
        open("/dashboard/PERMISSIONS_TEST");
        $(elementLocators("SubMenu")).should(exist).click(); //Click on SubMenu button on Form Dashboard
        $(elementLocators("EditFormPermissionsInSubMenu")).should(exist).shouldHave(Condition.text("Edit Form Permissions")).click(); //Edit permissions submenu
        $(elementLocators("FormAuthorizationTitle")).should(exist);
        $(elementLocators("PlusIconToAddUser")).should(exist).click();
        $(elementLocators("AddCreateUserTitle")).should(appear);
        $(elementLocators("SelectUserInput")).should(exist).click();
        $(elementLocators("Popover")).should(appear);
        $$(elementLocators("ListOfOptions")).shouldHave(itemWithText("GUI Tester 02"), Duration.ofSeconds(5));
        $$(elementLocators("ListOfOptions")).findBy(text("GUI Tester 02")).click(); //Click on the selected user
        $(elementLocators("ButtonConfirm")).should(exist).click(); //Click on Confirm
        $(elementLocators("UsersContainer")).should(appear);
        $(elementLocators("SecondUserNameInContainer")).shouldHave(Condition.text("GUI Tester 02")).shouldBe(visible);
        $(elementLocators("ApplyAndCloseButton")).should(exist).click(); //Click on Apply and close
        $(elementLocators("LeftFormDashboardHeader")).should(exist);

        shouldLogin(UserType.USER_02); //Should Login as GUI Tester 02
        $(elementLocators("UsageStats")).should(appear);
        $(elementLocators("TasksCardInDashboard")).should(appear);
        $(elementLocators("RecentlyWorkedForms")).should(appear);
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("Body")).click();
        $(elementLocators("FormsRelatedTab")).should(exist);
        $(elementLocators("NewlyCreatedForm")).should(exist).shouldHave(Condition.text("PERMISSIONS_TEST")); //Form with name Permissions_Test should exist in the list

        //Verify the deletion of previously given Permissions
        shouldLogin(UserType.USER_01); // Should login as  GUI Tester 01
        open("/dashboard/PERMISSIONS_TEST");
        $(elementLocators("SubMenu")).should(exist).click(); //Click on SubMenu button on Form Dashboard
        $(elementLocators("EditFormPermissionsInSubMenu")).should(exist).shouldHave(Condition.text("Edit Form Permissions")).click(); //Edit permissions submenu
        $(elementLocators("FormAuthorizationTitle")).should(exist);
        $(elementLocators("UserDeleteButton")).should(exist).click(); //Delete GUI Tester 02
        $(elementLocators("ApplyAndCloseButton")).should(exist).click();
        $(elementLocators("FormDashboardHeader")).should(exist);

    }
}
