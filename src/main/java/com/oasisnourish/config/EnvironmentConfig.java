package com.oasisnourish.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentConfig {
  private static final Dotenv dotenv;

  static {
    dotenv = Dotenv.load();
  }

  public static Dotenv getDotenv() {
    return dotenv;
  }

}
