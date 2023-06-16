package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.scenecontrollers.actionhandlers.accountemailscreen.SaveEmailChangesButtonClick;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AbstractDatabaseUtilTemplate;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class AccountEmailScreenController implements Screencontroller {

    /*
        variables
     */

    @FXML
    private MFXTextField emailTextField;
    @FXML
    private MFXPasswordField confirmPasswordField;

    /*
        Getters
     */

    public MFXTextField getEmailTextField() {
        return emailTextField;
    }

    public MFXPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    /*
        Util methods
     */

    public void updateAccountEmailInDatabase(Account account) {
        AbstractDatabaseUtilTemplate<Account> accountDatabaseUtil = AccountDatabaseUtil.getInstance();

        if (accountDatabaseUtil.update(account)) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "E-mailadres aangepast", "Uw e-mailadres is succesvol aangepast.");
            if (account.getId() == LoggedInAccountHolder.getInstance().getAccount().getId()) {
                LoggedInAccountHolder.getInstance().getAccount().setEmail(account.getEmail());
            }
            // velden leeg maken
            emailTextField.clear();
            confirmPasswordField.clear();
        } else {
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Fout bij e-mailadreswijziging", "Er is een fout opgetreden bij het aanpassen van uw e-mailadres. Probeer het alstublieft opnieuw.");
        }
    }

    public boolean checkIfTextFieldEmpty(String email, String confirmPassword) {
        // Controleren of e-mailadres leeg is
        if (email.isBlank()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "E-mailadres ontbreekt", "Het e-mailadresveld is niet ingevuld. Vul alstublieft uw nieuwe e-mailadres in.");
            return true;
        }
        // Controleren of wachtwoord leeg is
        if (confirmPassword.isBlank()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Bevestigingswachtwoord ontbreekt", "Het bevestigingswachtwoordveld is niet ingevuld. Vul dit veld alstublieft in.");
            return true;
        }
        return false;
    }

    /*
        Button handlers
     */

    public void onSaveEmailChangesClick() {
        SaveEmailChangesButtonClick saveEmailChangesButtonClick = new SaveEmailChangesButtonClick();
        saveEmailChangesButtonClick.setup(this);
        saveEmailChangesButtonClick.handle();
    }

}