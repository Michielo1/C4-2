package hhs4a.project2.c42.scenecontrollers.actionhandlers.menuoverlay;

import hhs4a.project2.c42.scenecontrollers.MenuOverlayScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;

public class ChatHistoryOverlayButtonClick implements Actionhandler {

    private MenuOverlayScreenController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof MenuOverlayScreenController)) return;
        controller = (MenuOverlayScreenController) screencontroller;
    }

    @Override
    public void handle() {
        System.out.println("Chat history overlay button clicked");
        if(controller.isInChatScreen() && MenuOverlayScreenController.chatScreenController != null){
            //if we are in the chat screen the chatScreenController should never be null, but checking to be sure
            if(!MenuOverlayScreenController.chatHistoryOverlayOpen){
                MenuOverlayScreenController.chatHistoryOverlayOpen = true;
                MenuOverlayScreenController.chatScreenController.showChatHistoryOverlay();
            } else {
                MenuOverlayScreenController.chatHistoryOverlayOpen = false;
                MenuOverlayScreenController.chatScreenController.hideChatHistoryOverlay();
            }
        } else {
            System.out.println("Chat history overlay button clicked, but we are not in the chat screen");
        }
    }

}
