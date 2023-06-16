package hhs4a.project2.c42.scenecontrollers.actionhandlers.chatscreen;

import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import javafx.scene.control.Alert;

public class CancelEditSubjectButtonClick extends ChatscreenHandler implements Actionhandler {

    private String oldSubject;

    @Override
    public void setup(Screencontroller screencontroller) {
        super.setup(screencontroller);
    }

    public void setOldSubject(String oldSubject) {
        this.oldSubject = oldSubject;
    }

    @Override
    public void handle() {
        if(ConversationHistoryHolder.getInstance().getConversationHistory().getChat().getChatId() > 0) {
            // if the old subject is null, set the text to an empty string, otherwise set it to the old subject
            controller.getSubjectTextField().setText(oldSubject == null ? "" : oldSubject);
            // Enable promptTextField and all buttons except for the confirm and cancel buttons
            controller.enableButtonsExpectConfirmCancel();
        } else {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Info", "Voer een onderwerp in om te beginnen met chatten.");
        }
    }
}
