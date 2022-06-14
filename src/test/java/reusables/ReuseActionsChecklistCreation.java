package reusables;

import com.vo.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static reusables.ReuseActions.elementLocators;

public class ReuseActionsChecklistCreation extends BaseTest {

    public static Pair<String, String> createNewChecklist(){

        open("/library/checklists");

        $(elementLocators("CreateNewChecklistButton")).should(exist).click();
        $(byText("Checklist Template")).should(appear);

        String checklistTitle = RandomStringUtils.randomAlphanumeric(5);
        $(elementLocators("FormTitleInputField")).should(exist).setValue(checklistTitle);

        String checklistId = RandomStringUtils.randomAlphanumeric(6);
        $(elementLocators("IdInputField")).should(exist).setValue(checklistId);

        $(elementLocators("createChecklistTemplateBtn")).should(exist).click();
        $(byText("Checklist Flow")).should(appear);

        return Pair.of(checklistTitle, checklistId);
    }

    public static Pair<String, String> createChecklistWithNewForm() {

        open("/library/checklists");

        $(elementLocators("CreateNewChecklistButton")).should(exist).click();
        $(byText("Checklist Template")).should(appear);

        String checklistTitle = RandomStringUtils.randomAlphanumeric(5);
        $(elementLocators("FormTitleInputField")).should(exist).setValue(checklistTitle);

        String checklistId = RandomStringUtils.randomAlphanumeric(6);
        $(elementLocators("IdInputField")).should(exist).setValue(checklistId);

        $(elementLocators("createChecklistTemplateBtn")).should(exist).click();
        $(byText("Checklist Flow")).should(appear);
        $(elementLocators("SourceFormElement")).should(exist).doubleClick();
        $(byText(elementLocators("SelectAForm"))).should(appear); // Form selection wizard will appear
        $(byText(elementLocators("CreateANewForm"))).click(); //Click on Create a New Form
        $(elementLocators("FormCreationWizard")).should(appear); //New form creation wizard will appear

        String formTitle = RandomStringUtils.randomAlphanumeric(5);
        $(elementLocators("FormTitleInputField")).should(exist).setValue(formTitle); //Enter Form Title

        String formDescription = RandomStringUtils.randomAlphanumeric(6);
        $(elementLocators("DescriptionInputField")).setValue(formDescription); //Enter Form Description
        $(elementLocators("CreateNewFormButton")).should(exist).shouldBe(enabled).click(); //Click on Create Form
        $(elementLocators("TargetListInChecklistFlow")).shouldHave(text(formTitle)); //Verify whether the created form is available in the Checklist flow or not

        return Pair.of(formTitle, formDescription);
    }
}
