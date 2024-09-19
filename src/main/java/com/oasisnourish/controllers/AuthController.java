package com.oasisnourish.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.oasisnourish.config.EnvironmentConfig;
import com.oasisnourish.dto.user.UserAuthDTO;
import com.oasisnourish.dto.user.UserCreateDTO;
import com.oasisnourish.dto.user.UserDTO;
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

public class AuthController implements Handler {

  private static final String JWT_ACCESS_KEY = "JWTAccessToken";
  private static final String JWT_REFRESH_KEY = "JWTRefreshToken";

  private final Dotenv dotenv;
  private final JWTService jwtService;
  private final AuthService authService;
  private final UserService userService;

  private final Map<String, RouteRole> rolesMapping = Map.of(
      "user", Role.USER,
      "admin", Role.ADMIN);

  public AuthController(UserService userService, AuthService authService, JWTService jwtService) {
    this.dotenv = EnvironmentConfig.getDotenv();
    this.jwtService = jwtService;
    this.userService = userService;
    this.authService = authService;
  }

  /**
   * Handles JWT access token decoding and sets it in session if valid.
   */
  public void decodeJWTFromCookie(Context ctx) {
    Optional.ofNullable(ctx.cookie(JWT_ACCESS_KEY))
        .flatMap(jwtService::getToken)
        .ifPresent(jwt -> ctx.sessionAttribute(JWT_ACCESS_KEY, jwt));
  }

  /**
   * Retrieves the current authenticated user from the session and returns the
   * user data.
   */
  public void getCurrentUser(Context ctx) {
    Optional.ofNullable(ctx.sessionAttribute("currentUser"))
        .ifPresentOrElse(
            user -> ctx.json(UserDTO.fromUser((User) user)),
            () -> ctx.status(404).result("No user is currently logged in."));
  }

  /**
   * Handles user signup.
   */
  public void signUpUser(Context ctx) throws ConstraintViolationException, EmailExistsException {
    UserCreateDTO userDTO = ctx.bodyAsClass(UserCreateDTO.class);
    User user = authService.signUpUser(userDTO);
    ctx.status(201).json(user);
  }

  /**
   * Handles user login, generates JWT tokens, and sets cookies.
   */
  public void signInUser(Context ctx) throws ConstraintViolationException, EmailExistsException {
    UserAuthDTO userDTO = ctx.bodyAsClass(UserAuthDTO.class);
    Map<String, String> tokens = jwtService.generateTokens(authService.signInUser(userDTO));

    ctx.cookie(createTokenCookie("access", tokens.get(JWT_ACCESS_KEY)));
    ctx.cookie(createTokenCookie("refresh", tokens.get(JWT_REFRESH_KEY)));
    ctx.status(200).result("Login successful.");
  }

  /**
   * Main handler for securing routes, checking roles, and validating JWT tokens.
   */
  @Override
  public void handle(@NotNull Context ctx) {
    DecodedJWT jwt = ctx.sessionAttribute(JWT_ACCESS_KEY);

    String userRole = Optional.ofNullable(jwt)
        .map(token -> token.getClaim("role").asString())
        .orElse("");
    RouteRole role = rolesMapping.getOrDefault(userRole, Role.ANYONE);

    Set<RouteRole> permittedRoles = ctx.routeRoles();

    if (!permittedRoles.contains(role)) {
      throw new UnauthorizedResponse();
    }

    validateAndSetUserSession(ctx, jwt, permittedRoles);
  }

  /**
   * Validates the JWT version and sets the current user in the session.
   */
  private void validateAndSetUserSession(Context ctx, DecodedJWT jwt, Set<RouteRole> permittedRoles) {
    if (jwt != null && !permittedRoles.contains(Role.ANYONE)) {
      int userId = jwt.getClaim("userId").asInt();
      long version = jwt.getClaim("version").asLong();

      try {
        if (version != jwtService.getTokenVersion(userId)) {
          invalidateSession(ctx);
          throw new UnauthorizedResponse();
        }
        User user = ctx.sessionAttribute("currentUser");
        if (user == null || user.getId() != userId) {
          user = userService.getUserById(userId);
          ctx.sessionAttribute("currentUser", user);
        }
      } catch (NotFoundException e) {
        invalidateSession(ctx);
        throw new UnauthorizedResponse();
      }
    }
  }

  /**
   * Invalidates the current session by removing the currentUser attribute.
   */
  private void invalidateSession(Context ctx) {
    ctx.sessionAttribute("currentUser", null);
  }

  /**
   * Creates a secure cookie for JWT tokens.
   */
  private Cookie createTokenCookie(String tokenType, String token) {
    String environment = dotenv.get("ENV", "development");

    Cookie cookie = new Cookie("access".equals(tokenType) ? JWT_ACCESS_KEY : JWT_REFRESH_KEY, token);
    cookie.setHttpOnly(true);
    cookie.setSecure("production".equals(environment));
    cookie.setSameSite(SameSite.STRICT);
    cookie.setMaxAge(jwtService.getTokenExpires(tokenType) * 60);
    return cookie;
  }
}
