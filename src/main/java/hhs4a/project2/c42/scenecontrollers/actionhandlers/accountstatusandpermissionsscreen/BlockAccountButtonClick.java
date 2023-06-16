package hhs4a.project2.c42.scenecontrollers.actionhandlers.accountstatusandpermissionsscreen;

import hhs4a.project2.c42.scenecontrollers.AccountStatusAndPermissionScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class BlockAccountButtonClick implements Actionhandler {

    private AccountStatusAndPermissionScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof AccountStatusAndPermissionScreenController)) return;
        controller = (AccountStatusAndPermissionScreenController) screencontroller;
    }

    @Override
    public void handle() {
        Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Account blokkeren", "Weet u zeker dat u dit account wilt blokkeren?");

        if (result.isPresent() && result.get().getText().equals("Ja")) {
            // Account status op true. True = geblokkeerd
            controller.getAccount().setStatus(true);
            // Blokkeer het account in database
            if (AccountDatabaseUtil.getInstance().update(controller.getAccount())) {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Account geblokkeerd", "Uw account is succesvol geblokkeerd.");
            } else {
                AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Fout bij blokkeren van account", "Er is een fout opgetreden bij het blokkeren van uw account. Probeer het alstublieft opnieuw.");
            }
            controller.updateBlockUnblockButtonState();
        }
    }

}
