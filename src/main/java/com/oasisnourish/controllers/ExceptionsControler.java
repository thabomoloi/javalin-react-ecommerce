package com.oasisnourish.controllers;

import com.oasisnourish.dto.ResponseError;

import io.javalin.http.Context;

public class ExceptionsControler {

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
    int code = 500;
    String description = "Internal Server Error";
    String message = "We're currently experiencing issues with our server. Please try again later.";
    ctx.status(code);
    ctx.json(new ResponseError<>(code, description, message, null));
  }
}
