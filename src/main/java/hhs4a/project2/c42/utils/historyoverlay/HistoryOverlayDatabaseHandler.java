package hhs4a.project2.c42.utils.historyoverlay;

import hhs4a.project2.c42.enums.SortOption;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.chatscreen.*;
import hhs4a.project2.c42.utils.database.databaseutils.*;

import java.util.List;

public class HistoryOverlayDatabaseHandler {
    private static HistoryOverlayDatabaseHandler instance;

    public static HistoryOverlayDatabaseHandler getInstance() {
        if (instance == null) {
            instance = new HistoryOverlayDatabaseHandler();
        }
        return instance;
    }
    private final AbstractDatabaseUtilTemplate<ConversationHistory> historyDatabaseUtil = HistoryDatabaseUtil.getInstance();
    private final AbstractDatabaseUtilTemplate<Conversation> conversationDatabaseUtil = ConversationDatabaseUtil.getInstance();
    private final AbstractDatabaseUtilTemplate<Question> questionDatabaseUtil = QuestionDatabaseUtil.getInstance();
    private final AbstractDatabaseUtilTemplate<Answer> answerDatabaseUtil = AnswerDatabaseUtil.getInstance();

    public void deleteConversations(List<Chat> chats){
        for (Chat chat : chats) {
            List<Conversation> conversations = conversationDatabaseUtil.getConversations(chat);
            for (Conversation conversation : conversations) {
                conversationDatabaseUtil.remove(conversation);
                questionDatabaseUtil.remove(conversation.getQuestion());
                answerDatabaseUtil.remove(conversation.getAnswer());
            }
            ChatDatabaseUtil.getInstance().remove(chat);
        }
        System.out.println(ChatDatabaseUtil.getInstance().getChatsFromAccount(LoggedInAccountHolder.getInstance().getAccount()).size());
    }

    public List<ConversationHistory> getConversationHistories(SortOption sortOption) {
        return historyDatabaseUtil.getHistory(LoggedInAccountHolder.getInstance().getAccount(), sortOption);
    }

}
