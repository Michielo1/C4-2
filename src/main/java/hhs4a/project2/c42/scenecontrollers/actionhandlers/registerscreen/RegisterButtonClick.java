package hhs4a.project2.c42.scenecontrollers.actionhandlers.registerscreen;

import hhs4a.project2.c42.scenecontrollers.RegisterAccountScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.Permission;
import hhs4a.project2.c42.utils.account.Settings;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import hhs4a.project2.c42.utils.email.EmailUtil;
import hhs4a.project2.c42.utils.password.PasswordUtil;
import javafx.scene.control.Alert;

public class RegisterButtonClick implements Actionhandler {

    private RegisterAccountScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof RegisterAccountScreenController)) return;
        controller = (RegisterAccountScreenController) screencontroller;
    }

    @Override
    public void handle() {
        String username = controller.getUsernameTextField().getText().trim();
        String email = controller.getEmailTextField().getText();
        String password = controller.getPasswordField().getText();
        String confirmPassword = controller.getConfirmPasswordField().getText();

        if (!controller.checkIfFieldsAreBlank() && EmailUtil.checkIfEmailValid(email) && PasswordUtil.checkIfPasswordValid(password) && password.equals(confirmPassword) && PasswordUtil.matchPassword(password, confirmPassword)) {
            // Permission and settings aanmaken die worden gekoppeld aan een account
            Settings settings = controller.createAndSaveSettings();
            Permission permissions = controller.createAndSavePermissions();

            // Nieuwe account toevoegen in database
            AccountDatabaseUtil.getInstance().add(new Account(-1, settings, permissions, username, email, password, controller.getStatusToggle().isSelected()));
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Account is aangemaakt", "Het account is succesvol geregistreerd.");
            controller.resetRegisterFields();
        }
    }
}
