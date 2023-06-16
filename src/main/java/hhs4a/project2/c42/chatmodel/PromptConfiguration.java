package hhs4a.project2.c42.chatmodel;

import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;

import java.util.List;

public abstract class PromptConfiguration {

    /*
        Variables
     */

    private final int maxTokens;
    private final boolean supportsMultiThreading;
    private final int threadCount;
    private final ConversationHistory conversationHistory;
    private String completePrompt;

    /*
        Constructor
     */

    public PromptConfiguration(ConversationHistory conversationHistory, int maxTokens, boolean supportsMultiThreading, int threadCount) {
        this.maxTokens = maxTokens;
        this.supportsMultiThreading = supportsMultiThreading;
        this.threadCount = threadCount;
        this.conversationHistory = conversationHistory;
    }


    /*
        Getters
     */

    public String getCompletePrompt() {
        // add the complete prompt that is used in the promptgenerator
        return completePrompt;
    }

    public void setCompletePrompt(String completePrompt) {
        this.completePrompt = completePrompt;
    }

    public boolean promptIsValid() {
        String[] splittedPrompt = conversationHistory.getConversation().get(conversationHistory.getConversation().size() - 1).getText().split("");
        return splittedPrompt.length <= maxTokens;
    }

    public List<Double> getWeights() {
        // if there are no weights that are adjustable or do not need to be parsed, this can be null
        // weights are meant to be hardcoded to avoid models with changing weights
        return null;
    }

    public boolean supportsMultiThreading() {
        return supportsMultiThreading;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public String getModel() {
        return "EXAMPLE_MODEL";
    }

}