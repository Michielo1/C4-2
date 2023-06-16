package hhs4a.project2.c42.utils.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {
    //If you fail this test, you messed something up with the database path
    @Test
    void testGetDBConnection() {
        // Arrange
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        // Act
        Connection connection = databaseConnection.getDBConnection();

        // Assert
        assertNotNull(connection);
    }

}