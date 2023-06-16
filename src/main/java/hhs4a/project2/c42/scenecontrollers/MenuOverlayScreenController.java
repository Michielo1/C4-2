package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.scenecontrollers.actionhandlers.menuoverlay.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuOverlayScreenController implements Initializable, Screencontroller {

    /*
        Variables
     */

    @FXML
    private BorderPane overlayScreenBorderPane;
    public static boolean chatHistoryOverlayOpen = false;
    private boolean inChatScreen = false;
    public static ChatScreenController chatScreenController;

    /*
        Getters
     */

    public boolean isInChatScreen() {
        return inChatScreen;
    }

    public BorderPane getOverlayScreenBorderPane() {
        return overlayScreenBorderPane;
    }

    /*
        Initialize (gets called from javafx)
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //load in chat screen on startup
        screenSwitcher("/hhs4a/project2/c42/fxml/ChatScreen.fxml");
    }

    /*
        Util methods
     */

    //the code for loading in the different windows
    public void screenSwitcher(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            overlayScreenBorderPane.setCenter(loader.load());
            inChatScreen = fxmlFile.equals("/hhs4a/project2/c42/fxml/ChatScreen.fxml");
            if(inChatScreen){
                //if we are in the chat screen we need to get the controller for the chat screen, to be able to call methods on it
                chatScreenController = loader.getController();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        Button handlers
     */

    // Code that runs when the user clicks on the chat button
    public void chatScreenButtonClick() {
        ChatButtonClick chatButtonClick = new ChatButtonClick();
        chatButtonClick.setup(this);
        chatButtonClick.handle();
    }

    // Method that runs when the user clicks on the chat geschiedenis button
    public void chatHistoryOverlayButtonClick() {
        ChatHistoryOverlayButtonClick chatHistoryOverlayButtonClick = new ChatHistoryOverlayButtonClick();
        chatHistoryOverlayButtonClick.setup(this);
        chatHistoryOverlayButtonClick.handle();
    }

    // Code that runs when the user clicks on the settings button
    public void settingsButtonClick() {
        SettingsButtonClick settingsButtonClick = new SettingsButtonClick();
        settingsButtonClick.setup(this);
        settingsButtonClick.handle();
    }

    // Code for logout button, switches screens to login screen and sets accountID to 0
    public void loginButtonClick() {
        LoginButtonClick loginButtonClick = new LoginButtonClick();
        loginButtonClick.setup(this);
        loginButtonClick.handle();
    }

    public void accountButtonClick() {
        AccountButtonClick accountButtonClick = new AccountButtonClick();
        accountButtonClick.setup(this);
        accountButtonClick.handle();
    }

}