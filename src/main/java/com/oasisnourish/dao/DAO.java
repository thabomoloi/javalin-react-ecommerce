package com.oasisnourish.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
  Optional<T> get(int id); // Get by ID

  List<T> getAll(); // Get all records

  T save(T t); // Save a record

  Optional<T> update(T t); // Update a record

  void delete(int id); // Delete a record
}
