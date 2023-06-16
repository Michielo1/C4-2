package hhs4a.project2.c42.utils.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class EmailUtilTest {

    @Test
    void testValidateEmailTrue() {
        // Arrange
        String email = "thisisatestemail@gmail.com";

        // Act
        boolean result = EmailUtil.validateEmail(email);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void testValidateEmailFalse() {
        // Arrange
        String email = "sugon@.org";

        // Act
        boolean result = EmailUtil.validateEmail(email);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void testEmailNotInUse() {
        // Arrange
        String email1 = "admin@c4-2.nl";
        String email2 = "fishy@gmail.com";
        String email3 = "yourdad@lol.org";

        // Act
        boolean result1 = TestEmailUtil.testEmailInUse(email1);
        boolean result2 = TestEmailUtil.testEmailInUse(email2);
        boolean result3 = TestEmailUtil.testEmailInUse(email3);

        // Assert
        Assertions.assertFalse(result1);
        Assertions.assertFalse(result2);
        Assertions.assertFalse(result3);
    }

    @Test
    void testEmailInUse() {
        // Arrange
        String email1 = "yoUR@emaIL.com";
        String email2 = "myLItTle@pONY.org";
        String email3 = "suNTsU@gmail.com";

        // Act
        boolean result1 = TestEmailUtil.testEmailInUse(email1);
        boolean result2 = TestEmailUtil.testEmailInUse(email2);
        boolean result3 = TestEmailUtil.testEmailInUse(email3);

        // Assert
        Assertions.assertTrue(result1);
        Assertions.assertTrue(result2);
        Assertions.assertTrue(result3);
    }

    @Test
    void testCheckIfEmailValid() {
        // Arrange
        String email1 = "yomail@o rg";
        String email2 = "iamthepresident@usa.org";
        String email3 = "thisisnotmyemailiswear@gmail.com";

        // Act
        boolean result1 = TestEmailUtil.testEmailValidCheck(email1);
        boolean result2 = TestEmailUtil.testEmailValidCheck(email2);
        boolean result3 = TestEmailUtil.testEmailValidCheck(email3);

        // Assert
        Assertions.assertFalse(result1);
        Assertions.assertFalse(result2);
        Assertions.assertTrue(result3);
    }
}