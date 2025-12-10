package com.github.fiecher.turnforge.presentation.servlet.filter;

import com.github.fiecher.turnforge.infrastructure.security.TokenService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthFilter implements Filter {

    private final TokenService tokenService;

    public AuthFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String path = req.getRequestURI();
        if (path.endsWith("/api/v1/health") || path.endsWith("/api/v1/users/login") || path.endsWith("/api/v1/users")) {
            chain.doFilter(request, response);
            return;
        }

        if (path.endsWith("/users/login") || path.endsWith("/users/register")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(resp, 401, "Missing Authorization Bearer token");
            return;
        }

        String token = authHeader.substring(7);
        TokenService.AuthData authData = tokenService.validateAndParse(token);

        if (authData == null) {
            sendError(resp, 401, "Invalid or expired token");
            return;
        }

        req.setAttribute("userId", authData.userId());
        req.setAttribute("userRole", authData.role());

        chain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        resp.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    }
}