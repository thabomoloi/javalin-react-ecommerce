package com.oasisnourish.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO extends UserBaseDTO {
  @NotNull(message = "Id cannot be null")
  private int id;

  @NotBlank(message = "Name cannot be blank")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  private String name;

  public UserUpdateDTO() {
  }

  public UserUpdateDTO(int id, String name, String email, String password) {
    super(email);
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
