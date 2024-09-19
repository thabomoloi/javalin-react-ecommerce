package com.oasisnourish.services;

import java.util.List;

import com.oasisnourish.dto.user.UserCreateDTO;
import com.oasisnourish.dto.user.UserUpdateDTO;
import com.oasisnourish.exceptions.EmailExistsException;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.models.User;

import jakarta.validation.ConstraintViolationException;

public interface UserService {
  User getUserById(int id) throws NotFoundException;

  User getUserByEmail(String email) throws NotFoundException;

  List<User> getAllUsers();

  User createUser(UserCreateDTO user) throws ConstraintViolationException, EmailExistsException;

  User updateUser(UserUpdateDTO user) throws NotFoundException, ConstraintViolationException;

  void deleteUser(int id) throws NotFoundException;
}
