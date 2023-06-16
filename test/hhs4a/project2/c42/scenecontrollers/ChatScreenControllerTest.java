package hhs4a.project2.c42.scenecontrollers;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.AccountGenerator;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.database.databaseutils.AccountDatabaseUtil;
import hhs4a.project2.c42.utils.database.databaseutils.ChatDatabaseUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatScreenControllerTest {
    @Test
    void changeChatSubject(){
        Account account = AccountGenerator.generateAccount();
        assertNotNull(account);

        String subject = AccountGenerator.generateEmail(true).split("@")[0];
        String subjectChanged = AccountGenerator.generateEmail(false).split("@")[0];

        Chat chat = new Chat(-1, subject, Model.AIML_NL, "2000-02-03", "nederlands");
        chat.setChatId(ChatDatabaseUtil.getInstance().processAddObject(chat));
        assertNotNull(chat);

        chat.setSubject(subjectChanged);
        ChatDatabaseUtil.getInstance().processUpdateObject(chat);
        Chat chat1 = ChatDatabaseUtil.getInstance().proccesGetObject(chat.getChatId());
        assertEquals(chat1.getSubject(), subjectChanged);

        ChatDatabaseUtil.getInstance().processRemoveObject(chat);
        AccountDatabaseUtil.getInstance().processRemoveObject(account);

        assertNull(ChatDatabaseUtil.getInstance().proccesGetObject(chat.getChatId()));
        assertNull(AccountDatabaseUtil.getInstance().getAccount(account.getUsername(), account.getPassword()));

    }

}