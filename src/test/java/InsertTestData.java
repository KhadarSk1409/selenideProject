import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.vo.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherConstants;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.openqa.selenium.Keys;
import utils.VOTestEngine;
import utils.xmlreport.XmlReportGeneratingListener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.CollectionCondition.*;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selectors.*;

//for manual execution!
@Disabled
public class InsertTestData extends BaseTest {

    @ParameterizedTest
    //@CsvSource({"1,Elene,Duxbarry,eduxbarry0@umn.edu,Female,98168,6 Arapahoe Place,Italy,Messina,493-763-4947"})
    @CsvFileSource(resources = "./test_data.csv", numLinesToSkip = 1)
    public void mainForMavenExecution(String id, String first_name, String last_name, String email,
                                      String gender, String postal_code, String street_address, String country,
                                      String city, String phone) throws Exception {

        String formDashboardUrl = "/dashboard/userform-7ee2ae1f347a-4b18-add0-365eb672b59d-2-0";
        if(!WebDriverRunner.url().contains(formDashboardUrl))
            open(formDashboardUrl);

        $("#btnFillForm").shouldBe(visible).shouldBe(enabled).click();

        $("#gridItemDataCard").should(appear);

        $("#textField_form-user-688930c94001-4b0e-b258-8895396f2c4e").shouldBe(visible).setValue(last_name).sendKeys(Keys.TAB);
        $("#textField_form-user-bece11c55e7f-4609-83e2-38ec806ebd91").shouldBe(visible).setValue(first_name).sendKeys(Keys.TAB);
        $("#textField_form-user-298b0eb0684b-4ea3-9c1f-4766010507e4").shouldBe(visible).setValue(atLeast3(postal_code)).sendKeys(Keys.TAB);
        $("#textField_form-user-55c468753708-43c7-b360-2c55a737ea7e").shouldBe(visible).setValue(street_address).sendKeys(Keys.TAB);
        $("#textField_form-user-9ef694d389c9-4d2d-b3ed-b3f5a5e746f7").shouldBe(visible).setValue(atLeast3(first_name)).sendKeys(Keys.TAB);
        $("#textField_form-user-dc104475b7ef-4fd9-9a7e-94a5441fe60c").shouldBe(visible).setValue(atLeast3(last_name)).sendKeys(Keys.TAB);

        $("#textField_form-user-b286a035aed5-47dc-aa84-d1e2869ed417").shouldBe(visible).setValue(city).sendKeys(Keys.TAB);
        $("#textField_form-user-e0fa829b9697-4023-8d22-1161c896d9ad").shouldBe(visible).setValue(phone).sendKeys(Keys.TAB);
        $("#textField_form-user-03bcb6840a3c-40de-a069-9d4cbbce4236").shouldBe(visible).setValue(email).sendKeys(Keys.TAB);


        $("#btnSubmit").click();

        $(byText("the data was submitted successfully")).should(appear);
        $(by("data-custom-id", "btnCloseSnackbar")).shouldBe(enabled).click();
        $("#btnCloseDataFillForm").click();
    }

    private String atLeast3(String value) {
        if(value.length() > 3) {
            return value;
        }
        return value + "   ";
    }


}
