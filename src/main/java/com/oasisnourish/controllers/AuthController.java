package com.oasisnourish.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.oasisnourish.config.EnvironmentConfig;
import com.oasisnourish.dto.UserDTO;
import com.oasisnourish.dto.UserInputDTO;
import com.oasisnourish.enums.Role;
import com.oasisnourish.exceptions.EmailExistsException;
import com.oasisnourish.exceptions.NotFoundException;
import com.oasisnourish.models.User;
import com.oasisnourish.services.AuthService;
import com.oasisnourish.services.JWTService;
import com.oasisnourish.services.UserService;

import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.http.Context;
import io.javalin.http.Cookie;
import io.javalin.http.Handler;
import io.javalin.http.SameSite;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.security.RouteRole;
import jakarta.validation.ConstraintViolationException;
import javalinjwt.JWTProvider;

public class AuthController implements Handler {

  private final Dotenv dotenv = EnvironmentConfig.getDotenv();

  private final JWTService jwtService;
  private final AuthService authService;
  private final UserService userService;
  private final Map<String, RouteRole> rolesMapping = new HashMap<>() {
    {
      put("user", Role.USER);
      put("admin", Role.ADMIN);
    }
  };

  public AuthController(UserService userService, AuthService authService, JWTService jwtService) {
    this.jwtService = jwtService;
    this.userService = userService;
    this.authService = authService;
  }

  public JWTProvider<User> getProvider() {
    return jwtService.getProvider();
  }

  public void createCookieDecodeHandler(Context ctx) {
    String accessToken = ctx.cookie("JWTAccessToken");
    if (accessToken == null) {
      return;
    }

    jwtService.getProvider().validateToken(accessToken).ifPresent(jwt -> {
      ctx.sessionAttribute("JWTAccessToken", jwt);
      long version = jwt.getClaim("version").asLong();
      int userId = jwt.getClaim("userId").asInt();

      try {
        User user = userService.getUserById(userId);
        if (version == jwtService.getTokenVersion(user)) {
          ctx.sessionAttribute("currentUser", user);
        }
      } catch (NotFoundException e) {
        ctx.sessionAttribute("currentUser", null);
      }
    });

  }

  public void getCurrentUser(Context ctx) {
    User user = ctx.sessionAttribute("currentUser");
    if (user != null) {
      ctx.json(UserDTO.fromUser(user));
    }
    ctx.json(new HashMap<>());
  }

  public void signUpUser(Context ctx) throws ConstraintViolationException, EmailExistsException {
    var userDTO = ctx.bodyAsClass(UserInputDTO.class);
    var user = authService.signUpUser(userDTO);
    ctx.status(201);
    ctx.json(user);
  }

  public void signInUser(Context ctx) throws ConstraintViolationException, EmailExistsException {
    String email = ctx.formParam("email");
    String password = ctx.formParam("password");
    if (email == null || password == null || email.isBlank() || password.isBlank()) {
      throw new UnauthorizedResponse("Invalid email or password.");
    }
    Map<String, String> tokens = jwtService.generateTokens(authService.signInUser(email, password));

    String enviroment = dotenv.get("ENV", "development");
    Cookie accessToken = new Cookie("JWTAccessToken", tokens.get("JWTAccessToken"));

    accessToken.setHttpOnly(true);
    accessToken.setSecure("production".equals(enviroment));
    accessToken.setSameSite(SameSite.STRICT);
    accessToken.setMaxAge(jwtService.getTokenExpires("access") * 60);

    Cookie refreshToken = new Cookie("JWTRefreshToken", tokens.get("JWTRefreshToken"));
    refreshToken.setHttpOnly(true);
    refreshToken.setSecure("production".equals(enviroment));
    accessToken.setSameSite(SameSite.STRICT);
    refreshToken.setMaxAge(jwtService.getTokenExpires("access") * 60);

    ctx.cookie(accessToken);
    ctx.cookie(refreshToken);
    ctx.status(200);
  }

  @Override
  public void handle(@NotNull Context ctx) {
    DecodedJWT jwt = ctx.sessionAttribute("JWTAccessToken");

    String userLevel = jwt == null ? "" : jwt.getClaim("role").asString();
    RouteRole role = Optional.ofNullable(rolesMapping.get(userLevel)).orElse(Role.ANYONE);
    Set<RouteRole> permittedRoles = ctx.routeRoles();
    if (jwt != null) {
      int userId = jwt.getClaim("userId").asInt();
      long version = jwt.getClaim("version").asLong();

      try {
        User user = userService.getUserById(userId);
        if (version != jwtService.getTokenVersion(user)) {
          throw new UnauthorizedResponse();
        }
        ctx.sessionAttribute("currentUser", user);
      } catch (NotFoundException e) {
        throw new UnauthorizedResponse();
      }
    }

    if (!permittedRoles.contains(role)) {
      throw new UnauthorizedResponse();
    }
  }

}
