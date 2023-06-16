package hhs4a.project2.c42.scenecontrollers.actionhandlers.chatscreen;

import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.alert.AlertUtil;
import hhs4a.project2.c42.utils.alert.ConfirmationUtil;
import hhs4a.project2.c42.utils.chatscreen.Conversation;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import hhs4a.project2.c42.utils.database.databaseutils.AnswerDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.ConversationDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.QuestionDatabaseUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class DeleteConversationButtonClick extends ChatscreenHandler implements Actionhandler {

    @Override
    public void setup(Screencontroller screencontroller) {
        super.setup(screencontroller);
    }

    @Override
    public void handle() {
        Optional<ButtonType> result = ConfirmationUtil.getInstance().showConfirmation("Bevestig conversatie verwijdering", "Weet u zeker dat u de huidige conversatie wilt verwijderen?");

        if (result.isPresent() && result.get().getText().equals("Ja")) {

            List<Conversation> conversationQuestionAnswer = ConversationDatabaseUtil.getInstance().getConversations(ConversationHistoryHolder.getInstance().getConversationHistory().getChat());

            for (Conversation conversation : conversationQuestionAnswer) {
                ConversationDatabaseUtil.getInstance().remove(conversation);
                QuestionDatabaseUtil.getInstance().remove(conversation.getQuestion());
                AnswerDatabaseUtil.getInstance().remove(conversation.getAnswer());
            }

            controller.clearMessageAndConversation();
            controller.addStartingQuestion();
            AlertUtil.getInstance().showAlert(Alert.AlertType.INFORMATION, "Conversatie is gewist", "De huidige conversatie is succesvol gewist.");
        }
    }
}
