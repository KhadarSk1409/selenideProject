package com.vo.mainDashboard;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.CollectionCondition.allMatch;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static reusables.ReuseActions.elementLocators;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify Data Capture Table Overview Actions")
public class DataCaptureOverviewTableActionsInLibraryTest extends BaseTest {

    @Test
    @DisplayName("Should Open data capture overview table")
    @Order(1)
    public void openDataCaptureOverview() {
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("DataCapture")).should(exist).click();
    }

    @Test
    @DisplayName("DataCapture Overview Table should be verified depending on the form selected")
    @Order(2)
    public void verifyDataCaptureFormsOverview() {

        $(elementLocators("FormsRelatedTab")).should(appear);
        $(elementLocators("DataCaptureTable")).should(exist);
        $(elementLocators("LeftArrowButton")).should(exist);
        $(elementLocators("RightArrowButton")).should(exist).click();
        $(elementLocators("LeftArrowButton")).should(exist).click();

        ElementsCollection DataCaptureFormsAvailable = $$(elementLocators("DataCaptureFormNames"));
        int formsSize = DataCaptureFormsAvailable.size();
        System.out.println("Data Capture forms available are "+ formsSize);

        String formToBeSelectedFirst = "DATA-CAPTURE-WO-PROCESS";
        $$(elementLocators("DataCaptureFormNames")).findBy(text(formToBeSelectedFirst)).click();
        ElementsCollection filteredRows = $$(elementLocators("FormNamesInTheFilteredTable"));
        filteredRows.asDynamicIterable().stream().
                filter(el -> (el.getText().equals(formToBeSelectedFirst) && el.getText().isEmpty() || el.getText().equals("displayed:false></div>,")));

        $$(elementLocators("DataCaptureFormNames")).findBy(text(formToBeSelectedFirst)).click();

        String formToBeSelectedSecond = "TA-TWO-APPROVAL-DIFF-USERS";
        $$(elementLocators("DataCaptureFormNames")).findBy(text(formToBeSelectedSecond)).click();
        filteredRows.asDynamicIterable().stream().filter(el -> (el.getText().equals(formToBeSelectedSecond) && el.getText().isEmpty() || el.getText().equals("displayed:false></div>,")));

        ElementsCollection formLabels = $$(elementLocators("FormsStateInTable"));
        SelenideElement targetElement1 = $(elementLocators("FormsTab"));

        //CLick on Started over the graph and verify all the forms which were in overdue are getting filtered or not
        actions().moveToElement( targetElement1, 10, 220).build().perform();
        actions().click().build().perform();//Click on started
        if(!formLabels.isEmpty()) {
            $$(elementLocators("FormsStateInTable")).
                    asDynamicIterable().stream().filter(el -> (el.getText().equals("Completed") && el.getText().isEmpty() || el.getText().equals("In Progress") ||
                            el.getText().equals("In Approval") || el.getText().equals("In Overdue")));
        }
        else {
            $(elementLocators("FormGrid")).shouldBe(empty);
        }

        //CLick on In Progress over the graph and verify all the forms which were in overdue are getting filtered or not
        actions().moveToElement( targetElement1, 25, 220).build().perform();
        actions().click().build().perform();//Click on In Progress
        if(!formLabels.isEmpty()) {
            $$(elementLocators("FormsStateInTable")).
                    should(allMatch("All filtered forms should have same label: In Progress", el -> (el.getText().equals("In Progress") || el.getText().isEmpty())));
        }
        else {
            $(elementLocators("FormGrid")).shouldBe(empty);
        }

        //CLick on In Approval over the graph and verify all the forms which were in overdue are getting filtered or not
        actions().moveToElement( targetElement1, 158, 198).build().perform();
        actions().click().build().perform();//Click on In Approval
        if(!formLabels.isEmpty()) {
            $$(elementLocators("FormsStateInTable")).
                    should(allMatch("All filtered forms should have same label: In Approval", el->(el.getText().equals("In Approval") || el.getText().isEmpty())));
        }
        else {
            $(elementLocators("FormGrid")).shouldBe(empty);
        }

        //CLick on In Overdue over the graph and verify all the forms which were in overdue are getting filtered or not
        SelenideElement targetElement2 = $(elementLocators("ChecklistsTab"));
        actions().moveToElement( targetElement2, 120, 220).build().perform();
        actions().click().build().perform();//Click on In Overdue
        if(!formLabels.isEmpty()) {
            $$(elementLocators("FormsStateInTable")).
                    should(allMatch("All filtered forms should have same label: In Overdue", el->(el.getText().equals("In Overdue") || el.getText().isEmpty())));
        }
        else {
            $(elementLocators("FormGrid")).shouldBe(empty);
        }

        //Click on Completed over the graph and verify all the completed forms are getting filtered or not
        actions().moveToElement( targetElement2, 150, 220).build().perform();
        actions().click().build().perform();//Click on Completed
        if(!formLabels.isEmpty()) {
            $$(elementLocators("FormsStateInTable")).
                    should(allMatch("All filtered forms should have same label: Completed", el->(el.getText().equals("Completed") || el.getText().isEmpty())));
        }
        else {
            $(elementLocators("FormGrid")).shouldBe(empty);
        }
        System.out.println("Filtered forms with labels Completed or Empty are: "+ $$(elementLocators("FormsStateInTable")));
    }
}
