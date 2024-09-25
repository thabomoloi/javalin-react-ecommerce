package com.oasisnourish.services.impl;

import java.util.UUID;

import com.oasisnourish.models.User;
import com.oasisnourish.services.TokenService;

import redis.clients.jedis.JedisPooled;

public class TokenServiceImpl implements TokenService {
  private final JedisPooled jedis;

  private final String KEY_FORMAT = "user:%d:tokens:%s";
  private final int EXPIRES = 3600; // 1 hour

  public TokenServiceImpl(JedisPooled jedis) {
    this.jedis = jedis;
  }

  @Override
  public String generateToken(String tokenName, User user) {
    String key = String.format(KEY_FORMAT, user.getId(), tokenName);
    String token = UUID.randomUUID().toString();
    jedis.setex(key, EXPIRES, token);

    return token;

  }

  @Override
  public boolean verifyToken(String tokenName, String token, User user) {
    String key = String.format(KEY_FORMAT, user.getId(), tokenName);
    if (jedis.exists(key)) {
      String savedToken = jedis.get(key);
      if (savedToken != null && token.equals(savedToken)) {
        return true;
      }
    }
    return false;
  }

}
