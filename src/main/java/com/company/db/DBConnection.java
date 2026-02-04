package com.company.db;

import com.company.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static DBConnection instance;

    private final String url;
    private final String user;
    private final String password;

    private DBConnection() {
        this.url = AppConfig.get("db.url");
        this.user = AppConfig.get("db.user");
        this.password = AppConfig.get("db.password");
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
