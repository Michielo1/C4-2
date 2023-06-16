package hhs4a.project2.c42.utils.password;

import hhs4a.project2.c42.utils.account.AccountGenerator;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PasswordUtilTest {
    @Test
    void testCheckIfPasswordValidWithValidPasswords() {
        // Arrange
        String password1 = "Abc123!";
        String password2 = "P@ssw0rd";
        String password3 = "HelloWorld1$";

        // Act
        boolean result1 = PasswordUtil.checkIfPasswordValid(password1);
        boolean result2 = PasswordUtil.checkIfPasswordValid(password2);
        boolean result3 = PasswordUtil.checkIfPasswordValid(password3);

        // Assert
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
    }

    @Test
    void testCheckIfPasswordValidWithNoUppercaseLetter() {
        // Arrange
        String password = "abc123";

        // Act
        boolean result = PasswordUtil.checkIfPasswordValid(password);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckIfPasswordValidWithNoLowercaseLetter() {
        // Arrange
        String password = "ABC123";

        // Act
        boolean result = PasswordUtil.checkIfPasswordValid(password);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckIfPasswordValidWithNoDigit() {
        // Arrange
        String password = "Abcdef";

        // Act
        boolean result = PasswordUtil.checkIfPasswordValid(password);

        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckIfPasswordValidWithNoSpecialCharacter() {
        // Arrange
        String password = "Abc123";

        // Act
        boolean result = PasswordUtil.checkIfPasswordValid(password);

        // Assert
        assertFalse(result);
    }

    //Decision Testing, the conditions are not important so TestPasswordUtil is not needed
    @Test
    void decisionTest(){
        String passwordValid = "Abc123!";
        String passwordInvalid = "abc123";

        assertTrue(PasswordUtil.checkIfPasswordValid(passwordValid));
        assertFalse(PasswordUtil.checkIfPasswordValid(passwordInvalid));
    }

    //Condition testing
    @ParameterizedTest
    @MethodSource("conditionProvider")
    void conditionPasswordTest(boolean[] conditions, boolean expected) {
        // Generate a password based on the conditions
        String password = AccountGenerator.generatePassword(conditions);
        // Check if the password is valid
        assertEquals(expected, PasswordUtil.checkIfPasswordValid(password));
    }

    static Stream<Arguments> conditionProvider() {
        return Stream.of(
                // All conditions are true, valid password
                Arguments.of(new boolean[]{true, true, true, true}, true),
                // All conditions are false, invalid password
                Arguments.of(new boolean[]{false, false, false, false}, false));
    }

    //MCDC testing, testing
    @ParameterizedTest
    @MethodSource("mcdcProvider")
    void mcdcPasswordTest(boolean[] conditions, PasswordValidationResult expected) {
        // Generate a password based on the conditions
        String password = AccountGenerator.generatePassword(conditions);
        // Check if the password is valid
        assertEquals(expected, TestPasswordUtil.checkIfPasswordValid(password));
    }

    static Stream<Arguments> mcdcProvider() {
        return Stream.of(
                // All conditions are true, valid password
                Arguments.of(new boolean[]{true, true, true, true}, PasswordValidationResult.VALID),
                // Change one condition at a time to false, so we test both values for every condition and we get all decisions.
                Arguments.of(new boolean[]{false, true, true, true}, PasswordValidationResult.MISSING_UPPERCASE_LETTER),
                Arguments.of(new boolean[]{true, false, true, true}, PasswordValidationResult.MISSING_LOWERCASE_LETTER),
                Arguments.of(new boolean[]{true, true, false, true}, PasswordValidationResult.MISSING_DIGIT),
                Arguments.of(new boolean[]{true, true, true, false}, PasswordValidationResult.MISSING_SPECIAL_CHARACTER));
    }


}