package com.oasisnourish.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserInputDTO {

  @NotNull(message = "Id cannot be null", groups = ValidationGroup.Create.class)
  private int id;

  @NotNull(message = "Name cannot be null", groups = { ValidationGroup.Create.class, ValidationGroup.Update.class })
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters", groups = {
      ValidationGroup.Create.class, ValidationGroup.Update.class })
  private String name;

  @NotNull(message = "Email cannot be null", groups = { ValidationGroup.Create.class, ValidationGroup.Update.class })
  @Email(message = "Email should be valid", groups = { ValidationGroup.Create.class, ValidationGroup.Update.class })
  private String email;

  @NotNull(message = "Password cannot be null", groups = ValidationGroup.Create.class)
  @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters", groups = ValidationGroup.Create.class)
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$", message = "Password must contain at least one letter, one digit, and one special character (@, #, $, %, ^, &, +, =)", groups = ValidationGroup.Create.class)
  private String password;

  public UserInputDTO() {
  }

  public UserInputDTO(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public UserInputDTO(int id, String name, String email, String password) {
    this(name, email, password);
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
