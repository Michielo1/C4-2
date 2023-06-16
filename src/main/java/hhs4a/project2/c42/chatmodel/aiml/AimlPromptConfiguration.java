package hhs4a.project2.c42.chatmodel.aiml;

import hhs4a.project2.c42.chatmodel.PromptConfiguration;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import hhs4a.project2.c42.utils.message.MessageInterface;

public class AimlPromptConfiguration extends PromptConfiguration {
    public AimlPromptConfiguration(ConversationHistory conversationHistory, int maxTokens, boolean supportsMultiThreading, int threadCount) {
        super(conversationHistory, maxTokens, supportsMultiThreading, threadCount);

        // Remove all messages from the conversation history that are not the last message from the user
        for (int i = conversationHistory.getConversation().size() - 1; i >= 0; i--) {
            MessageInterface message = conversationHistory.getConversation().get(i);
            if (message.isUser()) {
                conversationHistory.getConversation().clear();
                conversationHistory.getConversation().add(message);
                break;
            }
        }

        super.setCompletePrompt(conversationHistory.getConversation().get(conversationHistory.getConversation().size() - 1).getText());
    }

    public String getCompletePrompt() {
        return super.getCompletePrompt();
    }
}