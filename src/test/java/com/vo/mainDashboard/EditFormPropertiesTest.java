package com.vo.mainDashboard;

import com.codeborne.selenide.Condition;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the Form Properties")
public class EditFormPropertiesTest extends BaseTest {

    @Test
    @DisplayName("Open the Form Properties Test form")
    @Order(1)
    public void openFormDashboard(){
        open("/dashboard/Sample");
    }

    @Test
    @DisplayName("Edit the Form Properties Test form")
    @Order(2)
    public void editFormProperties() {
        $(elementLocators("LeftFormDashboardHeader")).should(appear, Duration.ofSeconds(30));
        $(elementLocators("SubMenu")).should(appear, Duration.ofSeconds(8)).click();
        $(elementLocators("EditFormDesignInSubMenu")).should(exist).click(); //Click on Edit Form Design
        $(elementLocators("FormStructure")).should(exist);
        $(elementLocators("DesignerMenu")).should(exist).click();
        $(elementLocators("FormProperties")).click(); //Should click on Form Properties
        $(elementLocators("DesignerTab")).should(exist);
        $(elementLocators("LabelsInput")).should(appear);

        if(!$(elementLocators("FormLabelsField")).has(text("SKB"))) {
            $(elementLocators("FormLabelSelection")).click(); //Should click on Label
            $(elementLocators("Popover")).should(appear);
            try {
                $$(elementLocators("ListOfOptions")).shouldHave(itemWithText("SKB"), Duration.ofSeconds(5));
                $$(elementLocators("ListOfOptions")).findBy(text("SKB")).click(); //Click on the selected Label
            } catch (Throwable t) {
                $(elementLocators("FormLabelInput")).setValue("SKB");
                $(elementLocators("Popover")).should(appear);
                $$(elementLocators("ListOfOptions")).shouldHave(itemWithText("SKB"));
                $$(elementLocators("ListOfOptions")).findBy(text("SKB")).click();
            }
            $(elementLocators("FormLabelsField")).shouldHave(text("SKB"));
        }
        $(elementLocators("FormLabelsField")).shouldHave(text("SKB"));
        $(elementLocators("AddLanguageButton")).should(exist).shouldBe(enabled).click();
        $(elementLocators("GermanLang")).should(appear);
        String newLang=$(elementLocators("GermanLang")).getText();
        System.out.println("Added Language is " +newLang);
        $(elementLocators("PublishButton")).click(); //Click on Publish
        $(elementLocators("ConfirmPublish")).click();
        $(elementLocators("ConfirmationMessage")).should(appear).shouldHave(Condition.text("The form was published successfully"));

        //Verify the selected options
        $(elementLocators("Launchpad")).click(); //Click on Launchpad
        open("/dashboard/Sample");//Open the Form dashboard
        $(elementLocators("LeftFormDashboardHeader")).should(exist);
        $(elementLocators("SubMenu")).should(appear, Duration.ofSeconds(8)).click();
        $(elementLocators("EditFormDesignInSubMenu")).should(exist).click(); //Click on Edit Form Design
        $(elementLocators("DesignerMenu")).should(exist).click();
        $(elementLocators("FormProperties")).shouldHave(Condition.text("Form Properties")).click();
        $(elementLocators("FormLabelsField")).shouldHave(text("SKB"));
        $(elementLocators("DesignerLanguage2")).shouldHave(text("German - Germany")); //German language should exist
        $(elementLocators("DeleteLang2")).click(); //Delete the selected language
        $(elementLocators("DesignerLanguage2")).shouldNot(exist, Duration.ofSeconds(2));
        $(elementLocators("PublishButton")).click(); //Click on Publish
        $(elementLocators("ConfirmPublish")).click();
        $(elementLocators("ConfirmationMessage")).should(appear).shouldHave(Condition.text("The form was published successfully"));
        $(elementLocators("LeftFormDashboardHeader")).should(exist);

    }
}
