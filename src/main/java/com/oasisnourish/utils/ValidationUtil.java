package com.oasisnourish.utils;

import java.util.Set;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidationUtil {

  private static final ValidatorFactory factory = Validation.byDefaultProvider()
      .configure()
      .messageInterpolator(new ParameterMessageInterpolator())
      .buildValidatorFactory();

  private static final Validator validator = factory.getValidator();

  public static <T> Set<ConstraintViolation<T>> validationViolations(T object, Class<?>... groups)
      throws ConstraintViolationException {
    return validator.validate(object, groups);
  }

  public static <T> void validate(T object, Class<?>... groups) throws ConstraintViolationException {
    Set<ConstraintViolation<T>> violations = validator.validate(object, groups);

    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<T> violation : violations) {
        sb.append(violation.getMessage()).append("\n");
      }
      throw new ConstraintViolationException(sb.toString(), violations);
    }
  }
}
