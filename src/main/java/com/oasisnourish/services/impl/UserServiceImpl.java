package com.oasisnourish.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import com.oasisnourish.dao.UserDAO;
import com.oasisnourish.dto.UserInputDTO;
import com.oasisnourish.dto.ValidationGroup;
import com.oasisnourish.models.User;
import com.oasisnourish.services.UserService;
import com.oasisnourish.utils.PasswordUtil;
import com.oasisnourish.utils.ValidationUtil;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class UserServiceImpl implements UserService {
  private final UserDAO userDAO;

  public UserServiceImpl(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @Override
  public Optional<User> getUserById(int id) {
    return userDAO.get(id);
  }

  @Override
  public Optional<User> getUserByEmail(String email) {
    return userDAO.getByEmail(email);
  }

  @Override
  public List<User> getAllUsers() {
    return userDAO.getAll();
  }

  @Override
  public void createUser(UserInputDTO userDTO) throws ConstraintViolationException {
    Set<ConstraintViolation<UserInputDTO>> violations = ValidationUtil.validate(userDTO, ValidationGroup.Create.class);

    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<UserInputDTO> violation : violations) {
        sb.append(violation.getMessage()).append("\n");
      }
      throw new ConstraintViolationException(sb.toString(), violations);
    }

    User user = new User();
    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());
    user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
    userDAO.save(user);
  }

  @Override
  public void updateUser(UserInputDTO userDTO) {
    Set<ConstraintViolation<UserInputDTO>> violations = ValidationUtil.validate(userDTO, ValidationGroup.Update.class);

    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
      violations.addAll(ValidationUtil.validate(userDTO, ValidationGroup.Create.class));
    }

    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<UserInputDTO> violation : violations) {
        sb.append(violation.getMessage()).append("\n");
      }
      throw new ConstraintViolationException(sb.toString(), violations);
    }

    Optional<User> existingUser = userDAO.get(userDTO.getId());
    if (existingUser.isPresent()) {
      User user = existingUser.get();
      user.setName(userDTO.getName());
      user.setEmail(userDTO.getEmail());
      if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
        user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
      }
      userDAO.update(user);
    }
    // throw not found exception
  }

  @Override
  public void deleteUser(int id) {
    try {
      User user = userDAO.get(id).orElseThrow();
      userDAO.delete(user.getId());
    } catch (NoSuchElementException e) {
      throw e;
    }
  }

}
