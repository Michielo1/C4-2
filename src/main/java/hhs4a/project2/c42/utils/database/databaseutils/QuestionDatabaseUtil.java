package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.utils.chatscreen.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class QuestionDatabaseUtil extends AbstractDatabaseUtilTemplate<Question> {
    // volatile keyword makes sure changes don't get cached but written to main memory so multiple threads can access it without issues
    private static volatile QuestionDatabaseUtil instance;

    private QuestionDatabaseUtil() {
        super();
    }

    // Double checked locking
    public static QuestionDatabaseUtil getInstance() {
        if(instance == null) {
            synchronized (QuestionDatabaseUtil.class) {
                if (instance == null) {
                    instance = new QuestionDatabaseUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Verwijdert de vraag uit de 'Question' tabel van de database met de opgegeven question id.
     * id De id van de vraag dat moet worden verwijderd
     * @return True als de vraag succesvol is verwijderd, anders false
     */
    @Override
    public boolean processRemoveObject(Question question) {
        try {
            Connection connectDatabase = super.getConnection();
            String deleteQuestionWhereIdQuery = "DELETE FROM Question WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(deleteQuestionWhereIdQuery);
            preparedStatement.setInt(1, question.getId());
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
     * Wijzigt de invoer van een gebruiker in de 'Question' tabel van de database met de nieuwe invoer, voor de vraag die overeenkomt met de opgegeven question id.
     *
     * @param question De nieuwe invoer dat door gebruiker is ingevoerd
     * id De id van de vraag waarvan de invoer moet worden gewijzigd
     * @return True als er minstens één rij is geüpdatet, anders false
     */

    @Override
    public boolean processUpdateObject(Question question) {
        try {
            Connection connectDatabase = super.getConnection();
            String updateQuestionInputWhereIdQuery = "UPDATE Question SET input = ? WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(updateQuestionInputWhereIdQuery);
            preparedStatement.setString(1, question.getQuestion());
            preparedStatement.setInt(2, question.getId());
            int affectedRows = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int processAddObject(Question question) {
        int questionId;
        try {
            Connection connectDatabase = super.getConnection();
            String insertQuestionQuery = "INSERT INTO Question (input) VALUES (?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertQuestionQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, question.getQuestion());
            preparedStatement.executeUpdate();
            questionId = super.getIdFromResultSet(preparedStatement.getGeneratedKeys());
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return questionId;
    }

    @Override
    Question proccesGetObject(int id) {
        return null;
    }

}
