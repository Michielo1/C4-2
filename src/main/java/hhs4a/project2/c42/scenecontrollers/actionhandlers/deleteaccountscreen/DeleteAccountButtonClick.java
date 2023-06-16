package hhs4a.project2.c42.scenecontrollers.actionhandlers.deleteaccountscreen;

import hhs4a.project2.c42.scenecontrollers.DeleteAccountScreenController;
import hhs4a.project2.c42.scenecontrollers.ManageAccountScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.password.PasswordUtil;
import javafx.scene.control.Alert;

public class DeleteAccountButtonClick implements Actionhandler {

    private DeleteAccountScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof DeleteAccountScreenController)) return;
        controller = (DeleteAccountScreenController) screencontroller;
    }

    @Override
    public void handle() {
        Account account = ManageAccountScreenController.getSelectedAccount();

        // Als account null is, dan is er rechstreeks via 'Account' scherm op wijzigen geklikt, hierbij pakken we dan de ingelogde account gegevens
        if (account == null) account = LoggedInAccountHolder.getInstance().getAccount();

        String confirmedPassword = controller.getPasswordField().getText();
        // Haal wachtwoord uit database
        String storedPassword = account.getPassword();

        // Controleren of bevestigingswachtwoordveld leeg is
        if (confirmedPassword.isBlank()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Bevestigingswachtwoord ontbreekt", "Het bevestigingswachtwoord is niet ingevuld.");
            return;
        }

        if (PasswordUtil.verifyPassword(storedPassword, confirmedPassword)) {
            controller.deleteAccountConfirmation(account);
        }

    }

}
