package hhs4a.project2.c42.utils.chatscreen;

import java.util.ArrayList;
import java.util.Date;

public class ConversationHistoryHolder {
    private static volatile ConversationHistoryHolder instance;
    private ConversationHistory conversationHistory;

    private ConversationHistoryHolder() {
        conversationHistory = new ConversationHistory(new Chat(), new ArrayList<>(), new Date(System.currentTimeMillis()), -1);
    }

    public static ConversationHistoryHolder getInstance() {
        if (instance == null) {
            synchronized (ConversationHistoryHolder.class) {
                if (instance == null) {
                    instance = new ConversationHistoryHolder();
                }
            }
        }
        return instance;
    }

    public ConversationHistory getConversationHistory() {
        return conversationHistory;
    }

    public void setConversationHistory(ConversationHistory conversationHistory) {
        this.conversationHistory = conversationHistory;
    }
}
