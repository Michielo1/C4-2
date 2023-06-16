package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import hhs4a.project2.c42.utils.internet.InternetConnection;
import hhs4a.project2.c42.utils.password.PasswordUtil;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginScreenControllerTest {
    private static AbstractDatabaseUtilTemplate<Account> accountDatabaseUtil;

    @BeforeAll
    static void setup(){
        accountDatabaseUtil = AccountDatabaseUtil.getInstance();
    }

    //This test works both offline and online since the database is local
    @Test
    @DisplayName("Login with username test")
    void loginTestUsername(){
        String userName = "admin";
        String password = "admin";

        Account account = accountDatabaseUtil.getAccount(userName, "");

        assertNotNull(account);
        assertTrue(PasswordUtil.verifyPassword(account.getPassword(), password));
    }

    @Test
    @DisplayName("Login with email test")
    void loginTestEmail(){
        String email = "admin@c4-2.nl";
        String password = "admin";

        Account account = accountDatabaseUtil.getAccount("", email);

        assertNotNull(account);
        assertTrue(PasswordUtil.verifyPassword(account.getPassword(), password));
    }

    @Test
    @DisplayName("Login with username test and wrong password")
    void loginTestUsernameWrongPassword(){
        String userName = "admin";
        String password = "test";

        Account account = accountDatabaseUtil.getAccount(userName, "");

        assertNotNull(account);
        assertFalse(PasswordUtil.verifyPassword(account.getPassword(), password));
    }

    //This test will skip if the user is connected to the internet
    @Test
    @DisplayName("Login with username test while offline")
    void loginTestUsernameOffline(){
        String userName = "admin";
        String password = "admin";

        Account account = accountDatabaseUtil.getAccount(userName, "");

        Assumptions.assumeFalse(InternetConnection.getInstance().checkInternetConnection());

        assertNotNull(account);
        assertTrue(PasswordUtil.verifyPassword(account.getPassword(), password));
    }
}