package com.oasisnourish.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import jakarta.validation.ConstraintViolationException;

public class ExceptionsControler {

  private static final Logger logger = LoggerFactory.getLogger(ExceptionsControler.class);

  public static void validationError(ConstraintViolationException e, Context ctx) {
    throw new BadRequestResponse(e.getMessage());
  }

  public static void badRequest(Exception e, Context ctx) {
    throw new BadRequestResponse();
  }

  public static void notFound(Exception e, Context ctx) {
    throw new NotFoundResponse(e.getMessage());
  }

  public static void internalServerError(Exception e, Context ctx) {
    logger.error(e.getMessage(), e);
    throw new InternalServerErrorResponse(
        "We're currently experiencing issues with our server. Please try again later.");
  }
}
