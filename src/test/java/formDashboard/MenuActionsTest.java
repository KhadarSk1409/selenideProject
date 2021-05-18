package formDashboard;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static reusables.ReuseActions.navigateToFormDashBoardFromFavoriteForms;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Menu actions validations")

public class MenuActionsTest extends BaseTest {
    @BeforeAll
    @DisplayName("Navigate to Form Dashboard from forms in Favorite Forms")
    public static void navigateFormDashboardFavoritesForms() {
        navigateToFormDashBoardFromFavoriteForms();
    }


    @Test
    @DisplayName("Verify Clicking on PDF opens new window")
    @Order(1)
    public void verifyClickPDFOpensTabWithJspdfBtn() throws InterruptedException {
        $("#formDashboardHeaderAppBar .btnMoreOptionsMenu").should(exist).click(); //Menu button on Form Dashboard
        $("#optionsMenu ul li:nth-child(1)").should(exist).shouldHave(Condition.text("PDF")).click();
        Thread.sleep(30000);

        //Verifying new tab opened and switch to it
        Set<String> handles = getWebDriver().getWindowHandles();
        List<String> tabs = new ArrayList<String>(handles);
        switchTo().window(tabs.get(1));
        $("#btnJsPdf").should(exist);
        Thread.sleep(5000);
        switchTo().window(tabs.get(0));
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
        String clipBoardUrl = Selenide.clipboard().getText(); //Assigned copied url to string
        clipBoardUrl.contains("vo-tenant-subscription-key");
        clipBoardUrl.contains("MS-EXCEL");
        clipBoardUrl.contains("targetLocale");
        clipBoardUrl.contains("dateEpoch");
        getWebDriver().close();
    }
}
