package com.oasisnourish.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.oasisnourish.config.EnvironmentConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
  private static final HikariDataSource dataSource;

  static {
    Dotenv dotenv = EnvironmentConfig.getDotenv();
    // Load DB URL and credentials from environment variables
    String url = dotenv.get("DB_URL");
    String username = dotenv.get("DB_USERNAME");
    String password = dotenv.get("DB_PASSWORD");

    if (url == null || username == null || password == null) {
      throw new IllegalStateException("Database environment variables are not set.");
    }

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(url);
    config.setUsername(username);
    config.setPassword(password);
    dataSource = new HikariDataSource(config);

  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
