package com.oasisnourish.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.oasisnourish.config.EnvironmentConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.cdimascio.dotenv.Dotenv;
import redis.clients.jedis.JedisPooled;

public class DatabaseConnection {
  private static final Dotenv dotenv = EnvironmentConfig.getDotenv();
  private static final HikariDataSource dataSource;
  private static final JedisPooled jedis;

  static {
    dataSource = setUpJdbcConnection();
    jedis = setUpRedisConnection();
  }

  private static HikariDataSource setUpJdbcConnection() {
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
    return new HikariDataSource(config);
  }

  private static JedisPooled setUpRedisConnection() {
    String host = dotenv.get("REDIS_HOST");
    String port = dotenv.get("REDIS_PORT");

    if (host == null || port == null) {
      throw new IllegalStateException("Redis environment variables are not set.");
    }
    int portNumber = Integer.parseInt(port);

    return new JedisPooled(host, portNumber);
  }

  public static Connection getJdbcConnection() throws SQLException {
    return dataSource.getConnection();
  }

  public static JedisPooled getRedis() {
    return jedis;
  }
}
