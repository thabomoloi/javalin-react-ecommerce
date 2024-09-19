package com.oasisnourish.dto.user;

import jakarta.validation.constraints.NotBlank;

public class UserAuthDTO extends UserBaseDTO {

  @NotBlank(message = "Password cannot be blank")
  private String password;

  public UserAuthDTO() {
  }

  public UserAuthDTO(String email, String password) {
    super(email);
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
