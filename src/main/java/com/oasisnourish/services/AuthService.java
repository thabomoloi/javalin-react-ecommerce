package com.oasisnourish.services;

import com.oasisnourish.dto.user.UserAuthDTO;
import com.oasisnourish.dto.user.UserCreateDTO;
import com.oasisnourish.exceptions.EmailExistsException;
import com.oasisnourish.models.User;

import jakarta.validation.ConstraintViolationException;

public interface AuthService {
  User signUpUser(UserCreateDTO userDTO) throws ConstraintViolationException, EmailExistsException;

  User signInUser(UserAuthDTO userDTO);

}
