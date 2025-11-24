package lms.connector.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnector {
    
    //CHANGE THESE TO MATCH YOUR LOCAL POSTGRES SETUP
    private static final String URL = "jdbc:postgresql://pg-61bbdb-iatop-d7dd.h.aivencloud.com:23456/defaultdb?sslmode=require";
    private static final String USER = "avnadmin"; 
    private static final String PASS = "AVNS_2tEro7RY7SCjHIdNxz-"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // Initialize tables automatically on startup
    public static void initializeDatabase() {
        String createStudentTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id VARCHAR(50) PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "fine_amount DECIMAL(10, 2) DEFAULT 0.00)";

        String createBookTable = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY, " +
                "title VARCHAR(200) NOT NULL, " +
                "author VARCHAR(100), " +
                "is_issued BOOLEAN DEFAULT FALSE, " +
                "issued_to VARCHAR(50) REFERENCES students(id), " +
                "issue_date TIMESTAMP, " +
                "return_date DATE)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createStudentTable);
            stmt.execute(createBookTable);
            System.out.println("Database tables checked/created successfully.");
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("FATAL: Could not connect to database. Check credentials in lms/connector/db/SQL.java");
        }
    }
}