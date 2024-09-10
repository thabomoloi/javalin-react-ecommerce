package com.oasisnourish.services;

import java.util.List;

import com.oasisnourish.dto.UserInputDTO;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.models.User;

public interface UserService {
  User getUserById(int id) throws NotFoundException;

  User getUserByEmail(String email) throws NotFoundException;

  List<User> getAllUsers();

  void createUser(UserInputDTO user);

  void updateUser(UserInputDTO user) throws NotFoundException;

  void deleteUser(int id) throws NotFoundException;
}
