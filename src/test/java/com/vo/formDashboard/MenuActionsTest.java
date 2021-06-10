package com.vo.formDashboard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.commands.WaitUntil;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static reusables.ReuseActions.navigateToFormDashBoardFromFavoriteForms;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Menu actions validations")

public class MenuActionsTest extends BaseTest {
    @BeforeAll
    @DisplayName("Open Sample Form")
    public static void navigateFormDashboardFavoritesForms() {
        open("/dashboard/sample-form");
    }


    @Test
    @DisplayName("Verify Clicking on PDF opens new window")
    @Order(1)
    public void verifyClickPDFOpensTabWithJspdfBtn() {
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).click(); //Menu button on Form Dashboard
        $("#optionsMenu ul li:nth-child(1)").should(exist).shouldHave(Condition.text("PDF")).click();
        new WebDriverWait(getWebDriver(), 10).until(v -> {
            Set<String> handles = getWebDriver().getWindowHandles();
            return handles.size() > 1;
        });
        switchTo().window(1);
        $("#btnJsPdf").should(exist);
        switchTo().window(0);
    }

    @Test
    @DisplayName("Verify Click on Edit Form Permission opens dialog Form Authorization")
    @Order(2)
    public void verifyClickOnEditFormPermOpensDialog() {
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).click(); //Menu button on Form Dashboard
        $("#optionsMenu ul li:nth-child(2)").should(exist).shouldHave(Condition.text("Edit Form Permissions")).click(); //Edit permissions submenu
        $("#form-wizard-dialog-title").should(appear); //Form Authorization dialog should appear
        $("#form_authorization_container").should(exist); //Form Authorization dialog container
        $("#wizard-cancelButton").should(exist).click(); //
    }

    @Test
    @DisplayName("Verify Click on Data Capture should open a modal dialog with Data Capture properties")
    @Order(3)
    public void verifyClickOnDataCaptureOpensModalDialog() {
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).click(); //Menu button on Form Dashboard
        $("#optionsMenu ul li:nth-child(3)").should(exist).shouldHave(Condition.text("Data Capture")).click();
        //modal dialog with Data Capture properties?
        $("#dlgDataCaptureWizard #btnCancel").should(exist);
        $("#dlgDataCaptureWizard #btnCancel").click();
        $("#dlgDataCaptureCancelConfirmation").should(exist); //Confirmation dialog shown
        $("#dlgDataCaptureCancelConfirmation #btnConfirm").should(exist).click(); //This is not working?
    }

    @Test
    @DisplayName("Verify Click on copy form dataset url into Clipboard")
    @Order(4)
    public void verifyClickOnCopyFormDatasetCopiesUrl() {
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).click(); //Menu button on Form Dashboard
        $("#optionsMenu ul li:nth-child(5)").should(exist).shouldHave(Condition.text("Copy Form Dataset URL to Clipboard")).click();
        String clipBoardUrl = getClipboardContent();
        System.out.println("clipboard content: " + clipBoardUrl);
        assertTrue(clipBoardUrl.contains("vo-tenant-subscription-key"));
        assertTrue(clipBoardUrl.contains("MS-EXCEL"));
        assertTrue(clipBoardUrl.contains("targetLocale"));
        assertTrue(clipBoardUrl.contains("dateEpoch"));
    }
}
