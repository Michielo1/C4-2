package hhs4a.project2.c42.scenecontrollers.actionhandlers.loginscreen;

import hhs4a.project2.c42.scenecontrollers.LoginScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import hhs4a.project2.c42.utils.password.PasswordUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class LoginButtonClick implements Actionhandler {

    private LoginScreenController controller;
    private ActionEvent actionEvent;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof LoginScreenController)) return;
        controller = (LoginScreenController) screencontroller;
    }

    public void setActionEvent(ActionEvent actionEvent) {
        this.actionEvent = actionEvent;
    }

    @Override
    public void handle() {
        AbstractDatabaseUtilTemplate<Account> accountDatabaseUtil = AccountDatabaseUtil.getInstance();
        // Standaard velden
        String usernameOrEmail = controller.getUsernameOrEmailTextField().getText();
        String password = controller.getPasswordField().getText();

        if (controller.checkIfTextFieldEmpty(usernameOrEmail, password)) return;

        // Account ophalen
        Account account = accountDatabaseUtil.getAccount(usernameOrEmail, usernameOrEmail);

        // Controleren of het account bestaat met de ingevoerde gebruikersnaam of e-mailadres
        if (account == null) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Foutmelding account", "Het account bestaat niet. Controleer alstublieft uw gebruikersnaam of e-mailadres.");
            return;
        }

        // Controleren of het ingevoerde wachtwoord overeenkomt met het opgeslagen wachtwoord in de database
        if (PasswordUtil.verifyPassword(account.getPassword(), password) && !controller.isAccountBlocked(account)) {
            LoggedInAccountHolder.getInstance().setAccount(account);
            controller.openChatScreen(actionEvent);
        }
    }


}
