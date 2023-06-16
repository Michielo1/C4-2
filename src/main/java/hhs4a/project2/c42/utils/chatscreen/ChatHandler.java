package hhs4a.project2.c42.utils.chatscreen;

import hhs4a.project2.c42.chatmodel.AiConnection;

public class ChatHandler {

    private static ChatHandler instance;
    public static ChatHandler getInstance() {
        if (instance == null) instance = new ChatHandler();
        return instance;
    }

    private AiConnection aiConnection;

    public String handleUserInput(ConversationHistory conversationHistory) {
        aiConnection = new AiConnection(conversationHistory.getChat().getModel());
        if (!aiConnection.hasValidModel()) {
            return "INVALID MODEL";
        }

        // TODO; translate if dutch is selected

        return aiConnection.prompt(conversationHistory);
    }

    public void cancelPrompt() {
        aiConnection.cancelPrompt();
    }


}
