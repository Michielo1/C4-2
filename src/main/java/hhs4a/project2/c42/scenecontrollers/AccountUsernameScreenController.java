package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.scenecontrollers.actionhandlers.accountusernamescreen.SaveUsernameChangesButtonClick;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class AccountUsernameScreenController implements Screencontroller {

    /*
        Variables
     */

    @FXML
    private MFXTextField usernameTextField;
    @FXML
    private MFXPasswordField confirmPasswordField;

    /*
        Getters
     */

    public MFXTextField getUsernameTextField() {
        return usernameTextField;
    }

    public MFXPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    /*
        Util methods
     */

    public void updateAccountUsernameInDatabase(Account account) {
        AbstractDatabaseUtilTemplate<Account> accountDatabaseUtil = AccountDatabaseUtil.getInstance();

        if (accountDatabaseUtil.update(account)) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Gebruikersnaam aangepast", "Uw gebruikersnaam is succesvol aangepast");
            if (LoggedInAccountHolder.getInstance().getAccount().getId() == account.getId()) {
                LoggedInAccountHolder.getInstance().getAccount().setUsername(account.getUsername());
            }
            // velden leeg maken
            usernameTextField.clear();
            confirmPasswordField.clear();
        } else {
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Fout bij gebruikersnaamwijziging", "Er is een fout opgetreden bij het aanpassen van uw gebruikersnaam. Probeer het alstublieft opnieuw.");
        }
    }

    public boolean checkIfTextFieldEmpty(String username, String confirmPassword) {
        // Checken of gebruikersnaam leeg is
        if (username.isBlank()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Gebruikersnaam ontbreekt", "Het gebruikersnaamveld is niet ingevuld. Vul alstublieft uw nieuwe gebruikersnaam in.");
            return true;
        }

        // Checken of wachtwoord leeg is
        if (confirmPassword.isBlank()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Bevestigingswachtwoord ontbreekt", "Het bevestigingswachtwoordveld is niet ingevuld. Vul alstublieft uw bevestigingswachtwoord in.");
            return true;
        }
        return false;
    }

    /*
        Action handlers
     */

    public void onSaveUsernameChangesClick() {
        SaveUsernameChangesButtonClick saveUsernameChangesButtonClick = new SaveUsernameChangesButtonClick();
        saveUsernameChangesButtonClick.setup(this);
        saveUsernameChangesButtonClick.handle();
    }
}