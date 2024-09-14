package com.oasisnourish.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oasisnourish.dto.ResponseError;

import io.javalin.http.Context;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class ExceptionsControler {

  private static final Logger logger = LoggerFactory.getLogger(ExceptionsControler.class);

  public static void validationError(ConstraintViolationException e, Context ctx) {
    int code = 400;
    String description = "Bad Request";
    String message = e.getMessage();
    List<Map<String, String>> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
      Map<String, String> error = new HashMap<>();
      error.put("property", violation.getPropertyPath().toString());
      error.put("value", violation.getInvalidValue().toString());
      error.put("message", violation.getMessage());
      errors.add(error);
    }
    ctx.status(code);
    ctx.json(new ResponseError<>(code, description, message, errors));
  }

  public static void badRequest(Exception e, Context ctx) {
    int code = 400;
    String description = "Bad Request";
    String message = e.getMessage();
    ctx.status(code);
    ctx.json(new ResponseError<>(code, description, message, null));
  }

  public static void notFound(Exception e, Context ctx) {
    int code = 404;
    String description = "Not Found";
    String message = e.getMessage();
    ctx.status(code);
    ctx.json(new ResponseError<>(code, description, message, null));
  }

  public static void internalServerError(Exception e, Context ctx) {
    logger.error(e.getMessage(), e);

    int code = 500;
    String description = "Internal Server Error";
    String message = "We're currently experiencing issues with our server. Please try again later.";
    logger.error(e.getMessage(), e);
    ctx.status(code);
    ctx.json(new ResponseError<>(code, description, message, null));

  }
}
