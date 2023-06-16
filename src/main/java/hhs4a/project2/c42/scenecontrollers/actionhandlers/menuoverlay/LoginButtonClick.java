package hhs4a.project2.c42.scenecontrollers.actionhandlers.menuoverlay;

import hhs4a.project2.c42.scenecontrollers.MenuOverlayScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.ShowScene;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginButtonClick implements Actionhandler {

    private MenuOverlayScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof MenuOverlayScreenController)) return;
        controller = (MenuOverlayScreenController) screencontroller;
    }

    @Override
    public void handle() {
        // Bevestigings alert aanmaken en tonen
        Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Uitloggen", "Weet u zeker dat u wilt uitloggen?");
        // Uitvoeren als de gebruiker op "ja" klikt
        if (result.isPresent() && result.get().getText().equals("Ja")) { // Met ButtonData.YES werkt het niet, dus op deze manier maar
            // Print message in console
            System.out.println("-- Gebuiker heeft op 'ja' geklikt en wordt uitgelogd. --");
            // Load the LoginScreen.fxml file and get the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/hhs4a/project2/c42/fxml/LoginScreen.fxml"));
            ShowScene.goToLoginScreen((Stage) controller.getOverlayScreenBorderPane().getScene().getWindow(), loader, getClass());

            // Reset chat and account
            ConversationHistoryHolder.getInstance().getConversationHistory().setChat(new Chat());
            LoggedInAccountHolder.getInstance().setAccount(new Account());
        }
        else {
            System.out.println("-- Gebruiker heeft op 'nee' geklikt en wordt niet uitgelogd. --");
        }
    }

}
