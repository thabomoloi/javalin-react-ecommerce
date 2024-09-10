package com.oasisnourish.models;

import java.time.LocalDateTime;

import com.oasisnourish.enums.Role;

public class User {

  private int id;
  private String name;
  private Role role;
  private String email;
  private LocalDateTime emailVerified;
  private String password;

  public User() {
    role = Role.USER;
  }

  public User(int id, String name, String email, Role role, LocalDateTime emailVerified, String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
    this.emailVerified = emailVerified;
    this.password = password;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
