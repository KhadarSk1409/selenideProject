package com.vo.formDashboard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the form versions")
public class FormComparisionExtensionTest extends BaseTest {

    @Test
    @DisplayName("Open the Compare Test Forms form")
    @Order(1)
    public void openFromComparisionform(){

        open("/compareForms/comparision-test/7/0");

    }

    @Test
    @DisplayName("Verify the difference between the form versions")
    @Order(2)
    public void verifyTheFormComparison() {

        SelenideElement firstBlock = $("#gridCompareForms .MuiCardContent-root > div:nth-child(1) #block-loc_en-GB-r_1-c_2");
        SelenideElement secondBlock = $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_3-c_2");
        SelenideElement extendedSourceBlock = $("#gridCompareForms .MuiCardContent-root > div:nth-child(1) #block-loc_en-GB-r_1-c_2");
        SelenideElement secondTargetBlock = $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_3-c_2");

        $("#formIcon").should(exist);
        $("#gridItemFormProps").should(exist);
        $("#gridCompareForms").should(exist);
        $("#sourceFormSelect").shouldHave(value("Compare Test Forms 7.0"));

        //Verifying version 5.0
        $("#sourceFormSelect ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator")
                .should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Compare Test Forms 5.0"), 10000);
        $$(".MuiAutocomplete-popper li").findBy(text("Compare Test Forms 5.0")).click();
        $("#btnFormDesignSave").should(exist).click();
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(1) #block-loc_en-GB-r_1-c_1").should(exist)
                .shouldHave(Condition.text("First Text Field")).should(exist);
        $(firstBlock).should(exist)
                .$(byAttribute("aria-label", "Moved")).should(appear); //Verify the text field is marked as Moved
        int oneWidhtBlock = $(firstBlock).getRect().getWidth();

        $(secondBlock).should(exist)
                .$(byAttribute("aria-label", "Moved")).should(appear); //Verify the text field is marked as Moved
        int extendedBlockWidth = $(secondBlock).getRect().getWidth();

        Assertions.assertEquals(extendedBlockWidth/oneWidhtBlock, 2, "second block should have span of 2");

        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_3-c_2 #movedBtn_form-user-d2a0678d-bb8c-4e28-a6a5-39bae183b9fc")
                .should(exist).click();
        $("#simple-popover").should(appear).shouldHave(Condition.text("MOVED"));
        $("#simple-popover").should(exist).click();
        $("#gridCompareForms input[value='checkedMoved']").should(exist).click(); //Click on Moved
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_3-c_2").should(exist)
                .$(byAttribute("aria-label", "Moved")).shouldNot(appear); //Verify the text field is marked Moved is visible or not
        $("#gridCompareForms input[value='checkedMoved']").should(exist).click(); //Click on Moved

        //Verifying Version 6.0
        $("#sourceFormSelect").shouldHave(value("Compare Test Forms 5.0"));
        $("#sourceFormSelect ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator")
                .should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Compare Test Forms 6.0"), 10000);
        $$(".MuiAutocomplete-popper li").findBy(text("Compare Test Forms 6.0")).click();
        $("#btnFormDesignSave").should(exist).click();
        $(extendedSourceBlock).should(exist)
                .$(byAttribute("aria-label", "Moved")).should(appear); //Verify the text field is marked Moved is visible or not
        int extendedSourceBlockWidth = $(extendedSourceBlock).getRect().getWidth();

        $(secondTargetBlock).should(exist)
                .$(byAttribute("aria-label", "Moved")).should(appear); //Verify the text field is marked as Moved
        int extendedTargetBlockWidth = $(secondTargetBlock).getRect().getWidth();

        Assertions.assertEquals(extendedTargetBlockWidth/extendedSourceBlockWidth, 1, "Both the blocks should have same span");

        //Verifying Version 7.0
        $("#sourceFormSelect").shouldHave(value("Compare Test Forms 6.0"));
        $("#sourceFormSelect ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator")
                .should(exist).click();
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("Compare Test Forms 7.0"), 10000);
        $$(".MuiAutocomplete-popper li").findBy(text("Compare Test Forms 7.0")).click();
        $("#btnFormDesignSave").should(exist).click();
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(1) #block-loc_en-GB-r_3-c_2").should(exist)
                .shouldHave(Condition.text("Second Text Field")).should(exist);
        $("#gridCompareForms .MuiCardContent-root > div:nth-child(2) #block-loc_en-GB-r_3-c_2").should(exist)
                .shouldHave(Condition.text("Second Text Field")).should(exist);

    }
}
