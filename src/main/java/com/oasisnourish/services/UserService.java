package com.oasisnourish.services;

import java.util.List;

import com.oasisnourish.dto.UserInputDTO;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.models.User;

import jakarta.validation.ConstraintViolationException;

public interface UserService {
  User getUserById(int id) throws NotFoundException;

  User getUserByEmail(String email) throws NotFoundException;

  List<User> getAllUsers();

  User createUser(UserInputDTO user) throws ConstraintViolationException;

  User updateUser(UserInputDTO user) throws NotFoundException, ConstraintViolationException;

  void deleteUser(int id) throws NotFoundException;
}
