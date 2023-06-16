package hhs4a.project2.c42.scenecontrollers.actionhandlers.settingsscreen;

import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.SettingsScreenController;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;

public class RegisterAccountButtonClick implements Actionhandler {

    private SettingsScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof SettingsScreenController)) return;
        controller = (SettingsScreenController) screencontroller;
    }

    @Override
    public void handle() {
        controller.setScreen("/hhs4a/project2/c42/fxml/RegisterAccountScreen.fxml");
    }
}
