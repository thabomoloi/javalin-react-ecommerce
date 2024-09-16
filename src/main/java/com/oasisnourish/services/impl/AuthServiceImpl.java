package com.oasisnourish.services.impl;

import com.oasisnourish.dto.UserInputDTO;
import com.oasisnourish.exceptions.EmailExistsException;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.models.User;
import com.oasisnourish.services.AuthService;
import com.oasisnourish.services.UserService;
import com.oasisnourish.utils.PasswordUtil;

import io.javalin.http.UnauthorizedResponse;
import jakarta.validation.ConstraintViolationException;

public class AuthServiceImpl implements AuthService {

  private final UserService userService;

  public AuthServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public User signUpUser(UserInputDTO userDTO) throws ConstraintViolationException, EmailExistsException {
    return userService.createUser(userDTO);
  }

  @Override
  public User signInUser(String email, String password) {
    try {
      User user = userService.getUserByEmail(email);
      if (PasswordUtil.checkPassword(password, user.getPassword())) {
        return user;
      }
      throw new UnauthorizedResponse("Invalid email or password.");

    } catch (NotFoundException e) {
      throw new UnauthorizedResponse("Invalid email or password.");
    }
  }

}
