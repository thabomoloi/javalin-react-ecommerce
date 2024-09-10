package com.oasisnourish.dao;

import java.util.Optional;

import com.oasisnourish.models.User;

public interface UserDAO extends DAO<User> {
  Optional<User> getByEmail(String email);
}
