package com.vo.createformdialog;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static java.lang.String.format;


public class ApproverSelection {

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

        $(format("#ckb_%s_ApproverManager",s)).should(exist).click(); //Direct manager of form publisher checkbox is checked
        $(format("#ckb_%s_ApproverGroupInMS",s)).shouldNotBe(checked); //Members of MS Group checkbox
        $(format("#ckb_%s_ApproverGroupInVO",s)).shouldNotBe(checked); //Members of VisualOrbit Group checkbox
        $(format("#ckb_%s_tApproverFreeUserSelection",s)).shouldNotBe(checked); //Free User Selection checkbox
        $(format("#sw_%s_UserCanOverwrite",s)).shouldNotBe(selected); //Toggle - “End-User can overwrite approver(s)“ - Switched Off

        $(format("#ckb_%s_ApproverGroupInMS",s)).should(exist).click(); //Members of MS Group is checked
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $(format("#fc_%s_MSGroupSelect #selUser",s)).should(appear); //Select approver for MS Group dropdown is enabled
        $(containerSelector).$(byText(order.labelText)).should(exist).click(); //Click on First Approvals
        $(format("#fc_%s_MSGroupSelect #selUser",s)).click();
        String optionDropdown = $("#selUser-option-0 div:first-of-type").should(exist).getText(); //User selects first option from the dropdown
        $("#selUser-option-0").click();
        $(format("#fc_%s_MSGroupSelect #selUser",s)).shouldHave(value(optionDropdown));
        Thread.sleep(3000);

        $(format("#ckb_%s_ApproverGroupInVO",s)).should(exist).click(); //Members of VisualOrbit Group
        $(containerSelector).$(byText(order.labelText)).should(exist).click(); //Click on First Approvals
        $(format("#fc_%s_VOGroupSelect #selUser",s)).should(appear); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled

        $(format("#l_%s_ApproverGroupInVO",s)).should(exist);
        $(format("#l_%s_ApproverGroupInVO",s)).click(); //Click on Members Of Visual Orbit Group
        $(format("#fc_%s_VOGroupSelect #selUser",s)).shouldBe(visible);
        $(format("#fc_%s_VOGroupSelect #selUser",s)).click(); //Click on Candidate approver dropdown
        String optionDropdown1 = $("#selUser-option-0 div:first-of-type").shouldBe(visible).getText();
        $("#selUser-option-0").click();
        $(format("#fc_%s_VOGroupSelect #selUser",s)).shouldHave(value(optionDropdown1));
        Thread.sleep(3000);

        $(containerSelector).$(byText(order.labelText)).should(exist).click(); //Click on First Approvals
        $(format("#ckb_%s_tApproverFreeUserSelection",s)).should(exist).click(); //Free User Selection
        $(format("#fc_%s_UserSelect #selUser",s)).should(exist); //Select approver dropdown is enabled
        $("#wizard-backButton").shouldBe(enabled);
        $("#wizard-cancelButton").shouldBe(enabled);
        $("#wizard-createFormButton").shouldBe(disabled); //Create Form button is disabled
        $("#wizard-addlOptionsButton").shouldBe(disabled); //Next button is disabled
        $(format("#fc_%s_UserSelect #selUser",s)).should(exist).click(); //Click on Free User Selection dropdown
        $(".MuiAutocomplete-popper").should(appear);
        String optionDropdown2 = $("#selUser-option-0 div:first-of-type").should(exist).getText();
        $("#selUser-option-0").click(); //User selects first option from the dropdown
        $(format("#fc_%s_UserSelect",s)).shouldHave(text(optionDropdown2));
        $("#selUser ~ .MuiAutocomplete-endAdornment .MuiAutocomplete-popupIndicator").should(exist).click();
        $(".MuiAutocomplete-popper").should(disappear);
        Condition expectedState = disabled;
        if(ApproverOrder.SECOND.equals(order)) {
            expectedState = enabled;
        }
        $("#wizard-createFormButton").shouldHave(expectedState); //Create Form button is disabled on first approval and enabled on second
        $("#wizard-addlOptionsButton").shouldHave(expectedState); //Next button iis disabled on first approval and enabled on second
    }
}
