package com.oasisnourish.dto;

public class ResponseError<T> {
  private int code;
  private String description;
  private String message;
  private T errors;

  public ResponseError(int code, String description, String message, T errors) {
    this.code = code;
    this.description = description;
    this.message = message;
    this.errors = errors;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getErrors() {
    return errors;
  }

  public void setErrors(T errors) {
    this.errors = errors;
  }

}