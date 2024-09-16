package com.oasisnourish.services;

import com.oasisnourish.dto.UserInputDTO;
import com.oasisnourish.exceptions.EmailExistsException;
import com.oasisnourish.models.User;

import jakarta.validation.ConstraintViolationException;

public interface AuthService {
  User signUpUser(UserInputDTO userDTO) throws ConstraintViolationException, EmailExistsException;

  User signInUser(String email, String password);

}
