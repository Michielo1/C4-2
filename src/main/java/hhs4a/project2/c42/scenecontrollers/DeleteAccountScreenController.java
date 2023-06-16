package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.scenecontrollers.actionhandlers.deleteaccountscreen.DeleteAccountButtonClick;
import hhs4a.project2.c42.utils.ShowScene;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Optional;

public class DeleteAccountScreenController implements Screencontroller {

    /*
        Variables
     */

    @FXML
    public BorderPane deleteAccountBorderPane;
    @FXML
    public MFXPasswordField passwordField;

    /*
        Getters
     */

    public MFXPasswordField getPasswordField() {
        return passwordField;
    }

    /*
        Util methods
     */

    public void logoutAndReturnToLoginScreen() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hhs4a/project2/c42/fxml/LoginScreen.fxml"));
        ShowScene.goToLoginScreen((Stage) deleteAccountBorderPane.getScene().getWindow(), loader, getClass());
        // Reset account id and chat id
        LoggedInAccountHolder.getInstance().setAccount();
        ConversationHistoryHolder.getInstance().getConversationHistory().setChat(new Chat());
    }

    public void deleteAccountConfirmation(Account account) {
        Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Definitief account verwijderen", "Weet u zeker dat u het account definitief wilt verwijderen?");

        if (result.isPresent() && result.get().getText().equals("Ja")) {
            AbstractDatabaseUtilTemplate<Account> accountDatabaseUtil = AccountDatabaseUtil.getInstance();

            // Loop door alle gerelateerde gegevens die zijn gekoppeld aan het account van de gebruiker en verwijder alles
            accountDatabaseUtil.remove(account);

            // De gebruiker uitloggen als het hetzelfde is, anders naar settings scherm
            if (LoggedInAccountHolder.getInstance().getAccount().getId() == account.getId()) {
                logoutAndReturnToLoginScreen();
            }

            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Account succesvol verwijderd", "Het account is succesvol verwijderd.");
        } else {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Account verwijdering geannuleerd", "Het verwijderen van het account is geannuleerd.");
        }

        // Clear password
        passwordField.clear();
    }

    /*
        Button handlers
     */

    public void onDeleteAccountButtonClick() {
        DeleteAccountButtonClick deleteAccountButtonClick = new DeleteAccountButtonClick();
        deleteAccountButtonClick.setup(this);
        deleteAccountButtonClick.handle();
    }

}