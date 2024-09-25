package com.oasisnourish.services.impl;

import org.thymeleaf.context.Context;

import com.oasisnourish.dto.user.UserAuthDTO;
import com.oasisnourish.dto.user.UserCreateDTO;
import com.oasisnourish.exceptions.EmailExistsException;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.models.User;
import com.oasisnourish.services.AuthService;
import com.oasisnourish.services.UserService;
import com.oasisnourish.services.EmailService;
import com.oasisnourish.services.TokenService;
import com.oasisnourish.utils.PasswordUtil;

import io.javalin.http.UnauthorizedResponse;
import jakarta.validation.ConstraintViolationException;

public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final EmailService emailService;
  private final TokenService tokenService;

  public AuthServiceImpl(UserService userService, EmailService emailService, TokenService tokenService) {
    this.userService = userService;
    this.emailService = emailService;
    this.tokenService = tokenService;
  }

  @Override
  public User signUpUser(UserCreateDTO userDTO) throws ConstraintViolationException, EmailExistsException {
    User user = userService.createUser(userDTO);
    String token = tokenService.generateToken("confirm-email", user);
    Context context = new Context();
    context.setVariable("user", user);
    context.setVariable("token", token);
    emailService.sendEmail(user.getEmail(), "Welcome to Oasis Nourish", "user/confirm", context);
    return user;
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
