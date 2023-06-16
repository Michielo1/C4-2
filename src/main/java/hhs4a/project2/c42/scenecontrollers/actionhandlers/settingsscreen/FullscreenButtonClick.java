package hhs4a.project2.c42.scenecontrollers.actionhandlers.settingsscreen;

import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.SettingsScreenController;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import javafx.stage.Stage;

public class FullscreenButtonClick implements Actionhandler {

    private SettingsScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof SettingsScreenController)) return;
        controller = (SettingsScreenController) screencontroller;
    }

    @Override
    public void handle() {
        // get the stage from the button's scene
        Stage stage = (Stage) controller.getFullscreenButton().getScene().getWindow();
        // toggle the full screen mode
        stage.setFullScreen(!stage.isFullScreen());
    }

}
