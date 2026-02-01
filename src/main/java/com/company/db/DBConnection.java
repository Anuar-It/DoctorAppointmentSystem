package main.java.com.company.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/clinic",
                    "postgres",
                    "0000"
            );
        }
        return connection;
    }
}
