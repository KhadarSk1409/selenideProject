package reusables;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.vo.BaseTest;
import com.vo.formdesign.NumberFieldTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.*;
import org.openqa.selenium.By;
import com.vo.formdesign.NumberFieldTest;
import org.openqa.selenium.Keys;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ReuseActions extends BaseTest {


    public static Pair<String, String> createNewForm() {
        //Create New Form:
        $(elementLocators("Launchpad")).click(); //Go back to Dashboard
        $(elementLocators("NavigateToLibrary")).should(exist).hover().click(); //Hover and click on Library
        $(elementLocators("CreateNewFormButton")).should(exist).click(); //Click on Create Form button
        $(elementLocators("FormCreationWindow")).should(appear); //Create Form wizard appears - Not working?
        String formTitle = RandomStringUtils.randomAlphanumeric(4);
        $(elementLocators("FormTitleInputField")).setValue(formTitle); //Set Title name
        String formDesc = RandomStringUtils.randomAlphanumeric(5);
        $(elementLocators("DescriptionInputField")).setValue(formDesc); //Setting form Description
        applyLabelForTestForms();
        $(elementLocators("CreateNewFormButton")).shouldBe(enabled); //Create Form button should be enabled

        return Pair.of(formTitle, formDesc);
    }

    public static void validationsAfterCheckingDirectManager() {
        $(elementLocators("DirectManagerOfDataFiller")).shouldBe(checked); //Direct manager of form publisher checkbox is checked
        $(elementLocators("BackButton")).shouldBe(enabled);
        $(elementLocators("CreateFormButton")).shouldBe(enabled);
        $(elementLocators("CancelButton")).shouldBe(enabled);
        $(elementLocators("NextButton")).shouldBe(enabled); //Next button
    }

    public static void validationsAfterCheckingMembersOfMSgroup() {
        $(elementLocators("MembersOfMSGroup")).should(exist).click(); //Members of MS Group is checked
        $(elementLocators("UserSelectionInput")).should(exist); //Select approver dropdown is enabled
        $(elementLocators("BackButton")).shouldBe(enabled);
        $(elementLocators("CancelButton")).shouldBe(enabled);
        //$(elementLocators("CreateFormButton")).shouldBe(disabled); //Create Form button is disabled
        //$(elementLocators("NextButton")).shouldBe(disabled); //Next button is disabled
        $(elementLocators("UserSelectionInput")).click();
        $(elementLocators("Popover")).should(appear);
        String optionDropdown1 = $(elementLocators("FirstOptionFromTheList")).shouldBe(visible).getText();
        $(elementLocators("OptionAvailable")).click();
        $(elementLocators("CreateFormButton")).should(exist).shouldBe(enabled); //Create Form button is enabled
        $(elementLocators("NextButton")).should(exist).shouldBe(enabled); //Next button is enabled
    }

    public static void validationsAfterCheckingMembersOfVisualOrbit() throws InterruptedException {
        $(elementLocators("MembersOfVOGroup")).should(exist).click(); //Members of VisualOrbit Group checkbox
        $(elementLocators("BackButton")).shouldBe(enabled);
        $(elementLocators("CancelButton")).shouldBe(enabled);
        /*$(elementLocators("CreateFormButton")).should(exist).shouldBe(disabled); //Create Form button is diabled
        $(elementLocators("NextButton")).should(exist).shouldBe(disabled); //Next button is disabled*/
        $(elementLocators("UserSelectionInput")).click();
        $(elementLocators("Popover")).should(appear);
        $(elementLocators("OptionAvailable")).click();
        Thread.sleep(3000);
        $(elementLocators("CreateFormButton")).should(exist).shouldBe(enabled); //Create Form button is enabled
        $(elementLocators("NextButton")).should(exist).shouldBe(enabled); //Next button is enabled
    }

    public static void validationsAfterCheckingFreeUserSelection() {
        $(elementLocators("FreeUserSelection")).should(exist).click(); //Free User Selection
        $(elementLocators("UserSelectionInput")).should(exist); //Select approver dropdown is enabled
        $(elementLocators("BackButton")).shouldBe(enabled);
        $(elementLocators("CancelButton")).shouldBe(enabled);
       /* $(elementLocators("CreateFormButton")).shouldBe(disabled); //Create Form button is diabled
        $(elementLocators("NextButton")).shouldBe(disabled); //Next button is disabled
*/
        $(elementLocators("UserSelectionInput")).click(); //Click on Candidate approver dropdown
        $(elementLocators("OptionAvailable")).should(exist).click(); //User selects first option from the dropdown
        $(elementLocators("CreateFormButton")).should(exist).shouldBe(enabled); //Create Form button is enabled
        $(elementLocators("NextButton")).should(exist).shouldBe(enabled); //Next button is enabled
    }

    public static void navigateToFormDashBoardFromFavoriteForms() {
        $(elementLocators("Launchpad")).should(exist);
        $(elementLocators("FormsInFavorites")).should(exist);
        ElementsCollection listFavForms = $$(elementLocators("FormsInFavorites"));
        listFavForms.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1)); //Verify that Favorite form is available
        listFavForms.get(0).click(); //Click on the Favorite form
        $(elementLocators("FormDashboardHeader")).should(exist); //Navigated to Form Dashboard
    }

    public static String elementLocators(String elementProperty) {

            try {
                //Instantiating the properties file
                props = new Properties();
                String PropFileName = "TestLibrary.properties";
                InputStream inputStream = ReuseActions.class.getClassLoader().getResourceAsStream(PropFileName);
                 if (inputStream != null) {
                    props.load(inputStream);
                } else {
                    throw new FileNotFoundException("Property file '" +PropFileName + "' not found in the classpath");
                }
            } catch (IOException e) {
                return e.toString();
            }
        return props.getProperty(elementProperty);
    }
    private static Properties props = null;
}



