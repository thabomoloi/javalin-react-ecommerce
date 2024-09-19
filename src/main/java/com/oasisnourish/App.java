package com.oasisnourish;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.oasisnourish.controllers.AuthController;
import com.oasisnourish.controllers.ExceptionsControler;
import com.oasisnourish.controllers.UserController;
import com.oasisnourish.dao.impl.UserDAOImpl;
import com.oasisnourish.database.DatabaseConnection;
import com.oasisnourish.enums.Role;
import com.oasisnourish.exceptions.EmailExistsException;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.services.UserService;
import com.oasisnourish.services.impl.AuthServiceImpl;
import com.oasisnourish.services.impl.JWTServiceImpl;
import com.oasisnourish.services.impl.UserServiceImpl;

import io.javalin.Javalin;
import jakarta.validation.ConstraintViolationException;

public class App {

    public static void main(String[] args) {

        UserService userService = new UserServiceImpl(new UserDAOImpl());
        UserController userController = new UserController(userService);
        AuthController authController = new AuthController(
                userService,
                new AuthServiceImpl(userService),
                new JWTServiceImpl(DatabaseConnection.getRedis()));

        var app = Javalin.create(/* config */);
        app.start(7070);

        // Before
        app.beforeMatched(authController);
        app.before(authController::decodeJWTFromCookie);

        // Exceptions
        app.exception(EmailExistsException.class, ExceptionsControler::badRequest);
        app.exception(ConstraintViolationException.class, ExceptionsControler::validationError);
        app.exception(NotFoundException.class, ExceptionsControler::notFound);
        app.exception(Exception.class, ExceptionsControler::internalServerError);

        // auth
        app.post("/api/auth/signup", authController::signUpUser, Role.ANYONE, Role.USER, Role.ADMIN);
        app.post("/api/auth/signin", authController::signInUser, Role.ANYONE, Role.USER, Role.ADMIN);

        // Users
        app.get("/api/users/me", authController::getCurrentUser, Role.USER, Role.ADMIN);
        app.get("/api/users", userController::getUsers, Role.ANYONE);
        app.get("/api/users/{userId}", userController::getUser);
        app.post("/api/users", userController::createUser);
        app.put("/api/users/{userId}", userController::updateUser);
        app.delete("/api/users/{userId}", userController::deleteUser);

    }
}