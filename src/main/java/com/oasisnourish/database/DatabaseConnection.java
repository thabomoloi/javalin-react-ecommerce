package com.oasisnourish.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
  private static final HikariDataSource dataSource;

  static {
    HikariConfig config = new HikariConfig();
    // Load DB URL and credentials from environment variables
    String dbUrl = System.getenv("DB_URL");
    String dbUsername = System.getenv("DB_USERNAME");
    String dbPassword = System.getenv("DB_PASSWORD");

    if (dbUrl == null || dbUsername == null || dbPassword == null) {
      throw new IllegalStateException("Database environment variables are not set.");
    }

    config.setJdbcUrl(dbUrl);
    config.setUsername(dbUsername);
    config.setPassword(dbPassword);

    dataSource = new HikariDataSource(config);
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
