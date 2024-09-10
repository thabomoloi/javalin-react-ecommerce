package com.oasisnourish.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
  private static final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

  public static String hashPassword(String plainTextPassword) {
    return bcrypt.encode(plainTextPassword);
  }

  public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
    return bcrypt.matches(plainTextPassword, hashedPassword);
  }
}
