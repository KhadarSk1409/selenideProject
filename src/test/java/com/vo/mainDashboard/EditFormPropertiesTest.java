package com.vo.mainDashboard;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the Form Properties")
public class EditFormPropertiesTest extends BaseTest {

    @Test
    @DisplayName("Open the Form Properties Test form")
    @Order(1)
    public void openFormDashboard(){
        open("/dashboard/sqJiKRUdB");
    }

    @Test
    @DisplayName("Edit the Form Properties Test form")
    @Order(2)
    public void editFormProperties(){
        $("#formDashboardHeaderLeft").should(appear);
        $("#btnEditFormDesign").should(exist).click();
        $("#formtree_card").should(exist);
        $("#formelement_properties_card").should(exist);
        $("#nav_button").should(exist).click();
        $("#designer_panel_menu ul li:nth-child(8)").click(); //Should click on Form Properties
        $("#designer_tab_FormProperties").$("#selectFormIcon").click();
        $("#selectFormIcon_dialog_content").should(appear);

        String iconName= $("#selectFormIcon_dialog_content span:nth-child(10)").getAttribute("title");
        //System.out.println(iconName);
        $("#selectFormIcon_dialog_content span:nth-child(10)").click(); //Should add selected Icon
        $("#selFormLabelsControl").should(exist);
        $("#designer_tab_FormProperties").$("#selLabel").click(); //Should click on Label
        $(".MuiAutocomplete-popper").should(appear);
        $$(".MuiAutocomplete-popper li").shouldHave(itemWithText("SKB"), 5000);
        $$(".MuiAutocomplete-popper li").findBy(text("SKB")).click(); //Click on the selected Label
        $(".fa-plus").should(exist).click();
        $("#designer_tab_FormProperties tbody tr:nth-child(2) td:nth-child(1)").should(exist);
        String newLang=$("#designer_tab_FormProperties tbody tr:nth-child(2) td:nth-child(1)").getText();

        //Should Add another language
        $("#root div:nth-child(2) div:nth-child(2) tr:nth-child(2) button:nth-child(1)").should(exist).click();
        $("#btnFormDesignPublish").click(); //Click on Publish
        $("#form-publish-dialog").$("#btnConfirm").click();

        //Verify the selected options
        $("#toDashboard").click(); //Click on Launchpad
        open("/dashboard/sqJiKRUdB"); //Open the Form
        $("#formDashboardHeaderLeft").should(exist);
        $("#btnEditFormDesign").should(exist).click(); //Click on Edit Form Design
        $("#formtree_card .MuiIcon-root svg").shouldHave(attributeMatching("data-src", ".*"+iconName+".*"));
        $("#designer_formCardHeader button:nth-child(2)").shouldHave(text(newLang.toUpperCase()));
        $("#nav_button").should(exist).click();
        $("#designer_panel_menu ul li:nth-child(8)").click();
        $("#designer_tab_FormProperties tbody tr:nth-child(2)  button:nth-child(2)").click(); //Delete the selected language
        $("#designer_tab_FormProperties tbody tr:nth-child(2) button:nth-child(1)").click();
        $("#btnFormDesignPublish").click(); //Click on Publish
        $("#form-publish-dialog").$("#btnConfirm").click();
        $("#formDashboardHeaderLeft").should(exist);
        $("#btnEditFormDesign").should(exist);

    }
}
