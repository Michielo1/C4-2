package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.enums.Model;
import hhs4a.project2.c42.enums.SortOption;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.LoggedInAccountHolder;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.ConversationHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryDatabaseUtil extends AbstractDatabaseUtilTemplate<ConversationHistory> {
    // volatile keyword makes sure changes don't get cached but written to main memory so multiple threads can access it without issues
    private static volatile HistoryDatabaseUtil instance;

    private HistoryDatabaseUtil() {
        super();
    }

    public static HistoryDatabaseUtil getInstance() {
        if(instance == null) {
            synchronized (HistoryDatabaseUtil.class) {
                if (instance == null) {
                    instance = new HistoryDatabaseUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Verwijdert een individuele chat uit de 'History' tabel van de database met de opgegeven chat id.
     * id De id van de chat dat uit de geschiedenis verwijderd moet worden
     * @return True als de chat van de gespreksgeschiedenis verwijderd is, anders false
     */
    @Override
    public boolean processRemoveObject(ConversationHistory history) {
        try {
            Connection connectDatabase = super.getConnection();
            String deleteHistoryWhereChatIdQuery = "DELETE FROM History WHERE chat_id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(deleteHistoryWhereChatIdQuery);
            preparedStatement.setInt(1, history.getChat().getChatId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean processUpdateObject(ConversationHistory history) {
        try{
            Connection connectDatabase = super.getConnection();
            String updateHistoryQuery = "UPDATE History SET chat_id = ?, account_id = ? WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(updateHistoryQuery);
            setHistoryInPreparedStatement(preparedStatement, history);
            preparedStatement.setInt(3, history.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void setHistoryInPreparedStatement(PreparedStatement preparedStatement, ConversationHistory history) throws SQLException {
        preparedStatement.setInt(1, history.getChat().getChatId());
        preparedStatement.setInt(2, LoggedInAccountHolder.getInstance().getAccount().getId());
    }

    /**
     * Voegt een bestaande chat toe aan de 'History' tabel in de database en koppelt deze aan het opgegeven account id.
     * Hierdoor kan een account zijn gekoppelde chats later teruglezen in de geschiedenis tabblad.
     * chatId De id van de chat waarmee de gespreksgeschiedenis wordt gekoppeld
     * accountId De id van het account waarmee de gespreksgeschiedenis wordt gekoppeld
     */

    @Override
    public int processAddObject(ConversationHistory history) {
        int lastInsertedHistoryId;
        try {
            Connection connectDatabase = super.getConnection();
            String insertHistoryQuery = "INSERT INTO History (chat_id, account_id) VALUES (?, ?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertHistoryQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            setHistoryInPreparedStatement(preparedStatement, history);
            preparedStatement.executeUpdate();
            // Get last inserted id
            lastInsertedHistoryId = super.getIdFromResultSet(preparedStatement.getGeneratedKeys());
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lastInsertedHistoryId;
    }

    @Override
    public ConversationHistory proccesGetObject(int id) {
        return null;
    }

    private ConversationHistory turnResultSetToHistory(ResultSet resultSet) {
        ConversationHistory history;
        try {
            int chatId = resultSet.getInt("cchat_id"); // Dubbel c zodat je weet het is afkomstig van 'Chat' tabel
            int historyid = resultSet.getInt("history_id");
            String subject = resultSet.getString("subject");
            Model modelName = Model.valueOf(resultSet.getString("modelName"));
            String lastActive = resultSet.getString("lastActive");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(lastActive);
            String language = resultSet.getString("language");
            Chat chat = new Chat(chatId, subject, modelName, lastActive, language);
            history = new ConversationHistory(chat, new ArrayList<>(), date, historyid);
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        return history;
    }


    @Override
    public List<ConversationHistory> getHistory(Account account, SortOption sortOption) {
        List<ConversationHistory> history = new ArrayList<>();
        try {
            Connection connectDatabase = super.getConnection();
            // Bouw de query op met behulp van StringBuilder
            StringBuilder query = buildHistorySortOrder(sortOption);
            // Zet StringBuilder om naar een String
            String selectHistoryWhereAccountIdQuery = query.toString();
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectHistoryWhereAccountIdQuery);
            preparedStatement.setInt(1, account.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                history.add(turnResultSetToHistory(resultSet));
            }
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return history;
    }

    private StringBuilder buildHistorySortOrder(SortOption sortOption) {
        StringBuilder query = new StringBuilder("SELECT h.id AS history_id, c.id AS cchat_id, c.subject, c.lastActive, c.modelName, c.language FROM History AS h JOIN Chat AS c on cchat_id = h.chat_id WHERE h.account_id = ? ");
        switch (sortOption) {
            case SUBJECT_ASC -> query.append("ORDER BY c.subject COLLATE NOCASE ASC");
            case SUBJECT_DESC -> query.append("ORDER BY c.subject COLLATE NOCASE DESC");
            case LAST_ACTIVE_ASC -> query.append("ORDER BY c.lastActive ASC");
            case LAST_ACTIVE_DESC -> query.append("ORDER BY c.lastActive DESC");
            case DEFAULT -> query.append("ORDER BY c.id");
        }
        return query;
    }

    /**
     * Verwijdert alle chats uit de 'History' tabel van de database met de opgegeven account id.
     *
     * @param id De id van het account waarvan de chatgeschiedenis moet worden verwijderd
     * @return True als de gespreksgeschiedenis met succes is verwijderd, anders false
     */
    @Override
    public boolean clearHistory(int id) {
        try {
            Connection connectDatabase = super.getConnection();
            String deleteHistoryWhereAccountIdQuery = "DELETE FROM History WHERE account_id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(deleteHistoryWhereAccountIdQuery);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
