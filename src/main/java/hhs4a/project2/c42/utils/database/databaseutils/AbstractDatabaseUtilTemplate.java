package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.enums.SortOption;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.Conversation;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;
import hhs4a.project2.c42.utils.chatscreen.Question;
import hhs4a.project2.c42.utils.database.DatabaseConnection;
import hhs4a.project2.c42.utils.message.MessageInterface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDatabaseUtilTemplate<T> {

    protected AbstractDatabaseUtilTemplate() {}

    public int add(T object) {
        return processAddObject(object);
    }

    public T get(int id) {
        return proccesGetObject(id);
    }

    public boolean update(T object) {
        return processUpdateObject(object);
    }

    public boolean remove(T object) {
        return processRemoveObject(object);
    }

    abstract int processAddObject(T object);

    abstract T proccesGetObject(int id);

    abstract boolean processUpdateObject(T object);

    abstract boolean processRemoveObject(T object);

    protected int getIdFromResultSet(ResultSet generatedKeys) {
        int id = -1;
        // Retrieve the generated keys
        try {
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
                System.out.println("Generated ID: " + id);
            } else {
                System.out.println("No generated keys");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    protected Connection getConnection() {
        return DatabaseConnection.getInstance().getDBConnection();
    }

    public Account getAccount(String username, String email){return null;}

    public List<Account> getAccounts() {return null;}

    public boolean checkIfEmailExists(String email){return false;}

    public int getAnswerId(Question question){return -1;}

    public List<Chat> getChatsFromAccount(Account account){return null;}

    public List<MessageInterface> getConversations(Account account, Chat chat) {return null;}

    public List<Conversation> getConversations(Chat chat) {return null;}

    public List<ConversationHistory> getHistory(Account account, SortOption sortOption) {return null;}

    public boolean clearHistory(int id) {return false;}
}