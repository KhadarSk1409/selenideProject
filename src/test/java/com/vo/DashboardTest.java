package com.vo;

import com.codeborne.selenide.*;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.Color;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.CollectionCondition.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selectors.*;

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
        $("#usageStatistics").should(appear);
        $("#platformNews").should(appear);
        $("#tasksCard").should(appear);
        $("#datacaptureOverview").should(appear);
        $("#formRelatedTabs").should(appear);
    }


    @Test
    @DisplayName("Click on 'Create a Form' should open Form Wizard Creation Dialog")
    @Order(2)
    public void clickOnEditFormShouldOpenDesigner() {
        $("#btnCreateForm").should(appear)
                                      .click();

        $("#wizardFormDlg").should(appear);
    }

    @Test
    @DisplayName("Click on 'Favorites' should change Navigation")
    @Order(3)
    public void clickOnFavoriteShouldChangeNavigation() {

        $("#sw_show_my_forms_USER").shouldBe(visible);
        boolean onlyMyFormsVisible = $("#sw_show_my_forms_USER input").is(checked);
        System.out.println("onlyMyFormsVisible " + onlyMyFormsVisible);
        if(onlyMyFormsVisible) {
            $("#sw_show_my_forms_USER").click();
            $("#sw_show_my_forms_USER input").shouldBe(not(checked));
        }

        SelenideElement firstFavoritesBtn = $$(".favorite")
                .shouldHave(sizeGreaterThan(0)).get(0);

        String initClasses = firstFavoritesBtn.getAttribute("class");
        boolean initIsAddFavorite = initClasses.contains("addFavorite");
        boolean initIsRemoveFavorite = initClasses.contains("removeFavorite");
        firstFavoritesBtn.click();

        //first element should change favorite selector class
        firstFavoritesBtn = $$(".favorite")
                .shouldHave(sizeGreaterThan(0)).get(0);
        String resultClasses = firstFavoritesBtn.getAttribute("class");
        boolean resultIsRemove = resultClasses.contains("removeFavorite");
        boolean resultIsAdd = resultClasses.contains("addFavorite");

        if(initIsAddFavorite) {
            assertTrue(resultIsRemove, "first button should change to remove favorite icon");
        } else if (initIsRemoveFavorite) {
            assertTrue(resultIsAdd, "first button should change to add favorite icon");
        } else {
            fail("undefined favorites state, neigher add or remove buttons available");
        }
    }


}
