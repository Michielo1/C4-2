package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.chatscreen.Chat;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatDatabaseUtil extends AbstractDatabaseUtilTemplate<Chat> {
    // volatile keyword makes sure changes don't get cached but written to main memory so multiple threads can access it without issues
    private static volatile ChatDatabaseUtil instance;

    private ChatDatabaseUtil() {
        super();
    }

    public static ChatDatabaseUtil getInstance() {
        if (instance == null) {
            synchronized (ChatDatabaseUtil.class) {
                if (instance == null) {
                    instance = new ChatDatabaseUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Verwijdert de chat uit de 'Chat' tabel van de database met de opgegeven chat id.
     * id De id van de chat die moet worden verwijderd
     * @return True als de chat succesvol is verwijderd, anders false
     */
    @Override
    public boolean processRemoveObject(Chat chat) {
        try {
            Connection connectDatabase = super.getConnection();
            String deleteChatQuery = "DELETE FROM Chat WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(deleteChatQuery);
            preparedStatement.setInt(1, chat.getChatId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean processUpdateObject(Chat chat) {
        System.out.println(chat.getChatId());
        try {
            Connection connectDatabase = super.getConnection();
            String insertChatQuery = "UPDATE Chat SET subject = ?, lastActive = ?, modelname = ?, language = ? WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertChatQuery);
            setChatInPreparedStatement(preparedStatement, chat);
            preparedStatement.setInt(5, chat.getChatId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Removing code duplication
    private void setChatInPreparedStatement(PreparedStatement preparedStatement, Chat chat) {
        try {
            preparedStatement.setString(1, chat.getSubject());
            preparedStatement.setString(2, chat.getLastActive().isBlank() ? LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : chat.getLastActive());
            String modelString = (chat.getModel() != null) ? chat.getModel().toString() : Model.AIML_NL.toString();
            preparedStatement.setString(3, modelString);
            preparedStatement.setString(4, chat.getLanguage().toLowerCase());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Voegt een nieuwe chat toe aan de 'Chat' tabel in de database met de opgegeven onderwerp, gebruikt model en laatste activiteitsdatum.
     * subject Het onderwerp van de chat
     * modelName De naam van het model
     * lastActive De datum van het laatste gesprek in de chat in het formaat "DD-MM-YYYY"
     */
    @Override
    public int processAddObject(Chat chat) {
        int lastInsertedChatId;
        try {
            Connection connectDatabase = super.getConnection();
            String insertChatQuery = "INSERT INTO Chat (subject, lastActive, modelname, language) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertChatQuery, Statement.RETURN_GENERATED_KEYS);
            setChatInPreparedStatement(preparedStatement, chat);
            preparedStatement.executeUpdate();
            // Get last inserted id
            lastInsertedChatId = super.getIdFromResultSet(preparedStatement.getGeneratedKeys());
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lastInsertedChatId;
    }

    @Override
    public Chat proccesGetObject(int id) {
        Chat chat = null;
        try{
            Connection connectDatabase = super.getConnection();
            String selectChatQuery = "SELECT * FROM Chat WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectChatQuery);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()){
                chat = new Chat(result.getInt("id"), result.getString("subject"), Model.valueOf(result.getString("modelname")), result.getString("lastActive"), result.getString("language"));
            }
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return chat;
    }

    /**
     * Haalt alle chats op uit de 'Chat' tabel van de database.
     * @return Een lijst met alle chats
     */

    @Override
    public List<Chat> getChatsFromAccount(Account account){
        List<Chat> chats = new ArrayList<>();
        try{
            Connection connectDatabase = super.getConnection();
            String selectChatQuery = "SELECT * FROM Chat WHERE id IN (SELECT chat_id FROM History WHERE account_id = ?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectChatQuery);
            preparedStatement.setInt(1, account.getId());
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                int chatId = result.getInt("id");
                String subject = result.getString("subject");
                String lastActive = result.getString("lastActive");
                Model modelName = Model.valueOf(result.getString("modelName"));
                String language = result.getString("language");
                Chat chat = new Chat(chatId, subject, modelName, lastActive, language);
                chats.add(chat);
            }
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return chats;
    }
}