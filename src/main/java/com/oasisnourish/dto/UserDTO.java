package com.oasisnourish.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.oasisnourish.enums.Role;

public class UserDTO {

  private int id;

  @NotNull(message = "Name cannot be null")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  private String name;

  @NotNull(message = "Role cannot be null")
  private Role role;

  @NotNull(message = "Email cannot be null")
  @Email(message = "Email should be valid")
  private String email;

  private LocalDateTime emailVerified;

  public UserDTO() {
  }

  public UserDTO(int id, String name, String email, Role role, LocalDateTime emailVerified) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
    this.emailVerified = emailVerified;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(LocalDateTime emailVerified) {
    this.emailVerified = emailVerified;
  }
}
