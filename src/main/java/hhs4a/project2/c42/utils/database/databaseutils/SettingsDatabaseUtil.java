package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.utils.account.Settings;
import hhs4a.project2.c42.utils.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SettingsDatabaseUtil extends AbstractDatabaseUtilTemplate<Settings>{
    // volatile keyword makes sure changes don't get cached but written to main memory so multiple threads can access it without issues
    private static volatile SettingsDatabaseUtil instance;

    private SettingsDatabaseUtil() {
        super();
    }

    public static SettingsDatabaseUtil getInstance() {
        if (instance == null) {
            synchronized (SettingsDatabaseUtil.class) {
                if (instance == null) {
                    instance = new SettingsDatabaseUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Verwijdert een instelling uit de 'Setting' tabel van de database met de opgegeven setting id.
     * id Het id van de instelling die moet worden verwijderd
     * @return True als de instelling succesvol is verwijderd, anders false
     */
    @Override
    public int processAddObject(Settings settings) {
        int lastInsertedSettingId;
        try {
            Connection connectDatabase = DatabaseConnection.getInstance().getDBConnection();
            String insertSettingQuery = "INSERT INTO Setting (theme_type, profile_picture_path, driver_option) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertSettingQuery, Statement.RETURN_GENERATED_KEYS);
            setSettingsInPreparedStatement(preparedStatement, settings);
            preparedStatement.executeUpdate();
            // Get last inserted id
            lastInsertedSettingId = super.getIdFromResultSet(preparedStatement.getGeneratedKeys());
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lastInsertedSettingId;
    }

    @Override
    Settings proccesGetObject(int id) {
        return null;
    }

    @Override
    public boolean processUpdateObject(Settings settings) {
        try{
            Connection connectDatabase = super.getConnection();
            String updateSettingQuery = "UPDATE Setting SET theme_type = ?, profile_picture_path = ?, driver_option = ? WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(updateSettingQuery);
            setSettingsInPreparedStatement(preparedStatement, settings);
            preparedStatement.setInt(4, settings.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setSettingsInPreparedStatement(PreparedStatement preparedStatement, Settings settings) throws SQLException {
        preparedStatement.setObject(1, settings.getTheme());
        preparedStatement.setString(2, settings.getProfilePicturePath());
        preparedStatement.setString(3, settings.getDiverOption().toString());
    }

    @Override
    boolean processRemoveObject(Settings setting) {
        try {
            Connection connectDatabase = super.getConnection();
            String deleteSettingWhereIdQuery = "DELETE FROM Setting WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(deleteSettingWhereIdQuery);
            preparedStatement.setInt(1, setting.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
