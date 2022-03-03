package com.vo.formdesign;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import reusables.ReuseActionsFormCreation;

import java.io.IOException;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static reusables.ReuseActions.createNewForm;
import static reusables.ReuseActions.elementLocators;
import static reusables.ReuseActionsFormCreation.navigateToFormDesign;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the Text Field element block actions")
public class TextFieldBlockActionsTest extends BaseTest {

    @Test
    @DisplayName("Open Form Designer Block Actions form")
    @Order(1)
    public void openFormDesigner() throws IOException {
        navigateToFormDesign(ReuseActionsFormCreation.FormField.TEXTFIELD_BLOCKACTIONS);
    }

    @Test
    @DisplayName("Verify the Text Field Block Navigation to Right")
    @Order(2)
    public void moveTextFieldBlockToRight() {

        for (int col = 1, row = 1; col < 4; col++) {
            $(elementLocators("PublishButton")).should(exist); //Verify that user has navigated to form design
            String blockID = "#block-loc_en-GB-r_1-c_1";
            String nextBlockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
            if ($(blockID).exists()) {
                $(nextBlockId).shouldBe(visible).click();
            } else {
                $(elementLocators("BlockR1C2")).should(exist).click();
                $(blockID).should(exist).click();
            }
            $(nextBlockId).$(elementLocators("PenIcon")).closest("button").should(exist).click(); //Click on Edit
            $(elementLocators("NavigateBlockToRightBtn")).should(exist).click();//Should navigate to right once
        }
    }

    @Test
    @DisplayName("Verify the Text Field Block Navigation to Left")
    @Order(3)
    public void moveTextFieldBlockToLeft() {

        for (int col = 4, row = 1; col > 1; col--) {
            $(elementLocators("PublishButton")).should(exist); //Verify that user has navigated to form design
            String prevBlockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
            if ($(prevBlockId).exists()) {
                $(prevBlockId).shouldBe(visible).click();
            } else {
                $(elementLocators("BlockR1C2")).should(exist).click();
                $(prevBlockId).should(exist).click();
            }
            $(prevBlockId).$(elementLocators("PenIcon")).closest("button").should(exist).click(); //Click on Edit
            $(elementLocators("NavigateBlockToLeftBtn")).should(exist).click();//Should navigate to left once
        }
    }

    @Test
    @DisplayName("Verify the Text Field Block Navigation to Down")
    @Order(4)
    public void moveTextFieldBlockToDown() {

        for (int col = 1, row = 1; row < 4; row++) {
            $(elementLocators("PublishButton")).should(exist); //Verify that user has navigated to form design
            String blockID = "#block-loc_en-GB-r_1-c_1";
            String nexdowntBlockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
            if ($(blockID).exists()) {
                $(nexdowntBlockId).shouldBe(visible).click();
            } else {
                $(elementLocators("BlockR1C2")).should(exist).click();
                $(blockID).should(exist).click();
            }
            $(nexdowntBlockId).$(elementLocators("PenIcon")).closest("button").should(exist).click(); //Click on Edit
            $(elementLocators("NavigateBlockToDownwardsBtn")).should(exist).click();//Should navigate to down once
        }
    }

    @Test
    @DisplayName("Verify the Text Field Block Navigation to up")
    @Order(5)
    public void moveTextFieldBlockToUp() {

        for (int col = 1, row = 4; row > 1; row--) {
            $(elementLocators("PublishButton")).should(exist); //Verify that user has navigated to form design
            String nextUpBlockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
            if ($(nextUpBlockId).exists()) {
                $(nextUpBlockId).shouldBe(visible).click();
            } else {
                $(elementLocators("BlockR2C2")).should(exist).click();
                $(nextUpBlockId).should(exist).click();
            }
            $(nextUpBlockId).$(elementLocators("PenIcon")).closest("button").should(exist).click(); //Click on Edit
            $(elementLocators("NavigateBlockToUpwardsBtn")).should(exist).click();//Should navigate to upwards once
        }
    }

    @Test
    @DisplayName("Verify the Expand Action in Text Field Block")
    @Order(6)
    public void expandTextFieldBlock() {

        String blockID = "#block-loc_en-GB-r_1-c_1";
        int columnSpan = 4;
        $(blockID).should(exist);
        $(elementLocators("BlockR3C4")).should(exist).click();

        if (columnSpan > 1) {
            int prevWidth = $(blockID).getRect().getWidth();
            IntStream.range(1, columnSpan).forEach(c -> {
                String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText();
                $(blockID).$(elementLocators("PenIcon")).closest("button").should(exist).click();
                $(elementLocators("ExpandBlockBtn")).shouldBe(visible).click();
                $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr));
            });
            int currWidth = $(blockID).getRect().getWidth();
            Assertions.assertEquals(columnSpan, currWidth / prevWidth, "block column span should be " + columnSpan);
        } else {
            $(elementLocators("BlockR2C3")).click();
            $(blockID).$(elementLocators("PenIcon")).closest("button").should(exist).click();
            $(elementLocators("ExpandBlockBtn")).shouldBe(visible).click();
        }
    }

        @Test
        @DisplayName("Verify the reduce in size Action in Text Field Block")
        @Order(7)
        public void compressTextFieldBlock() {

            String blockID = "#block-loc_en-GB-r_1-c_1";
            int columnSpan = 4;
            $(blockID).should(exist);
            $(elementLocators("BlockR3C4")).should(exist).click();

            if (columnSpan >1) {
                int currWidth = $(blockID).getRect().getWidth();
                IntStream.range(1, columnSpan).forEach(c -> {
                    String initialVerNumStr = $(elementLocators("InitialVersion")).should(exist).getText();
                    $(blockID).$(elementLocators("PenIcon")).closest("button").should(exist).click();
                    $(elementLocators("CompressBlockBtn")).shouldBe(visible).shouldBe(enabled).click();
                    $(elementLocators("InitialVersion")).shouldNotHave(text(initialVerNumStr));
                });
                int prevWidth = $(blockID).getRect().getWidth();
                Assertions.assertEquals(columnSpan, currWidth / prevWidth, "block column span should be " +columnSpan);
            } else {
                $(elementLocators("BlockR2C3")).click();
                $(blockID).$(elementLocators("PenIcon")).closest("button").should(exist).click();
                $(elementLocators("CompressBlockBtn")).shouldBe(visible).click();
        }
    }
}