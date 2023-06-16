package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.scenecontrollers.actionhandlers.accountpasswordscreen.SavePasswordChangesButtonClick;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class AccountPasswordScreenController implements Screencontroller {

    /*
        Variables
     */

    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private MFXPasswordField confirmPasswordField;

    /*
        Getters
     */

    public MFXPasswordField getPasswordField() {
        return passwordField;
    }

    public MFXPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    /*
        Util methods
     */

    public void updateAccountPasswordInDatabase(Account account) {
        AbstractDatabaseUtilTemplate<Account> accountDatabaseUtil = AccountDatabaseUtil.getInstance();

        if (accountDatabaseUtil.update(account)) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Wachtwoord aangepast", "Uw wachtwoord is succesvol aangepast.");
            if (LoggedInAccountHolder.getInstance().getAccount().getId() == account.getId()) {
                LoggedInAccountHolder.getInstance().getAccount().setPassword(account.getPassword());
            }
            // velden leeg maken
            passwordField.clear();
            confirmPasswordField.clear();
        } else {
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Fout bij wachtwoordwijziging", "Er is een fout opgetreden bij het aanpassen van uw wachtwoord. Probeer het alstublieft opnieuw.");
        }
    }

    public boolean checkIfTextFieldEmpty(String password, String confirmPassword) {
        // Controleren of wachtwoord leeg is
        if (password.isBlank()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Wachtwoord ontbreekt", "Het wachtwoordveld is niet ingevuld. Vul alstublieft uw nieuwe wachtwoord in.");
            return true;
        }
        // Controleren of bevestigingswachtwoord leeg is
        if (confirmPassword.isBlank()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Bevestigingswachtwoord ontbreekt", "Het bevestigingswachtwoordveld is niet ingevuld. Vul alstublieft uw bevestigingswachtwoord in.");
            return true;
        }
        return false;
    }

    /*
        Button handlers
     */

    public void onSavePasswordChangesClick() {
        SavePasswordChangesButtonClick savePasswordChangesButtonClick = new SavePasswordChangesButtonClick();
        savePasswordChangesButtonClick.setup(this);
        savePasswordChangesButtonClick.handle();
    }

}