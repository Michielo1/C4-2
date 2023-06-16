package hhs4a.project2.c42.scenecontrollers.actionhandlers.menuoverlay;

import hhs4a.project2.c42.scenecontrollers.MenuOverlayScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;

public class ChatButtonClick implements Actionhandler {

    private MenuOverlayScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof MenuOverlayScreenController)) return;
        controller = (MenuOverlayScreenController) screencontroller;
    }

    @Override
    public void handle() {
        MenuOverlayScreenController.chatHistoryOverlayOpen = false;
        controller.screenSwitcher("/hhs4a/project2/c42/fxml/ChatScreen.fxml");
    }

}
