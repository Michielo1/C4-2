package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.utils.account.Permission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class PermissionDatabaseUtil extends AbstractDatabaseUtilTemplate<Permission> {
    private static volatile PermissionDatabaseUtil instance;

    private PermissionDatabaseUtil() {
        super();
    }

    public static PermissionDatabaseUtil getInstance() {
        if (instance == null) {
            synchronized (PermissionDatabaseUtil.class) {
                if (instance == null) {
                    instance = new PermissionDatabaseUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Voegt een nieuwe rechten toe aan de 'Permission' tabel in de database met de opgegeven rechten.
     *
     * @param permission Het Permission-object dat moet worden toegevoegd
     * @return Het ID van de laatst ingevoegde rechten
     */
    @Override
    public int processAddObject(Permission permission) {
        int lastInsertedPermissionId;
        try {
            Connection connectDatabase = super.getConnection();
            String insertPermissionQuery = "INSERT INTO Permission (chat_with_online_chatbot, chat_with_offline_chatbot, isAdmin) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertPermissionQuery, Statement.RETURN_GENERATED_KEYS);
            setPermissionInPreparedStatement(preparedStatement, permission);
            preparedStatement.executeUpdate();
            // Get last inserted id
            lastInsertedPermissionId = super.getIdFromResultSet(preparedStatement.getGeneratedKeys());
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lastInsertedPermissionId;
    }

    @Override
    public Permission proccesGetObject(int id) {
        return null;
    }

    @Override
    public boolean processUpdateObject(Permission permission) {
        try {
            Connection connectDatabase = super.getConnection();
            String updatePermissionQuery = "UPDATE Permission SET chat_with_online_chatbot = ?, chat_with_offline_chatbot = ?, isAdmin = ? WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(updatePermissionQuery);
            setPermissionInPreparedStatement(preparedStatement, permission);
            preparedStatement.setInt(4, permission.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean processRemoveObject(Permission permission) {
        try {
            Connection connectDatabase = super.getConnection();
            String deletePermissionWhereIdQuery = "DELETE FROM Permission WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(deletePermissionWhereIdQuery);
            preparedStatement.setInt(1, permission.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPermissionInPreparedStatement(PreparedStatement preparedStatement, Permission permission) throws SQLException {
        preparedStatement.setInt(1, permission.getCanChatWithOnlineChatbot() ? 1 : 0);
        preparedStatement.setInt(2, permission.getCanChatWithOfflineChatbot() ? 1 : 0);
        preparedStatement.setInt(3, permission.isAdmin() ? 1 : 0);
    }
}