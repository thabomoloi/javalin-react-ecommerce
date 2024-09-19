package com.oasisnourish.services.impl;

import java.util.List;

import com.oasisnourish.dao.UserDAO;
import com.oasisnourish.dto.user.UserCreateDTO;
import com.oasisnourish.dto.user.UserUpdateDTO;
import com.oasisnourish.exceptions.EmailExistsException;
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
  public User createUser(UserCreateDTO userDTO) throws ConstraintViolationException, EmailExistsException {
    ValidationUtil.validate(userDTO);

    if (userDAO.getByEmail(userDTO.getEmail()) == null) {
      throw new EmailExistsException("The email has already been taken");
    }

    User user = new User();
    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());
    user.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
    return userDAO.save(user).get();
  }

  @Override
  public User updateUser(UserUpdateDTO userDTO) throws NotFoundException, ConstraintViolationException {
    ValidationUtil.validate(userDTO);

    User user = userDAO.get(userDTO.getId())
        .orElseThrow(() -> new NotFoundException("User with ID " + userDTO.getId() + " not found."));

    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());

    return userDAO.update(user).get();

  }

  @Override
  public void deleteUser(int id) throws NotFoundException {
    userDAO.delete(id);
  }

}
