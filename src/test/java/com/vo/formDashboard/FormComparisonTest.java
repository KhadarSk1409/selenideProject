package com.vo.formDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the form versions")
public class FormComparisonTest extends BaseTest {

    @Test
    @DisplayName("Open the Compare Test Forms form")
    @Order(1)
    public void openFromComparisionform(){

        open("/compareForms/comparision-test/5/0");

    }

    @Test
    @DisplayName("Verify the difference between the form versions")
    @Order(2)
    public void verifyTheFormComparison() {

        $("#formIcon").should(exist);
        $("#gridItemFormProps").should(exist);
        $("#gridCompareForms").should(exist);
        $("#sourceFormSelect").shouldHave(value("Compare Test Forms 5.0"));
        $("#sourceFormSelect ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator")
                .should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Compare Test Forms 1.0"), 10000);
        $$(".MuiAutocomplete-popper li").findBy(text("Compare Test Forms 1.0")).click();
        $("#btnFormDesignSave").should(exist).click();
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_1").should(exist)
                .$(byAttribute("aria-label", "Edited")).should(appear); //Verify the 1st text field is marked as Edited
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_2").should(exist)
                .$(byAttribute("aria-label", "New")).should(appear); //Verify the 2nd text field is marked as Added
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_1 #editedBtn_form-user-725dd927-78eb-4af8-9fa7-28ebd0ddbf8c")
                .should(exist).click();
        $("#simple-popover").should(appear).shouldHave(Condition.text("EDITED"));
        $("#simple-popover").should(exist).click();
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_2 #newBtn_form-user-d2a0678d-bb8c-4e28-a6a5-39bae183b9fc")
                .should(exist).click();
        $("#simple-popover").should(appear).shouldHave(Condition.text("NEW FIELD"));
        $("#simple-popover").should(exist).click();
        $("#gridCompareForms input[value='checkedEdited']").should(exist).click(); //Click on Edited
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_1").should(exist)
                .$(byAttribute("aria-label", "Edited")).shouldNot(appear); //Verify the 1st text field is marked Edited is visible or not
        $("#gridCompareForms input[value='checkedNew']").should(exist).click(); //Click on New
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_2").should(exist)
                .$(byAttribute("aria-label", "New")).shouldNot(appear); //Verify the 2nd text field is marked as Added is visible or not
        $("#gridCompareForms input[value='checkedEdited']").should(exist).click(); //Click on Edited
        $("#gridCompareForms input[value='checkedNew']").should(exist).click(); //Click on New

        //Verifying Version 2.0
        $("#sourceFormSelect").shouldHave(value("Compare Test Forms 1.0"));
        $("#sourceFormSelect ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator")
                .should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Compare Test Forms 2.0"), 10000);
        $$(".MuiAutocomplete-popper li").findBy(text("Compare Test Forms 2.0")).click();
        $("#btnFormDesignSave").should(exist).click();
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_1").should(exist)
                .$(byAttribute("aria-label", "Edited")).should(exist); //Verfiy the 1st text field is marked as Edited

        //Verifying Version 3.0
        $("#sourceFormSelect").shouldHave(value("Compare Test Forms 2.0"));
        $("#sourceFormSelect ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator")
                .should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Compare Test Forms 3.0"), 10000);
        $$(".MuiAutocomplete-popper li").findBy(text("Compare Test Forms 3.0")).click();
        $("#btnFormDesignSave").should(exist).click();
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(1) #block-loc_en-GB-r_1-c_3").should(exist)
                .$(byAttribute("aria-label", "Removed")).should(exist); //Verify the text field is marked as Removed
        $("#gridCompareForms input[value='checkedRemoved']").should(exist).click(); //Click on Removed
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(1) #block-loc_en-GB-r_1-c_3").should(exist)
                .$(byAttribute("aria-label", "Removed")).shouldNot(exist); //Verify the text field is marked as Removed is visible or not
        $("#gridCompareForms input[value='checkedRemoved']").should(exist).click(); //Click on Removed

        //Verifying Version 4.0
        $("#sourceFormSelect").shouldHave(value("Compare Test Forms 3.0"));
        $("#sourceFormSelect ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator")
                .should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Compare Test Forms 4.0"), 10000);
        $$(".MuiAutocomplete-popper li").findBy(text("Compare Test Forms 4.0")).click();
        $("#btnFormDesignSave").should(exist).click();
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_1").should(exist)
                .$(byAttribute("aria-label", "Edited")).should(appear); //Verify the text field is marked as Edited

        //Verifying Version 5.0
        $("#sourceFormSelect").shouldHave(value("Compare Test Forms 4.0"));
        $("#sourceFormSelect ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator")
                .should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Compare Test Forms 5.0"), 10000);
        $$(".MuiAutocomplete-popper li").findBy(text("Compare Test Forms 5.0")).click();
        $("#btnFormDesignSave").should(exist).click();
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(1) #block-loc_en-GB-r_1-c_1").should(exist)
                .shouldHave(Condition.text("First Text Field")).should(exist);
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_1").should(exist)
                .shouldHave(Condition.text("First Text Field")).should(exist);
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(1) #block-loc_en-GB-r_1-c_2").should(exist)
                .shouldHave(Condition.text("Second Text Field")).should(exist);
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_1-c_2").should(exist)
                .shouldHave(Condition.text("Second Text Field")).should(exist);

    }
}
