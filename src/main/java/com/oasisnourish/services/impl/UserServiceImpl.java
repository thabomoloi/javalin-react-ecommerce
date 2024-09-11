package com.oasisnourish.services.impl;

import java.util.List;

import com.oasisnourish.dao.UserDAO;
import com.oasisnourish.dto.UserInputDTO;
import com.oasisnourish.dto.ValidationGroup;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.models.User;
import com.oasisnourish.services.UserService;
import com.oasisnourish.utils.PasswordUtil;
import com.oasisnourish.utils.ValidationUtil;

import jakarta.validation.ConstraintViolationException;

public class UserServiceImpl implements UserService {
  private final UserDAO userDAO;

  public UserServiceImpl(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @Override
  public User getUserById(int id) throws NotFoundException {
    return userDAO.get(id)
        .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found."));
  }

  @Override
  public User getUserByEmail(String email) throws NotFoundException {
    return userDAO.getByEmail(email)
        .orElseThrow(() -> new NotFoundException("User with email " + email + " not found."));
  }

  @Override
  public List<User> getAllUsers() {
    return userDAO.getAll();
  }

  @Override
  public User createUser(UserInputDTO userDTO) throws ConstraintViolationException {
    ValidationUtil.validate(userDTO, ValidationGroup.Create.class);

    User user = new User();
    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());
    user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
    return userDAO.save(user);
  }

  @Override
  public User updateUser(UserInputDTO userDTO) throws NotFoundException, ConstraintViolationException {
    ValidationUtil.validate(userDTO, ValidationGroup.Update.class);

    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
      ValidationUtil.validate(userDTO, ValidationGroup.Create.class);
    }

    User user = userDAO.get(userDTO.getId())
        .orElseThrow(() -> new NotFoundException("User with ID " + userDTO.getId() + " not found."));

    if (!user.getEmail().equals(userDTO.getEmail())) {
      user.setEmailVerified(null);
    }

    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());

    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
      user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
    }
    return userDAO.update(user).get();

  }

  @Override
  public void deleteUser(int id) throws NotFoundException {
    User user = userDAO.get(id)
        .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found."));
    userDAO.delete(user.getId());
  }

}
