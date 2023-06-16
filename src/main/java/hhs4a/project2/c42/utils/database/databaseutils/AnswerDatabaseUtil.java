package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.utils.chatscreen.Answer;
import hhs4a.project2.c42.utils.chatscreen.Question;

import java.sql.*;

public class AnswerDatabaseUtil extends AbstractDatabaseUtilTemplate<Answer> {
    // volatile keyword makes sure changes don't get cached but written to main memory so multiple threads can access it without issues
    private static volatile AnswerDatabaseUtil instance;

    private AnswerDatabaseUtil() {
        super();
    }

    // Double checked locking
    public static AnswerDatabaseUtil getInstance() {
        if (instance == null) {
            synchronized (AnswerDatabaseUtil.class) {
                if (instance == null) {
                    instance = new AnswerDatabaseUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Verwijdert het antwoord uit de 'Answer' tabel van de database met de opgegeven answer id.
     * id De id van het antwoord dat moet worden verwijderd
     * @return True als het antwoord succesvol is verwijderd, anders false
     */
    @Override
    public boolean processRemoveObject(Answer answer) {
        try {
            Connection connectDatabase = super.getConnection();
            String deleteAnswerWhereIdQuery = "DELETE FROM Answer WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(deleteAnswerWhereIdQuery);
            preparedStatement.setInt(1, answer.getId());
            preparedStatement.executeUpdate();
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Wijzigt de uitvoer van een bot in de 'Answer' tabel van de database met de nieuwe uitvoer, voor het antwoord die overeenkomt met de opgegeven answer id.
     * answer De nieuwe uitvoer dat door de bot is gegenereerd
     * id De id van het antwoord waarvan de uitvoer moet worden gewijzigd
     * @return True als er minstens één rij is geüpdatet, anders false
     */
    @Override
    public boolean processUpdateObject(Answer answer) {
        try {
            Connection connectDatabase = super.getConnection();
            String updateAnswerOutputWhereIdQuery = "UPDATE Answer SET output = ? WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(updateAnswerOutputWhereIdQuery);
            preparedStatement.setString(1, answer.getAnswer());
            preparedStatement.setInt(2, answer.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Voegt een nieuw antwoord toe aan de database en retourneert de id van de laatst toegevoegde antwoord.
     * answer Het antwoord dat moet worden toegevoegd aan de database
     * @return De id van de laatst toegevoegde antwoord
     */
    @Override
    public int processAddObject(Answer answer) {
        int answerId;
        try {
            Connection connectDatabase = super.getConnection();
            String insertAnswerQuery = "INSERT INTO Answer (output) VALUES (?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertAnswerQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, answer.getAnswer());
            preparedStatement.executeUpdate();
            answerId = super.getIdFromResultSet(preparedStatement.getGeneratedKeys());
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return answerId;
    }

    @Override
    public Answer proccesGetObject(int id) {
        Answer answer = null;

        try{
            Connection connectDatabase = super.getConnection();
            String selectAnswerWhereIdQuery = "SELECT * FROM Answer WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectAnswerWhereIdQuery);
            preparedStatement.setInt(1, id);
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                answer = new Answer(result.getString("output"), result.getInt("id"));
            }
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return answer;
    }

    /**
     * Haalt het antwoord-ID op basis van de vraag-ID uit de 'Conversation' tabel in de database.
     * questionId De id van de vraag waarvoor de id van het antwoord moet worden opgehaald
     * @return Retourneert de id van het antwoord als deze bestaat, anders -1
     */
    @Override
    public int getAnswerId(Question question) {
        try {
            Connection connectDatabase = super.getConnection();
            String selectConversationAnswerIdWhereQuestionIdQuery = "SELECT answer_id FROM Conversation WHERE question_id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectConversationAnswerIdWhereQuestionIdQuery);
            preparedStatement.setInt(1, question.getId());
            ResultSet result = preparedStatement.executeQuery();
            int answerId = result.next() ? result.getInt("answer_id") : -1;
            // Close the database connection
            connectDatabase.close();
            return answerId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}