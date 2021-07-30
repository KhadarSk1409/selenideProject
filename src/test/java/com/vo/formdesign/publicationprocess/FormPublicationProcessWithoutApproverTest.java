package com.vo.formdesign.publicationprocess;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify Form Publication Process Without Approver")
public class FormPublicationProcessWithoutApproverTest extends BaseTest {

    @Test
    @DisplayName("Create a form and publish without any approvers")
    public void formPublicationWithoutApproval() {

        //Create a New Form
        Pair<String, String> formName=createNewForm();
        String actualFormName= formName.getKey();
        $("#wizard-createFormButton").should(exist).shouldBe(enabled).click(); //Click on Create Form
        $("#formDashboardHeaderLeft").should(appear);
        $("#block-loc_en-GB-r_1-c_1").should(exist).click(); //Click on + to add a field
        $("#template_card").should(appear).$("#li-template-Textfield-04").click(); //Add one field
        $("#formtree_card").should(exist);
        $("#formelement_properties_card").should(exist);
        $("#nav_button").should(exist).click();
        $("#designer_panel_menu ul").$(byText("Configure publication process"))
                .should(exist).click(); //Should click on Configure publication process
        $("#btnNext").should(exist).click(); //Click on Next
        $("#designer_tab_Publications div:nth-child(3)").shouldHave(text("Ready and Save"));
        $("#btnSave").should(exist).click(); //Click on Save
        $("#btnFormDesignPublish").should(exist).click();
        $("#form-publish-dialog").$("#btnConfirm").should(exist).shouldBe(enabled).click();
        $("#formDashboardHeaderLeft").should(appear);
        $("#btnCreateNewData").should(exist);
        $("#navMainDashboard").should(exist).click();

        //Verify the created form is published or not
        SelenideElement formListTable = $("#formListTable .MuiTableBody-root").shouldBe(visible);

        ElementsCollection formRows = formListTable.$$("tr");
        System.out.println(" Form Count is " + formRows.size());

        if (formRows.size() == 0) {
            System.out.println("No Forms available");
            return;
        }
        formRows.forEach(rowEl -> {
            String finalFormName = rowEl.$("td:nth-child(2)").getText();
            if (finalFormName.equals(actualFormName)) {
                rowEl.$("td:nth-child(3)").shouldHave(Condition.text("Published"));
            }
        });
    }
}
