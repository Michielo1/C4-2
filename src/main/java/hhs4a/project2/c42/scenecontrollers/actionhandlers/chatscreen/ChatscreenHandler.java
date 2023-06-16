package hhs4a.project2.c42.scenecontrollers.actionhandlers.chatscreen;

import hhs4a.project2.c42.scenecontrollers.ChatScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;

public abstract class ChatscreenHandler {

    public ChatScreenController controller;

    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof ChatScreenController)) return;
        controller = (ChatScreenController) screencontroller;
    }

}
