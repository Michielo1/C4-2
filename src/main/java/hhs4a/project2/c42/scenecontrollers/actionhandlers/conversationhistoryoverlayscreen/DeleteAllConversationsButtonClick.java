package hhs4a.project2.c42.scenecontrollers.actionhandlers.conversationhistoryoverlayscreen;

import hhs4a.project2.c42.scenecontrollers.ConversationHistoryOverlayController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.database.databaseutils.ChatDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.HistoryDatabaseUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DeleteAllConversationsButtonClick implements Actionhandler {

    private ConversationHistoryOverlayController controller;

    @Override
    public void setup(Screencontroller screencontroller) {
        if (!(screencontroller instanceof ConversationHistoryOverlayController)) return;
        controller = (ConversationHistoryOverlayController) screencontroller;
    }

    @Override
    public void handle() {
        if (!ConversationHistoryOverlayController.getChatHistory().isEmpty()) {
            Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Geschiedenis wissen bevestiging", "Weet u zeker dat u uw geschiedenis wilt wissen?");
            if (result.isPresent() && result.get().getText().equals("Ja")) {

                if (HistoryDatabaseUtil.getInstance().clearHistory(LoggedInAccountHolder.getInstance().getAccount().getId())) {
                    // delete all relations in database
                    controller.deleteConversationsAndClearChatHistory(ChatDatabaseUtil.getInstance().getChatsFromAccount(LoggedInAccountHolder.getInstance().getAccount()));
                } else {
                    AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Geschiedenis wissen mislukt", "Er is een fout opgetreden bij het verwijderen van de geschiedenis.");
                }
            }
        } else {
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Geschiedenis is leeg", "Er zijn momenteel geen chats om te verwijderen in uw geschiedenis.");
        }
    }
}