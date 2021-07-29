package com.vo;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName(("Multiple User Switch"))
public class MultipleUserSwitchTest extends BaseTest {

    @Test
    @DisplayName("Verify the multiple user login logout functions")
    @Order(1)
    public void verifyUserSwitch() {

        shouldLogin(UserType.USER_01);
        shouldLogin(UserType.USER_02);
        shouldLogin(UserType.USER_03);
        shouldLogin(UserType.MAIN_TEST_USER);
    }
}
