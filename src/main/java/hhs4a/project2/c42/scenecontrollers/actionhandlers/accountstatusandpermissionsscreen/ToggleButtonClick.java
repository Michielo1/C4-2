package hhs4a.project2.c42.scenecontrollers.actionhandlers.accountstatusandpermissionsscreen;

import hhs4a.project2.c42.scenecontrollers.AccountStatusAndPermissionScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.database.databaseutils.PermissionDatabaseUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ToggleButtonClick implements Actionhandler {

    private AccountStatusAndPermissionScreenController controller;
    private ActionEvent event;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof AccountStatusAndPermissionScreenController)) return;
        controller = (AccountStatusAndPermissionScreenController) screencontroller;
    }

    public void setEvent(ActionEvent event) {
        this.event = event;
    }

    @Override
    public void handle() {
        Object source = event.getSource();

        Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Bevestig rechtaanpassing", "Weet u zeker dat u dit recht wilt aanpassen?");

        if (result.isPresent() && result.get().getText().equals("Ja")) {
            // Op basis van de source, selecteer de juiste recht
            controller.updateAccountPermissions(source);
            // Wijzig het in de database
            if (PermissionDatabaseUtil.getInstance().update(controller.getAccount().getPermissions())) {
                AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Rechtaanpassing succesvol", "Het recht is succesvol aangepast van de gebruiker.");
            } else {
                AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Fout bij rechtaanpassing", "Er is een fout opgetreden bij het aanpassen van het recht van de gebruiker. Probeer het alstublieft opnieuw.");
            }
        } else {
            controller.updateToggleButtonState();
        }
    }
}