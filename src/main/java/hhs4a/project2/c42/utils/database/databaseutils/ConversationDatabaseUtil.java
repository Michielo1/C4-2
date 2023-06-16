package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.chatscreen.Answer;
import hhs4a.project2.c42.utils.chatscreen.Chat;
import hhs4a.project2.c42.utils.chatscreen.Conversation;
import hhs4a.project2.c42.utils.chatscreen.Question;
import hhs4a.project2.c42.utils.database.DatabaseConnection;
import hhs4a.project2.c42.utils.message.BotMessage;
import hhs4a.project2.c42.utils.message.MessageInterface;
import hhs4a.project2.c42.utils.message.UserMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConversationDatabaseUtil extends AbstractDatabaseUtilTemplate<Conversation> {
    // volatile keyword makes sure changes don't get cached but written to main memory so multiple threads can access it without issues
    private static volatile ConversationDatabaseUtil instance;

    private ConversationDatabaseUtil() {
        super();
    }

    public static ConversationDatabaseUtil getInstance() {
        if (instance == null) {
            synchronized (ConversationDatabaseUtil.class) {
                if (instance == null) {
                    instance = new ConversationDatabaseUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Verwijdert een conversatie uit de 'Conversation' tabel in de database met de opgegeven vraag id en antwoord id of chatid.
     */
    @Override
    public boolean processRemoveObject(Conversation conversation) {
        try {
            Connection connectDatabase = super.getConnection();
            String deleteConversationWhereQuestionIdAndAnswerIdQuery = "DELETE FROM Conversation WHERE question_id = ? AND answer_id = ? AND chat_id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(deleteConversationWhereQuestionIdAndAnswerIdQuery);
            setConversationInPreparedStatement(preparedStatement, conversation);
            preparedStatement.executeUpdate();
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setConversationInPreparedStatement(PreparedStatement preparedStatement, Conversation conversation) throws SQLException {
        preparedStatement.setInt(1, conversation.getQuestion().getId());
        preparedStatement.setInt(2, conversation.getAnswer().getId());
        preparedStatement.setInt(3, conversation.getChat().getChatId());
    }

    @Override
    public boolean processUpdateObject(Conversation conversation) {
        try {
            Connection connectDatabase = super.getConnection();
            String insertConversationQuery = "INSERT INTO Conversation (question_id, answer_id, chat_id, id) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertConversationQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            setConversationInPreparedStatement(preparedStatement, conversation);
            preparedStatement.setInt(4, conversation.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Voegt een nieuw gesprek toe aan de 'Conversation' tabel in de database met de opgegeven vraag, antwoord en chat id.
     * question De vraag die bij het gesprek hoort van de gebruiker
     * answer Het antwoord dat bij het gesprek hoort van de chatbot
     * id Het id van de chat waartoe het gesprek behoort
     */
    @Override
    public int processAddObject(Conversation conversation) {
        int id;
        try {
            Connection connectDatabase = super.getConnection();
            String insertConversationQuery = "INSERT INTO Conversation (question_id, answer_id, chat_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertConversationQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            setConversationInPreparedStatement(preparedStatement, conversation);
            preparedStatement.executeUpdate();
            id = super.getIdFromResultSet(preparedStatement.getGeneratedKeys());
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    Conversation proccesGetObject(int id) {
        return null;
    }

    /**
     * Haalt de conversaties op die zijn verbonden aan een chat op basis van een account.
     * Met behulp van een join worden alle relevante tabellen in één query opgehaald.
     * De opgehaalde vragen en antwoorden worden vervolgens opgeslagen in een List<String[]> en geretourneerd.
     * accountId De id van het account waarvoor de conversaties moeten worden opgehaald
     * chatId De id van de chat waarvoor de conversaties moeten worden opgehaald
     * @return Een lijst van strings waarbij elke string een vraag of antwoord uit de chat representeert
     */
    @Override
    public List<MessageInterface> getConversations(Account account, Chat chat) {
        List<MessageInterface> conversations = new ArrayList<>();
        try {
            Connection connectDatabase = super.getConnection();
            String selectConversationWhereAccountIdAndChatIdQuery = "SELECT q.input, a.output, co.question_id, co.answer_id FROM Chat AS c INNER JOIN Conversation AS co ON c.id = co.chat_id INNER JOIN Question AS q ON co.question_id = q.id INNER JOIN Answer AS a ON co.answer_id = a.id LEFT JOIN History AS h ON h.chat_id = c.id LEFT JOIN Account AS ac ON h.account_id = ac.id WHERE ac.id = ? AND c.id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectConversationWhereAccountIdAndChatIdQuery);
            preparedStatement.setInt(1, account.getId());
            preparedStatement.setInt(2, chat.getChatId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String questionText = resultSet.getString("input");
                String answerText = resultSet.getString("output");
                int questionId = resultSet.getInt("question_id");
                int answerId = resultSet.getInt("answer_id");
                Question question = new Question(questionText, questionId);
                Answer answer = new Answer(answerText, answerId);
                conversations.add(new UserMessage(question));
                conversations.add(new BotMessage(answer));
            }
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conversations;
    }

    /**
     * Haalt de vraag- en antwoord id's op voor gesprekken die zijn gekoppeld aan het opgegeven chat id uit de 'Conversation' tabel in de database.
     * chatId De id van de chat waarvoor de conversaties moeten worden opgehaald
     * @return Een lijst van ints waarbij elke int een vraag id of antwoord id de chat representeert
     */
    @Override
    public List<Conversation> getConversations(Chat chat) {
        List<Conversation> conversations = new ArrayList<>();
        try {
            Connection connectDatabase = DatabaseConnection.getInstance().getDBConnection();
            String selectConversationWhereAccountIdAndChatIdQuery = "SELECT co.id AS conversation_id, co.question_id, co.answer_id FROM Chat AS c INNER JOIN Conversation AS co ON c.id = co.chat_id INNER JOIN Question AS q ON co.question_id = q.id INNER JOIN Answer AS a ON co.answer_id = a.id WHERE c.id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectConversationWhereAccountIdAndChatIdQuery);
            preparedStatement.setInt(1, chat.getChatId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int conversationId = resultSet.getInt("conversation_id");
                Question question = new Question("", resultSet.getInt("question_id"));
                Answer answer = new Answer("", resultSet.getInt("answer_id"));
                Conversation conversation = new Conversation(question, answer, chat, conversationId);
                conversations.add(conversation);
            }
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(conversations.size());
        return conversations;
    }
}