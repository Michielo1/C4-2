package hhs4a.project2.c42.scenecontrollers.actionhandlers.accountusernamescreen;

import hhs4a.project2.c42.scenecontrollers.AccountUsernameScreenController;
import hhs4a.project2.c42.scenecontrollers.ManageAccountScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.password.PasswordUtil;

public class SaveUsernameChangesButtonClick implements Actionhandler {

    private AccountUsernameScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof AccountUsernameScreenController)) return;
        controller = (AccountUsernameScreenController) screencontroller;
    }

    @Override
    public void handle() {
        Account account = ManageAccountScreenController.getSelectedAccount();

        // Als account null is, dan is er rechstreeks via 'Account' scherm op wijzigen geklikt, hierbij pakken we dan de ingelogde account gegevens
        if (account == null) account = LoggedInAccountHolder.getInstance().getAccount();

        // Standaard velden en wachtwoord voor bevestiging
        String username = controller.getUsernameTextField().getText();
        String confirmPassword = controller.getConfirmPasswordField().getText();
        String storedPassword = account.getPassword();

        if (controller.checkIfTextFieldEmpty(username, confirmPassword)) return;

        if (PasswordUtil.verifyPassword(storedPassword, confirmPassword)) {
            // Set nieuwe gebruikersnaam
            account.setUsername(username.trim());

            // Gebruikersnaam aanpassen in database als er geen foutmeldingen zijn
            controller.updateAccountUsernameInDatabase(account);
        }
    }

}
