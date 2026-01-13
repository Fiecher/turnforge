package com.github.fiecher.turnforge.presentation.servlet.api;

import com.github.fiecher.turnforge.app.dtos.requests.CreateUserRequest;
import com.github.fiecher.turnforge.app.dtos.requests.LoginRequest;
import com.github.fiecher.turnforge.app.dtos.responses.UserDetails;
import com.github.fiecher.turnforge.app.usecase.CreateUserUseCase;
import com.github.fiecher.turnforge.app.usecase.LoginUserUseCase;
import com.github.fiecher.turnforge.infrastructure.security.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class UserServlet extends BaseServlet {

    private static CreateUserUseCase createUserUseCase;
    private static LoginUserUseCase loginUserUseCase;
    private static TokenService tokenService;

    public static void setDependencies(CreateUserUseCase createUC, LoginUserUseCase loginUC, TokenService ts) {
        createUserUseCase = Objects.requireNonNull(createUC);
        loginUserUseCase = Objects.requireNonNull(loginUC);
        tokenService = Objects.requireNonNull(ts);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Endpoint required: /login or /register");
            return;
        }

        try {
            if (pathInfo.endsWith("/register")) {
                handleRegister(req, resp);
            } else if (pathInfo.endsWith("/login")) {
                handleLogin(req, resp);
            } else {
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Unknown endpoint");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage());
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateUserRequest request = readBody(req, CreateUserRequest.class);

        if (request.login() == null || request.password() == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Login and password required");
            return;
        }

        createUserUseCase.execute(request);
        sendCreated(resp, null, "User registered successfully");
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LoginRequest request = readBody(req, LoginRequest.class);

        Optional<UserDetails> userDetailsOpt = loginUserUseCase.execute(request);

        if (userDetailsOpt.isEmpty()) {
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Invalid login or password");
            return;
        }

        UserDetails user = userDetailsOpt.get();

        String token = tokenService.generateToken(user.id(), user.role());

        sendJson(resp, new LoginResponse(token, user));
    }

    public record LoginResponse(String token, UserDetails user) {}
}