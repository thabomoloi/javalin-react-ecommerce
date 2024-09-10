package com.oasisnourish.utils;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

public class ValidationUtil {

  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  public static <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups)
      throws ConstraintViolationException {
    return validator.validate(object, groups);
  }
}
