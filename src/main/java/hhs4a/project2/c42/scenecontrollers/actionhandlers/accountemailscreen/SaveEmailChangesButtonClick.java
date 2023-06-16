package hhs4a.project2.c42.scenecontrollers.actionhandlers.accountemailscreen;

import hhs4a.project2.c42.scenecontrollers.AccountEmailScreenController;
import hhs4a.project2.c42.scenecontrollers.ManageAccountScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.email.EmailUtil;
import hhs4a.project2.c42.utils.password.PasswordUtil;

public class SaveEmailChangesButtonClick implements Actionhandler {

    private AccountEmailScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof AccountEmailScreenController)) return;
        controller = (AccountEmailScreenController) screencontroller;
    }

    @Override
    public void handle() {
        Account account = ManageAccountScreenController.getSelectedAccount();

        // Als account null is, dan is er rechstreeks via 'Account' scherm op wijzigen geklikt, hierbij pakken we dan de ingelogde account gegevens
        if (account == null) account = LoggedInAccountHolder.getInstance().getAccount();

        // Standaard velden en wachtwoord voor bevestiging
        String email = controller.getEmailTextField().getText();
        String confirmPassword = controller.getConfirmPasswordField().getText();
        String storedPassword = account.getPassword();

        if (controller.checkIfTextFieldEmpty(email, confirmPassword)) return;

        if (PasswordUtil.verifyPassword(storedPassword, confirmPassword) && EmailUtil.checkIfEmailValid(email)) {
            // Set nieuwe e-mailadres
            account.setEmail(email.trim());
            // E-mailadres aanpassen in database als er geen foutmeldingen zijn
            controller.updateAccountEmailInDatabase(account);
        }
    }

}
