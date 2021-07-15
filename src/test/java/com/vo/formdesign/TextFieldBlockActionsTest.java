package com.vo.formdesign;

import com.vo.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static reusables.ReuseActions.createNewForm;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Verify the Text Field element block actions")
public class TextFieldBlockActionsTest extends BaseTest {

    @Test
    @DisplayName("Open Form Designer Block Actions form")
    @Order(1)
    public void openFormDesigner() {
        createNewForm();
        $("#wizard-createFormButton").should(exist).click();
        $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design

        String blockId = "#block-loc_en-GB-r_1-c_1"; //Need to change later as of now _1 is returning two results
        String initialVerNumStr = $("#formMinorversion").should(exist).getText(); //Initial version
        $(blockId).shouldBe(visible).click();
        $("#formMinorversion").shouldNotHave(text(initialVerNumStr)); //Verify that version is increased
        $("#li-template-Textfield-04").should(appear).click();
        $(blockId).$(".fa-pen").closest("button").shouldBe(visible).click(); //Click on Edit
        $("#formelement_properties_card").should(appear);
    }

    @Test
    @DisplayName("Verify the Text Field Block Navigation to Right")
    @Order(2)
    public void moveTextFieldBlockToRight() {

        for (int col = 1, row = 1; col < 4; col++) {
            $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design
            String blockID = "#block-loc_en-GB-r_1-c_1";
            String nextBlockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
            if ($(blockID).exists()) {
                $(nextBlockId).shouldBe(visible).click();
            } else {
                $("#block-loc_en-GB-r_1-c_2").should(exist).click();
                $(blockID).should(exist).click();
            }
            $(nextBlockId).$(".fa-pen").closest("button").should(exist).click(); //Click on Edit
            $("#blockButtonRight").should(exist).click();//Should navigate to right once
        }
    }

    @Test
    @DisplayName("Verify the Text Field Block Navigation to Left")
    @Order(3)
    public void moveTextFieldBlockToLeft() {

        for (int col = 4, row = 1; col > 1; col--) {
            $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design
            String prevBlockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
            if ($(prevBlockId).exists()) {
                $(prevBlockId).shouldBe(visible).click();
            } else {
                $("#block-loc_en-GB-r_1-c_2").should(exist).click();
                $(prevBlockId).should(exist).click();
            }
            $(prevBlockId).$(".fa-pen").closest("button").should(exist).click(); //Click on Edit
            $("#blockButtonLeft").should(exist).click();//Should navigate to left once
        }
    }

    @Test
    @DisplayName("Verify the Text Field Block Navigation to Down")
    @Order(4)
    public void moveTextFieldBlockToDown() {

        for (int col = 1, row = 1; row < 4; row++) {
            $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design
            String blockID = "#block-loc_en-GB-r_1-c_1";
            String nexdowntBlockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
            if ($(blockID).exists()) {
                $(nexdowntBlockId).shouldBe(visible).click();
            } else {
                $("#block-loc_en-GB-r_1-c_2").should(exist).click();
                $(blockID).should(exist).click();
            }
            $(nexdowntBlockId).$(".fa-pen").closest("button").should(exist).click(); //Click on Edit
            $("#blockButtonDownwards").should(exist).click();//Should navigate to down once
        }
    }

    @Test
    @DisplayName("Verify the Text Field Block Navigation to up")
    @Order(5)
    public void moveTextFieldBlockToUp() {

        for (int col = 1, row = 4; row > 1; row--) {
            $("#btnFormDesignPublish").should(exist); //Verify that user has navigated to form design
            String nextUpBlockId = "#block-loc_en-GB-r_" + row + "-c_" + col;
            if ($(nextUpBlockId).exists()) {
                $(nextUpBlockId).shouldBe(visible).click();
            } else {
                $("#block-loc_en-GB-r_2-c_2").should(exist).click();
                $(nextUpBlockId).should(exist).click();
            }
            $(nextUpBlockId).$(".fa-pen").closest("button").should(exist).click(); //Click on Edit
            $("#blockButtonUpwards").should(exist).click();//Should navigate to upwards once
        }
    }

    @Test
    @DisplayName("Verify the Expand Action in Text Field Block")
    @Order(6)
    public void expandTextFieldBlock() {

        String blockID = "#block-loc_en-GB-r_1-c_1";
        int columnSpan = 4;
        $(blockID).should(exist);
        $("#block-loc_en-GB-r_3-c_4").should(exist).click();

        if (columnSpan > 1) {
            int prevWidth = $(blockID).getRect().getWidth();
            IntStream.range(1, columnSpan).forEach(c -> {
                String initialVerNumStr = $("#formMinorversion").should(exist).getText();
                $(blockID).$(".fa-pen").closest("button").should(exist).click();
                $("#blockButtonExpand").shouldBe(visible).click();
                $("#formMinorversion").shouldNotHave(text(initialVerNumStr));
            });
            int currWidth = $(blockID).getRect().getWidth();
            Assertions.assertEquals(columnSpan, currWidth / prevWidth, "block column span should be " + columnSpan);
        } else {
            $("#block-loc_en-GB-r_2-c_3").click();
            $(blockID).$(".fa-pen").closest("button").should(exist).click();
            $("#blockButtonExpand").shouldBe(visible).click();
        }
    }

        @Test
        @DisplayName("Verify the reduce in size Action in Text Field Block")
        @Order(7)
        public void compressTextFieldBlock() {

            String blockID = "#block-loc_en-GB-r_1-c_1";
            int columnSpan = 4;
            $(blockID).should(exist);
            $("#block-loc_en-GB-r_3-c_4").should(exist).click();

            if (columnSpan >1) {
                int currWidth = $(blockID).getRect().getWidth();
                IntStream.range(1, columnSpan).forEach(c -> {
                    String initialVerNumStr = $("#formMinorversion").should(exist).getText();
                    $(blockID).$(".fa-pen").closest("button").should(exist).click();
                    $("#blockButtonDownsize").shouldBe(visible).click();
                    $("#formMinorversion").shouldNotHave(text(initialVerNumStr));
                });
                int prevWidth = $(blockID).getRect().getWidth();
                Assertions.assertEquals(columnSpan, currWidth / prevWidth, "block column span should be " +columnSpan);
            } else {
                $("#block-loc_en-GB-r_2-c_3").click();
                $(blockID).$(".fa-pen").closest("button").should(exist).click();
                $("#blockButtonDownsize").shouldBe(visible).click();
            }
    }
}