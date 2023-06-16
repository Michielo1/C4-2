package hhs4a.project2.c42.scenecontrollers.actionhandlers.chatscreen;

import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.chatscreen.ChatHandler;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class CancelButtonClick extends ChatscreenHandler implements Actionhandler {

    @Override
    public void setup(Screencontroller screencontroller) {
        super.setup(screencontroller);
    }

    @Override
    public void handle() {
        System.out.println("Cancel button clicked");

        Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Annulering van vraag", "Weet u zeker dat u de vraag wilt annuleren?");

        if (result.isPresent() && result.get().getText().equals("Ja")) {
            //Setting the on action of the send button back to the original
            //This is not needed when the user presses no, since the onaction will be reverted at the end of the task/thread
            controller.getSendButton().setOnAction(actionEvent -> controller.onSendButtonClick());
            controller.getSendButton().setText("Versturen");
            controller.getSendButton().setDisable(false);
            controller.getDeleteConversationButton().setDisable(false);
            controller.getNewConversationButton().setDisable(false);
            ChatHandler chatHandler = ChatHandler.getInstance();
            chatHandler.cancelPrompt();
        }
    }
}
