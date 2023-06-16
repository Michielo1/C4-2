package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.scenecontrollers.actionhandlers.loginscreen.LoginButtonClick;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.css.CSSLoader;
import hhs4a.project2.c42.utils.css.CSSPaths;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Objects;

public class LoginScreenController implements Screencontroller {

    /*
        Variables
     */

    @FXML
    private MFXTextField usernameOrEmailTextField;
    @FXML
    private MFXPasswordField passwordField;

    /*
        Getters
     */

    public MFXTextField getUsernameOrEmailTextField() {
        return usernameOrEmailTextField;
    }

    public MFXPasswordField getPasswordField() {
        return passwordField;
    }

    /*
        Util methods
     */

    public boolean checkIfTextFieldEmpty(String usernameOrEmail, String password) {
        // Controleren of het gebruikersnaam of e-mailadresveld leeg is
        if (usernameOrEmail.isBlank()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Foutmelding tekstveld", "Het gebruikersnaam- of e-mailadresveld is leeg. Vul alstublieft uw gebruikersnaam of e-mailadres in.");
            return true;
        }
        // Controleren of wachtwoordveld leeg is
        if (password.isBlank()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Foutmelding tekstveld", "Het wachtwoordveld is leeg. Vul alstublieft uw wachtwoord in.");
            return true;
        }
        return false;
    }

    public boolean isAccountBlocked(Account account) {
        if (account.getStatus()) {
            AlertUtil.getInstance().showAlert(Alert.AlertType.ERROR, "Account is geblokkeerd", "Uw account is geblokkeerd. Neem alstublieft contact op met de beheerder via 'admin@c4-2.nl' voor verdere assistentie.");
            return true;
        }
        return false;
    }

    public void openChatScreen(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/hhs4a/project2/c42/fxml/MenuOverlayScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage newStage = new Stage();
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            newStage.setMinHeight(600);
            newStage.setMinWidth(800);
            newStage.setTitle("C4-2");

            // Set the theme and image respectively
            scene.getStylesheets().add(CSSLoader.loadCSS(CSSPaths.getInstance().getThemePath(LoggedInAccountHolder.getInstance().getAccount().getSettings().getTheme())));
            newStage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/hhs4a/project2/c42/images/c4-2_bot.png"))));

            MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);

            newStage.setScene(scene);
            newStage.show();
            currentStage.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        Button handlers
     */

    public void onLoginButtonClick(ActionEvent actionEvent) {
        LoginButtonClick loginButtonClick = new LoginButtonClick();
        loginButtonClick.setup(this);
        loginButtonClick.setActionEvent(actionEvent);
        loginButtonClick.handle();
    }

}