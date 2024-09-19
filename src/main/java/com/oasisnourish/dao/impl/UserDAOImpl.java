package com.oasisnourish.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oasisnourish.dao.UserDAO;
import com.oasisnourish.database.DatabaseConnection;
import com.oasisnourish.enums.Role;
import com.oasisnourish.models.User;

public class UserDAOImpl implements UserDAO {

  private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

  // SQL queries
  private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
  private static final String GET_ALL_USERS = "SELECT * FROM users";
  private static final String INSERT_USER = "INSERT INTO users (name, email, email_verified, role, password) VALUES (?, ?, ?, ?, ?)";
  private static final String UPDATE_USER = "UPDATE users SET name = ?, email = ?, email_verified = ?, role = ?, password = ? WHERE id = ?";
  private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
  private static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

  // User table column names
  private static final String COLUMN_ID = "id";
  private static final String COLUMN_NAME = "name";
  private static final String COLUMN_EMAIL = "email";
  private static final String COLUMN_EMAIL_VERIFIED = "email_verified";
  private static final String COLUMN_ROLE = "role";
  private static final String COLUMN_PASSWORD = "password";

  @Override
  public Optional<User> get(int id) {
    try (Connection connection = DatabaseConnection.getJdbcConnection();
        PreparedStatement ps = connection.prepareStatement(GET_USER_BY_ID)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapRowToUser(rs));
        }
      }
    } catch (SQLException e) {
      logger.error("Error getting user with ID: {}", id, e);
    }
    return Optional.empty();
  }

  @Override
  public List<User> getAll() {
    List<User> users = new ArrayList<>();
    try (Connection connection = DatabaseConnection.getJdbcConnection();
        PreparedStatement ps = connection.prepareStatement(GET_ALL_USERS);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        users.add(mapRowToUser(rs));
      }
    } catch (SQLException e) {
      logger.error("Error getting all users", e);
    }
    return users;
  }

  @Override
  public Optional<User> save(User user) {
    try (Connection connection = DatabaseConnection.getJdbcConnection();
        PreparedStatement ps = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
      mapUserToRow(ps, user);
      int rowsAffected = ps.executeUpdate();

      if (rowsAffected == 0) {
        logger.warn("No user was inserted.");
        return Optional.empty();
      }

      // Retrieve the generated ID
      try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          user.setId(generatedKeys.getInt(1)); // Set the generated ID
        }
      }
      return Optional.of(user);
    } catch (SQLException e) {
      logger.error("Error saving user: {}", user, e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<User> update(User user) {
    try (Connection connection = DatabaseConnection.getJdbcConnection();
        PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {

      mapUserToRow(ps, user);
      ps.setInt(6, user.getId());

      int rowsAffected = ps.executeUpdate();

      if (rowsAffected == 0) {
        logger.warn("User with ID {} not found for update.", user.getId());
        return Optional.empty();
      }
      return Optional.of(user);
    } catch (SQLException e) {
      logger.error("Error updating user: {}", user, e);
    }
    return Optional.empty();
  }

  @Override
  public boolean delete(int id) {
    try (Connection connection = DatabaseConnection.getJdbcConnection();
        PreparedStatement ps = connection.prepareStatement(DELETE_USER)) {
      ps.setInt(1, id);
      int rowsAffected = ps.executeUpdate();

      if (rowsAffected == 0) {
        logger.warn("No user with ID {} was found to delete.", id);
        return false;
      }

      return true;
    } catch (SQLException e) {
      logger.error("Error deleting user with ID: {}", id, e);
    }
    return false;
  }

  @Override
  public Optional<User> getByEmail(String email) {
    try (Connection connection = DatabaseConnection.getJdbcConnection();
        PreparedStatement ps = connection.prepareStatement(GET_USER_BY_EMAIL)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapRowToUser(rs));
        }
      }
    } catch (SQLException e) {
      logger.error("Error getting user with email: {}", email, e);
    }
    return Optional.empty();
  }

  private User mapRowToUser(ResultSet rs) throws SQLException {
    User user = new User();
    user.setId(rs.getInt(COLUMN_ID));
    user.setName(rs.getString(COLUMN_NAME));
    user.setEmail(rs.getString(COLUMN_EMAIL));
    user.setRole(Role.valueOf(rs.getString(COLUMN_ROLE)));

    Timestamp emailVerifiedTS = rs.getTimestamp(COLUMN_EMAIL_VERIFIED);
    user.setEmailVerified(emailVerifiedTS == null ? null : emailVerifiedTS.toLocalDateTime());

    user.setPassword(rs.getString(COLUMN_PASSWORD));
    return user;
  }

  private void mapUserToRow(PreparedStatement ps, User user) throws SQLException {
    ps.setString(1, user.getName());
    ps.setString(2, user.getEmail());
    ps.setTimestamp(3, user.getEmailVerified() == null ? null : Timestamp.valueOf(user.getEmailVerified()));
    ps.setString(4, user.getRole().name());
    ps.setString(5, user.getPassword());
  }

}
