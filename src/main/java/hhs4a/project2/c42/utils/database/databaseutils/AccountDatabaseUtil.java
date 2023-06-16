package hhs4a.project2.c42.utils.database.databaseutils;

import hhs4a.project2.c42.enums.ThemeType;
import hhs4a.project2.c42.enums.DriverEnum;
import hhs4a.project2.c42.utils.account.Account;
import hhs4a.project2.c42.utils.account.Permission;
import hhs4a.project2.c42.utils.account.Settings;
import hhs4a.project2.c42.utils.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountDatabaseUtil extends AbstractDatabaseUtilTemplate<Account> {
    // volatile keyword makes sure changes don't get cached but written to main memory so multiple threads can access it without issues
    private static volatile AccountDatabaseUtil instance;

    // Double checked locking
    public static AccountDatabaseUtil getInstance() {
        if(instance == null) {
            synchronized (AccountDatabaseUtil.class) {
                if (instance == null) {
                    instance = new AccountDatabaseUtil();
                }
            }
        }
        return instance;
    }

    private AccountDatabaseUtil() {
        super();
    }

    @Override
    public boolean processRemoveObject(Account account) {
        try {
            Connection connectDatabase = super.getConnection();
            String deleteAccountQuery = "DELETE FROM Account WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(deleteAccountQuery);
            preparedStatement.setInt(1, account.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();

            ChatDatabaseUtil.getInstance().getChatsFromAccount(account).forEach(chat -> ChatDatabaseUtil.getInstance().processRemoveObject(chat));
            SettingsDatabaseUtil.getInstance().processRemoveObject(account.getSettings());
            PermissionDatabaseUtil.getInstance().processRemoveObject(account.getPermissions());
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean processUpdateObject(Account account) {
        try {
            Connection connectDatabase = super.getConnection();
            String updateAccountQuery = "UPDATE Account SET username = ?, email = ?, password = ?, status = ? WHERE id = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(updateAccountQuery);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getEmail());
            preparedStatement.setString(3, account.getPassword());
            preparedStatement.setInt(4, account.getStatus() ? 1 : 0); // 1: Geblokkeerd. 1: Niet geblokkeerd.
            preparedStatement.setInt(5, account.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            // Close the database connection
            connectDatabase.close();
            return rowsAffected >0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int processAddObject(Account account) {
        int lastInsertedAccountId;
        try {
            Connection connectDatabase = DatabaseConnection.getInstance().getDBConnection();
            String insertAccountQuery = "INSERT INTO Account (setting_id, permission_id, username, email, password, status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(insertAccountQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, account.getSettings().getId());
            preparedStatement.setInt(2, account.getPermissions().getId());
            preparedStatement.setString(3, account.getUsername());
            preparedStatement.setString(4, account.getEmail());
            preparedStatement.setString(5, account.getPassword());
            preparedStatement.setInt(6, account.getStatus() ? 1 : 0); // 1: Geblokkeerd. 1: Niet geblokkeerd.
            preparedStatement.executeUpdate();
            // Get last inserted id
            lastInsertedAccountId = super.getIdFromResultSet(preparedStatement.getGeneratedKeys());
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lastInsertedAccountId;
    }

    @Override
    public Account proccesGetObject(int id) {
        return null;
    }

    /**
     * Haalt een Account-object op op basis van de opgegeven gebruikersnaam en e-mailadres uit de 'Account' tabel van de database.
     *
     * @param username De gebruikersnaam van het account
     * @param email Het e-mailadres van het account
     * @return Het Account-object dat overeenkomt met de opgegeven gebruikersnaam en e-mailadres, of null als er geen overeenkomst is gevonden
     */
    @Override
    public Account getAccount(String username, String email) {
        Account account = null;
        try {
            Connection connectDatabase = super.getConnection();
            String selectAllAccountWhereUsernameOrEmailQuery = "SELECT a.*, s.*, p.* FROM Account AS a JOIN Setting AS s ON a.setting_id = s.id JOIN Permission AS p ON a.permission_id = p.id WHERE (a.username = ? OR a.email = ?)";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectAllAccountWhereUsernameOrEmailQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                account = turnResultSetToAccount(resultSet);
            }
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    /**
     * Converteert een ResultSet naar een Account-object.
     *
     * @param resultSet het ResultSet dat moet worden geconverteerd
     * @return het geconverteerde Account-object
     */
    private Account turnResultSetToAccount(ResultSet resultSet) throws SQLException {
        Account account;
        try {
            int accountId = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            boolean status = resultSet.getInt("status") == 1;
            int settingsId = resultSet.getInt("setting_id");
            String theme_type = resultSet.getString("theme_type");
            DriverEnum driver_option = DriverEnum.valueOf(resultSet.getString("driver_option").toUpperCase().trim());
            String profile_picture_path = resultSet.getString("profile_picture_path");
            Settings settings = new Settings(settingsId, ThemeType.valueOf(theme_type), profile_picture_path, driver_option);
            int permissionId = resultSet.getInt("permission_id");
            boolean canChatWithOnlineChatbot = resultSet.getInt("chat_with_online_chatbot") == 1;
            boolean canChatWithOfflineChatbot = resultSet.getInt("chat_with_offline_chatbot") == 1;
            boolean isAdmin = resultSet.getInt("isAdmin") == 1;
            Permission permissions = new Permission(permissionId, canChatWithOnlineChatbot, canChatWithOfflineChatbot, isAdmin);
            account = new Account(accountId, settings, permissions, username, email, password, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    /**
     * Haalt alle account op uit de 'Account' tabel van de database.
     *
     * @return Een lijst met opgeslagen accounts in de database
     */
    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        try {
            Connection connectDatabase = super.getConnection();
            String selectAllAccountQuery = "SELECT a.*, s.*, p.* FROM Account AS a JOIN Setting AS s ON a.setting_id = s.id JOIN Permission AS p ON a.permission_id = p.id";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectAllAccountQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(turnResultSetToAccount(resultSet));
            }
            // Close the database connection
            connectDatabase.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    /**
     * Controleert of een e-mailadres al bestaat in de 'Account' tabel van de database.
     *
     * @param email Het e-mailadres dat gecontroleerd moet worden
     * @return True als het e-mailadres al bestaat, anders false
     */
    @Override
    public boolean checkIfEmailExists(String email) {
        try {
            Connection connectDatabase = super.getConnection();
            String selectEmailWhereQuery = "SELECT COUNT(*) FROM Account WHERE email = ?";
            PreparedStatement preparedStatement = connectDatabase.prepareStatement(selectEmailWhereQuery);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            int rowsAffected = resultSet.next() ? resultSet.getInt(1) : 0;
            // Close the database connection
            connectDatabase.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
