package com.vo.createformdialog;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static java.lang.String.format;
import static reusables.ReuseActions.elementLocators;


public class ReuseApproverSelection {

    public enum ApproverOrder {
        FIRST("first", "First Approval"),
        SECOND("second", "Second Approval");

        private String order;
        private String labelText;

        ApproverOrder(String order, String labelText) {
            this.order = order;
            this.labelText = labelText;
        }

        public String getOrder() {
            return this.order;
        }
        public String getLabelText() {
            return this.labelText;
        }
    }

    public static void selectApprovers(String containerSelector, ApproverOrder order) throws InterruptedException {
        String s = order.order;

        $(format(elementLocators("SelectApproverManagerByOrder"),s)).should(exist).click(); //Direct manager of form publisher checkbox is checked
        $(format(elementLocators("SelectApproverOrderInMSGroup"),s)).shouldNotBe(checked); //Members of MS Group checkbox
        $(format(elementLocators("SelectApproverOrderInVOGroup"),s)).shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $(format(elementLocators("SelectApproverOrderInFreeUserSelection"),s)).shouldNotBe(checked); //Free User Selection checkbox
        $(format(elementLocators("SelectApproverOrderInUserCanOverWrite"),s)).shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off

        $(format(elementLocators("SelectApproverOrderInMSGroup"),s)).should(exist).click(); //Members of MS Group is checked
        $(elementLocators("BackButton")).shouldBe(enabled);
        $(elementLocators("CancelButton")).shouldBe(enabled);
        $(elementLocators("CreateFormButton")).shouldBe(disabled); //Create Form button is disabled
        $(elementLocators("NextButton")).shouldBe(disabled); //Next button is disabled
        $(format(elementLocators("SelectMSGroupDropDownByOrder"),s)).should(appear); //Select approver for MS Group dropdown is enabled
        $(containerSelector).$(byText(order.labelText)).should(exist).click(); //Click on First Approvals
        $(format(elementLocators("SelectMSGroupDropDownByOrder"),s)).click();
        String optionDropdown = $(elementLocators("FirstOptionFromTheList")).should(exist).getText(); //User selects first option from the dropdown
        $(elementLocators("OptionAvailable")).click();
        $(format(elementLocators("SelectMSGroupDropDownByOrder"),s)).shouldHave(value(optionDropdown));
        Thread.sleep(3000);

        $(format(elementLocators("SelectApproverOrderInVOGroup"),s)).should(exist).click(); //Members of VisualOrbit Group
        $(containerSelector).$(byText(order.labelText)).should(exist).click(); //Click on First Approvals
        $(format(elementLocators("SelectVOGroupDropDownByOrder"),s)).should(appear); //Select approver dropdown is enabled
        $(elementLocators("BackButton")).shouldBe(enabled);
        $(elementLocators("CancelButton")).shouldBe(enabled);
        $(elementLocators("CreateFormButton")).shouldBe(disabled); //Create Form button is disabled
        $(elementLocators("NextButton")).shouldBe(disabled); //Next button is disabled

        $(format(elementLocators("MembersOfVisualOrbitGroupByOrder"),s)).should(exist);
        $(format(elementLocators("MembersOfVisualOrbitGroupByOrder"),s)).click(); //Click on Members Of Visual Orbit Group
        $(format(elementLocators("SelectVOGroupDropDownByOrder"),s)).shouldBe(visible);
        $(format(elementLocators("SelectVOGroupDropDownByOrder"),s)).click(); //Click on Candidate approver dropdown
        String optionDropdown1 = $(elementLocators("FirstOptionFromTheList")).shouldBe(visible).getText();
        $(elementLocators("OptionAvailable")).click();
        $(format(elementLocators("SelectVOGroupDropDownByOrder"),s)).shouldHave(value(optionDropdown1));
        Thread.sleep(3000);

        $(containerSelector).$(byText(order.labelText)).should(exist).click(); //Click on First Approvals
        $(format(elementLocators("SelectApproverOrderInFreeUserSelection"),s)).should(exist).click(); //Free User Selection
        $(format(elementLocators("UserSelectDropdownByOrder"),s)).should(exist); //Select approver dropdown is enabled
        $(elementLocators("BackButton")).shouldBe(enabled);
        $(elementLocators("CancelButton")).shouldBe(enabled);
        $(elementLocators("CreateFormButton")).shouldBe(disabled); //Create Form button is disabled
        $(elementLocators("NextButton")).shouldBe(disabled); //Next button is disabled
        $(format(elementLocators("UserSelectDropdownByOrder"),s)).should(exist).click(); //Click on Free User Selection dropdown
        $(elementLocators("Popover")).should(appear);
        String optionDropdown2 = $(elementLocators("FirstOptionFromTheList")).should(exist).getText();
        $(elementLocators("OptionAvailable")).click(); //User selects first option from the dropdown
        $(format(elementLocators("UserSelectByOrder"),s)).shouldHave(text(optionDropdown2));
        $(elementLocators("DropDownButton")).should(exist).click();
        $(elementLocators("Popover")).should(disappear);
        Condition expectedState = disabled;
        if(ApproverOrder.SECOND.equals(order)) {
            expectedState = enabled;
        }
        $(elementLocators("CreateFormButton")).shouldHave(expectedState); //Create Form button is disabled on first approval and enabled on second
        $(elementLocators("NextButton")).shouldHave(expectedState); //Next button iis disabled on first approval and enabled on second
    }
}
