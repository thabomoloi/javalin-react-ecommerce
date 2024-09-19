package com.oasisnourish.services;

import java.util.Map;
import java.util.Optional;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.oasisnourish.models.User;

import javalinjwt.JWTProvider;

public interface JWTService {

  int getTokenExpires(String tokenType);

  JWTProvider<User> getProvider();

  Map<String, String> generateTokens(User user);

  boolean isTokenValid(String token);

  void revokeToken(String token);

  Optional<DecodedJWT> getToken(String token);

  long getTokenVersion(int userId);

}
