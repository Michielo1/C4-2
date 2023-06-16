package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.enums.SortOption;
import hhs4a.project2.c42.utils.account.*;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatDatabaseUtilTest {
    private Account newAccount;
    private Chat chat;
    private ConversationHistory conversationHistory;

    @BeforeEach
    void setUp() {
        generateAccount();
        generateChat();
        generateHistory();
        //TODO: koppel chat aan account
    }

    @AfterEach
    void tearDown() {
        ChatDatabaseUtil.getInstance().processRemoveObject(chat);
        AccountDatabaseUtil.getInstance().processRemoveObject(newAccount);
        HistoryDatabaseUtil.getInstance().processRemoveObject(conversationHistory);
    }

    @Test
    void getChatWithSameAccount(){
        List<ConversationHistory> conversationHistories = HistoryDatabaseUtil.getInstance().getHistory(newAccount, SortOption.DEFAULT);
        System.out.println(conversationHistories.size());
        assertTrue(checkIfContains(conversationHistories));
    }

    @Test
    void getChatWithDifferentAccount(){
        Account adminAccount = AccountDatabaseUtil.getInstance().getAccount("admin", "admin");
        List<ConversationHistory> conversationHistories = HistoryDatabaseUtil.getInstance().getHistory(adminAccount, SortOption.DEFAULT);
        System.out.println(conversationHistories.size());
        assertFalse(checkIfContains(conversationHistories));
    }

    private boolean checkIfContains(List<ConversationHistory> conversationHistories){
        for(ConversationHistory conversationHistory1 : conversationHistories){
            if(conversationHistory1.getChat().getChatId() == chat.getChatId()){
                return true;
            }
        }
        return false;
    }


    private void generateChat(){
        String subject = AccountGenerator.generateEmail(true).split("@")[0];
        chat = new Chat(-1, subject, Model.AIML_NL, "2000-02-03", "nederlands");
        chat.setChatId(ChatDatabaseUtil.getInstance().processAddObject(chat));
    }

    private void generateAccount(){
        String password = AccountGenerator.generatePassword(new boolean[]{true,true,true,true});

        String email = AccountGenerator.generateEmail(true);

        String username = email.split("@")[0];

        newAccount = new Account(-1, new Settings(), new Permission(), username, email, password, true);
        newAccount.setId(AccountDatabaseUtil.getInstance().processAddObject(newAccount));
        LoggedInAccountHolder.getInstance().setAccount(newAccount);
    }

    private void generateHistory(){
        conversationHistory = new ConversationHistory(chat, new ArrayList<>(), new Date(System.currentTimeMillis()), -1);
        conversationHistory.setId(HistoryDatabaseUtil.getInstance().processAddObject(conversationHistory));
    }

}