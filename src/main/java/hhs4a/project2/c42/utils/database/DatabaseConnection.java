package hhs4a.project2.c42.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection databaseLink;
    // volatile keyword makes sure changes don't get cached but written to main memory so multiple threads can access it without issues
    private static volatile DatabaseConnection instance;

    private DatabaseConnection() {
    }

    //TODO implement double checked locking with synchronized block in every singleton class?
    public static DatabaseConnection getInstance() {
        // Double checked locking
        // https://www.geeksforgeeks.org/java-singleton-design-pattern-practices-examples/
        if(instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }


    //Synchronized method to control simultaneous access
    //When writing to an sqlite database it locks the database for a bit. This prevents multiple threads from writing or reading to the database at the same time.
    public synchronized Connection getDBConnection() {

        //String url = "jdbc:sqlite:file:src/main/resources/database/c4-2.db";
        String url = "jdbc:sqlite::resource:c4-2.db";

        try {
            databaseLink = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            System.exit(4);
            System.out.println(e.getMessage());
        }
        return databaseLink;
    }
}