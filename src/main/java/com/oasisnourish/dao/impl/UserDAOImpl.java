package com.oasisnourish.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

  private final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
  private static final String GET_ALL_USERS = "SELECT * FROM users";
  private static final String INSERT_USER = "INSERT INTO users (name, email, email_verified, role, password) VALUES (?, ?, ?, ?, ?)";
  private static final String UPDATE_USER = "UPDATE users SET name = ?, email = ?, email_verified = ?, role = ?, password = ? WHERE id = ?";
  private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
  private static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

  @Override
  public Optional<User> get(int id) {
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_USER_BY_ID)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          User user = mapRowToUser(rs);
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      logger.error("Error getting user with ID: " + id, e);
      throw new RuntimeException(e);
    }
    return Optional.empty();
  }

  @Override
  public List<User> getAll() {
    List<User> users = new ArrayList<>();
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_ALL_USERS);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        User user = mapRowToUser(rs);
        users.add(user);
      }
    } catch (SQLException e) {
      logger.error("Error getting all users", e);
      throw new RuntimeException(e);
    }
    return users;
  }

  @Override
  public User save(User user) {
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(INSERT_USER)) {
      mapUserToRow(ps, user);
      ps.executeUpdate();

      try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          user.setId(generatedKeys.getInt(1)); // Set the generated ID to the user
        }
      }
      return user;
    } catch (SQLException e) {
      logger.error("Error saving user: " + user, e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<User> update(User user) {
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {

      // Map the user data to the prepared statement
      mapUserToRow(ps, user);
      ps.setInt(6, user.getId()); // Set the ID for the WHERE clause

      int rowsAffected = ps.executeUpdate();

      if (rowsAffected == 0) {
        logger.warn("User with ID " + user.getId() + " not found for update.");
        return Optional.empty(); // Return an empty Optional if the user ID is not found
      }

      return Optional.of(user); // Return the updated user object
    } catch (SQLException e) {
      logger.error("Error updating user: " + user, e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(int id) {
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(DELETE_USER)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      logger.error("Error deleting user with ID: " + id, e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<User> getByEmail(String email) {
    try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_USER_BY_EMAIL)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          User user = mapRowToUser(rs);
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      logger.error("Error getting user with email: " + email, e);
      throw new RuntimeException(e);
    }
    return Optional.empty();
  }

  private User mapRowToUser(ResultSet rs) throws SQLException {
    User user = new User();
    user.setId(rs.getInt("id"));
    user.setEmail(rs.getString("email"));
    user.setRole(Role.valueOf(rs.getString("role")));
    user.setEmailVerified(rs.getTimestamp("email_verified").toLocalDateTime());
    user.setPassword(rs.getString("password"));
    return user;
  }

  private void mapUserToRow(PreparedStatement ps, User user) throws SQLException {
    ps.setString(1, user.getName());
    ps.setString(2, user.getEmail());
    ps.setTimestamp(3, Timestamp.valueOf(user.getEmailVerified()));
    ps.setString(4, user.getRole().name());
    ps.setString(5, user.getPassword());
  }

}
