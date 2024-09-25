package com.oasisnourish.services;

import com.oasisnourish.models.User;

public interface TokenService {
  String generateToken(String tokenName, User user);

  boolean verifyToken(String tokenName, String token, User user);
}