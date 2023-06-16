package hhs4a.project2.c42.scenecontrollers.actionhandlers.accountpasswordscreen;

import hhs4a.project2.c42.scenecontrollers.AccountPasswordScreenController;
import hhs4a.project2.c42.scenecontrollers.ManageAccountScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.password.PasswordUtil;;

public class SavePasswordChangesButtonClick implements Actionhandler {

    private AccountPasswordScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof AccountPasswordScreenController)) return;
        controller = (AccountPasswordScreenController) screencontroller;
    }

    @Override
    public void handle() {
        Account account = ManageAccountScreenController.getSelectedAccount();

        // Als account null is, dan is er rechstreeks via 'Account' scherm op wijzigen geklikt, hierbij pakken we dan de ingelogde account gegevens
        if (account == null) account = LoggedInAccountHolder.getInstance().getAccount();

        // Standaard velden en wachtwoord voor bevestiging
        String password = controller.getPasswordField().getText();
        String confirmPassword = controller.getConfirmPasswordField().getText();
        // Haal wachtwoord uit database
        String storedPassword = account.getPassword();

        if (controller.checkIfTextFieldEmpty(password, confirmPassword)) return;

        if (PasswordUtil.verifyPassword(storedPassword, confirmPassword) && PasswordUtil.checkIfPasswordValid(password)) {
            // Set nieuwe wachtwoord
            account.setPassword(password.trim());

            // Wachtwoord aanpassen in database als er geen foutmeldingen zijn
            controller.updateAccountPasswordInDatabase(account);
        }
    }


}
