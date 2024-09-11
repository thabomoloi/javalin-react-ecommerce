package com.oasisnourish;

import com.oasisnourish.controllers.ExceptionsControler;
import com.oasisnourish.controllers.UserController;
import com.oasisnourish.dao.impl.UserDAOImpl;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.services.impl.UserServiceImpl;

import io.javalin.Javalin;

public class App {
    private final static UserController userController = new UserController(new UserServiceImpl(new UserDAOImpl()));

    public static void main(String[] args) {

        var app = Javalin.create(/* config */);
        app.start(7070);

        // Exceptions
        app.exception(NotFoundException.class, ExceptionsControler::notFound);
        app.exception(Exception.class, ExceptionsControler::internalServerError);

        // Users
        app.get("/api/users", userController::getUsers);
        app.get("/api/users/{userId}", userController::getUser);
        app.post("/api/users", userController::createUser);
        app.put("/api/users/{userId}", userController::updateUser);
        app.delete("/api/users/{userId}", userController::deleteUser);

    }
}