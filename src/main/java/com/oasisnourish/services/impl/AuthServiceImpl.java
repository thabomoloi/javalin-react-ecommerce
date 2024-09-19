package com.oasisnourish.services.impl;

import com.oasisnourish.dto.user.UserAuthDTO;
import com.oasisnourish.dto.user.UserCreateDTO;
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
  public User signUpUser(UserCreateDTO userDTO) throws ConstraintViolationException, EmailExistsException {
    return userService.createUser(userDTO);
  }

  @Override
  public User signInUser(UserAuthDTO userDTO) {
    try {
      User user = userService.getUserByEmail(userDTO.getEmail());
      if (PasswordUtil.checkPassword(userDTO.getPassword(), user.getPassword())) {
        return user;
      }
      throw new UnauthorizedResponse("Invalid email or password.");

    } catch (NotFoundException e) {
      throw new UnauthorizedResponse("Invalid email or password.");
    }
  }

}
