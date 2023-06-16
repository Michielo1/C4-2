package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.enums.SortOption;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.AccountGenerator;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.ChatDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.HistoryDatabaseUtil;
import hhs4a.project2.c42.utils.historyoverlay.HistoryOverlayDatabaseHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ConversationHistoryOverlayControllerTest {
    private Account account;
    private ArrayList<ConversationHistory> conversationHistories;
    private ArrayList<Chat> chats;

    @BeforeEach
    void setUp() {
        account = AccountGenerator.generateAccount();
        LoggedInAccountHolder.getInstance().setAccount(account);
        conversationHistories = new ArrayList<>();
        chats = new ArrayList<>();
        generateConversationHistory();
    }

    @AfterEach
    void tearDown() {
        for(ConversationHistory conversationHistory : conversationHistories) {
            HistoryDatabaseUtil.getInstance().processRemoveObject(conversationHistory);
        }
        for(Chat chat : chats) {
            ChatDatabaseUtil.getInstance().processRemoveObject(chat);
        }
        AccountDatabaseUtil.getInstance().processRemoveObject(account);
    }

    private void generateConversationHistory() {
        for(int i = 0; i < 10; i++) {
            String subject = AccountGenerator.generateEmail(true).split("@")[0];
            String lastActive = AccountGenerator.randomDate();
            Chat chat = new Chat(-1, subject, Model.AIML_NL, lastActive, "nederlands");
            chat.setChatId(ChatDatabaseUtil.getInstance().processAddObject(chat));
            ConversationHistory conversationHistory = new ConversationHistory(chat, new ArrayList<>(), new Date(System.currentTimeMillis()), -1);
            conversationHistory.setId(HistoryDatabaseUtil.getInstance().processAddObject(conversationHistory));
            conversationHistories.add(conversationHistory);
            chats.add(chat);
        }
    }

    @Test
    void allChatHistoryContainsDateAndSubject() {
        for(ConversationHistory conversationHistory : conversationHistories) {
            assertFalse(conversationHistory.getChat().getLastActive().isBlank());
            assertFalse(conversationHistory.getChat().getSubject().isBlank());
        }
    }

    @Test
    void deleteAllConversations(){
        assertFalse(conversationHistories.isEmpty());
        assertFalse(chats.isEmpty());

        HistoryOverlayDatabaseHandler.getInstance().deleteConversations(chats);

        assertEquals(0, ChatDatabaseUtil.getInstance().getChatsFromAccount(LoggedInAccountHolder.getInstance().getAccount()).size());
    }

    @Test
    void sortByDateAsc(){
        conversationHistories.clear();
        conversationHistories.addAll(HistoryOverlayDatabaseHandler.getInstance().getConversationHistories(SortOption.LAST_ACTIVE_ASC));
        for(int i = 0; i < conversationHistories.size() - 1; i++){
            assertFalse(conversationHistories.get(i).getChat().getLastActive().compareTo(conversationHistories.get(i + 1).getChat().getLastActive()) > 0);
        }
    }

    @Test
    void sortByDateDesc() {
        conversationHistories.clear();
        conversationHistories.addAll(HistoryOverlayDatabaseHandler.getInstance().getConversationHistories(SortOption.LAST_ACTIVE_DESC));

        for (int i = 0; i < conversationHistories.size() - 1; i++) {
            assertFalse(conversationHistories.get(i).getChat().getLastActive().compareTo(conversationHistories.get(i + 1).getChat().getLastActive()) < 0);
        }
    }

    @Test
    void sortBySubjectAsc() {
        conversationHistories.clear();
        conversationHistories.addAll(HistoryOverlayDatabaseHandler.getInstance().getConversationHistories(SortOption.SUBJECT_ASC));

        for (int i = 0; i < conversationHistories.size() - 1; i++) {
            String currentSubject = conversationHistories.get(i).getChat().getSubject();
            String nextSubject = conversationHistories.get(i + 1).getChat().getSubject();
            assertTrue(currentSubject.compareToIgnoreCase(nextSubject) <= 0);
        }
    }

    @Test
    void sortBySubjectDesc() {
        conversationHistories.clear();
        conversationHistories.addAll(HistoryOverlayDatabaseHandler.getInstance().getConversationHistories(SortOption.SUBJECT_DESC));

        for (int i = 0; i < conversationHistories.size() - 1; i++) {
            String currentSubject = conversationHistories.get(i).getChat().getSubject();
            String nextSubject = conversationHistories.get(i + 1).getChat().getSubject();
            assertTrue(currentSubject.compareToIgnoreCase(nextSubject) >= 0);
        }
    }


}