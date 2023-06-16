package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.utils.account.AccountGenerator;
import hhs4a.project2.c42.utils.email.EmailUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterScreenControllerTest {
    @Test
    void checkIfEmailValid() {
        String email = AccountGenerator.generateEmail(true);

        System.out.println(email);
        assertTrue(EmailUtil.checkIfEmailValid(email));
    }

    @Test
    void checkIfEmailNotValidWithSpecialCharacters(){
       String email = AccountGenerator.generateEmail(false);

        System.out.println(email);
        assertFalse(EmailUtil.checkIfEmailValid(email));
    }

    @Test
    void emailAlreadyExistsTest(){
        String email = "admin@c4-2.nl";
        assertFalse(EmailUtil.checkIfEmailValid(email));
    }
}