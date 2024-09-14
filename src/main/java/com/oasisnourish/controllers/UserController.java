package com.oasisnourish.controllers;

import java.util.stream.Collectors;

import com.oasisnourish.dto.UserDTO;
import com.oasisnourish.dto.UserInputDTO;
import com.oasisnourish.exceptions.EmailExistsException;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.services.UserService;

import io.javalin.http.Context;
import jakarta.validation.ConstraintViolationException;

public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public void getUser(Context ctx) throws NotFoundException {
    int id = ctx.pathParamAsClass("userId", Integer.class).get();
    var user = UserDTO.fromUser(userService.getUserById(id));
    ctx.json(user);
  }

  public void getUsers(Context ctx) {
    var users = userService.getAllUsers().stream()
        .map(UserDTO::fromUser)
        .collect(Collectors.toList());
    ctx.json(users);
  }

  public void createUser(Context ctx) throws ConstraintViolationException, EmailExistsException {
    var userDTO = ctx.bodyAsClass(UserInputDTO.class);
    var user = userService.createUser(userDTO);
    ctx.status(201);
    ctx.json(user);
  }

  public void updateUser(Context ctx) throws NotFoundException {
    int id = ctx.pathParamAsClass("userId", Integer.class).get();
    var userDTO = ctx.bodyAsClass(UserInputDTO.class);
    userDTO.setId(id);
    var user = userService.updateUser(userDTO);
    ctx.json(user);
  }

  public void deleteUser(Context ctx) throws NotFoundException {
    int id = ctx.pathParamAsClass("userId", Integer.class).get();
    userService.deleteUser(id);
  }

}
