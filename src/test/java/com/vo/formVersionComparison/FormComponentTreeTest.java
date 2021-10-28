package com.vo.formVersionComparison;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the form versions and form tree components")
public class FormComponentTreeTest extends BaseTest {

    @Test
    @DisplayName("Open the from to be compared")
    @Order(1)
    public void openFormComparisionForm(){

        open("/compareForms/comparision-test/9/0");

    }

    @Test
    @DisplayName("Verify the difference between the form versions and tree components")
    @Order(2)
    public void verifyTheFormVersion(){

        SelenideElement sourceFirstBlock= $("#gridCompareForms .MuiCardContent-root > div:nth-child(1) #block-loc_en-GB-r_1-c_1");
        SelenideElement targetFirstBlock= $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_1");
        SelenideElement targetSecondBlock= $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_3-c_2");
        SelenideElement targetThirdBlock= $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_4-c_1");

        $("#formIcon").should(exist);
        $("#gridItemFormProps").should(exist);
        $("#gridCompareForms").should(exist);
        $("#sourceFormSelect").shouldHave(value("Compare Test Forms 9.0"));

        //Verify version 1.0 with version 9.0
        $("#sourceFormSelect ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Compare Test Forms 1.0"), 15000);
        $$(".MuiAutocomplete-popper li").findBy(text("Compare Test Forms 1.0")).click();
        $("#btnFormDesignSave").should(exist).click();
        $(sourceFirstBlock).should(exist).$(byAttribute("aria-label", "Edited")).should(appear);
        $(targetFirstBlock).should(exist).$(byAttribute("aria-label", "Edited")).should(appear);
        $(targetSecondBlock).should(exist).$(byAttribute("aria-label", "New")).should(appear);
        $(targetThirdBlock).should(exist).$(byAttribute("aria-label","New")).should(appear);

        //Click on Target and Verify Form Component Tree
        $("#formelement_properties_card").should(exist);
        $(".MuiBottomNavigation-root .fa-dot-circle").should(exist).click(); //Click on Target in Form Component tree
        $("#formelement_properties_card ul li").should(exist);
        /*$("#formelement_properties_card .MuiCardContent-root  ul li:nth-child(1)")
                .shouldHave(value("Compare Test Forms")).$(byAttribute("aria-label","Edited")).should(exist);
        $("#formelement_properties_card .MuiCardContent-root  ul li:nth-child(1)")
                .shouldHave(value("Compare Test Forms")).$(byAttribute("aria-label","Moved")).should(exist);
        */
        $("#formelement_properties_card .MuiCardContent-root ul li ul li ul li:nth-child(1)").should(exist)
                .$(byAttribute("aria-label","Edited")).should(exist);
        $("#formelement_properties_card .MuiCardContent-root ul li:nth-child(2)").should(exist)
                .$(byAttribute("aria-label","New")).should(exist);
        $("#formelement_properties_card .MuiCardContent-root ul li:nth-child(3)").should(exist)
                .$(byAttribute("aria-label","New")).should(exist);
    }
}
