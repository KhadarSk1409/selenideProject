package com.vo;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Dashboard Tests")
public class DashboardTest extends BaseTest {

    @BeforeEach
    public void openDashboard() {
        open("/dashboard");
    }

    @Test
    @DisplayName("Dashboard should contain elements")
    @Order(1)
    public void dashboardShouldContainElements() {
        $(elementLocators("UsageStats")).should(appear);
        $(elementLocators("TasksCardInDashboard")).should(appear);
        $(elementLocators("RecentlyWorkedForms")).should(appear);
    }


    @Test
    @DisplayName("Click on 'Create a Form' should open Form Wizard Creation Dialog")
    @Order(2)
    public void clickOnEditFormShouldOpenDesigner() {
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("CreateNewFormButton")).should(appear).click();
        $(elementLocators("FormCreationWindow")).should(appear);
    }

    @Test
    @DisplayName("Click on 'Favorites' should change Navigation")
    @Order(3)
    public void clickOnFavoriteShouldChangeNavigation() {
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("MoreFilter")).should(exist).click(); //Click on filter icon
        $(elementLocators("MoreFilterPopover")).should(appear);
        $(elementLocators("OwnForms")).shouldBe(visible);
        boolean onlyMyFormsVisible = $(elementLocators("OwnFormsCheckBox")).is(checked);
        System.out.println("onlyMyFormsVisible " + onlyMyFormsVisible);
        if (onlyMyFormsVisible) {
            $(elementLocators("OwnForms")).click();
            $(elementLocators("OwnFormsCheckBox")).shouldBe(not(checked));
        }
        $(elementLocators("Body")).click();
        $(elementLocators("MoreFilterPopover")).should(disappear);

        SelenideElement firstFavoritesBtn = $$(elementLocators("AddFavoriteButton"))
                .shouldHave(sizeGreaterThan(0)).get(0);

        String initClasses = firstFavoritesBtn.getAttribute("class");
        boolean initIsAddFavorite = initClasses.contains("buttonAddFavorite");
        boolean initIsRemoveFavorite = initClasses.contains("buttonRemoveFavorite");
        firstFavoritesBtn.click();

        //first element should change favorite selector class
        firstFavoritesBtn = $$(elementLocators("AddFavoriteButton"))
                .shouldHave(sizeGreaterThan(0)).get(0);
        String resultClasses = firstFavoritesBtn.getAttribute("class");
        boolean resultIsRemove = resultClasses.contains("buttonRemoveFavorite");
        boolean resultIsAdd = resultClasses.contains("buttonAddFavorite");

        if (initIsAddFavorite) {
            assertTrue(resultIsRemove, "first button should change to remove favorite icon");
        } else if (initIsRemoveFavorite) {
            assertTrue(resultIsAdd, "first button should change to add favorite icon");
        } else {
            fail("undefined favorites state, neither add nor remove buttons available");
        }
    }
}
