package hhs4a.project2.c42.scenecontrollers.actionhandlers.chatscreen;

import hhs4a.project2.c42.scenecontrollers.ChatScreenController;
import hhs4a.project2.c42.scenecontrollers.Screencontroller;
import hhs4a.project2.c42.scenecontrollers.actionhandlers.Actionhandler;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistoryHolder;
import hhs4a.project2.c42.utils.chatscreen.Question;
import hhs4a.project2.c42.utils.database.databaseutils.QuestionDatabaseUtil;
import hhs4a.project2.c42.utils.message.UserMessage;

public class SendButtonClick extends ChatscreenHandler implements Actionhandler {

    @Override
    public void setup(Screencontroller screencontroller) {
        super.setup(screencontroller);
    }

    @Override
    public void handle() {
        // Clearing the messages list and the conversation in the conversation history if the first message is the "start" message
        // Bit of a hacky way, might need to change later but for now it works
        if (!ChatScreenController.getMessages().isEmpty() && ChatScreenController.getMessages().get(0).getId() == -60) {
            controller.clearMessageAndConversation();
        }

        Question question = new Question(controller.getPromptTextField().getText(), -1);
        question.setId(QuestionDatabaseUtil.getInstance().add(question));

        UserMessage userMessage = new UserMessage(question);
        ChatScreenController.getMessages().add(userMessage);
        ConversationHistoryHolder.getInstance().getConversationHistory().getConversation().add(userMessage);

        // Schakel de knoppen uit
        controller.getDeleteConversationButton().setDisable(true);
        controller.getNewConversationButton().setDisable(true);
        controller.getLanguageSelectorComboBox().setDisable(true);
        controller.getPromptTextField().clear();

        controller.sendMessageToChatbot(question);
    }

}
