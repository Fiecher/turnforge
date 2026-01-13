package com.github.fiecher.turnforge.presentation.servlet.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public abstract class BaseServlet extends HttpServlet {

    protected static final Jsonb jsonb = JsonbBuilder.create();

    protected void sendJson(HttpServletResponse resp, Object object) throws IOException {
        setupResponse(resp, HttpServletResponse.SC_OK);
        try (PrintWriter out = resp.getWriter()) {
            jsonb.toJson(object, out);
        }
    }

    protected void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        setupResponse(resp, status);
        try (PrintWriter out = resp.getWriter()) {
            jsonb.toJson(new ErrorResponse(message), out);
        }
    }

    protected void sendSuccess(HttpServletResponse resp, String message) throws IOException {
        sendJson(resp, new SuccessResponse(message));
    }

    protected void sendCreated(HttpServletResponse resp, Long id, String message) throws IOException {
        setupResponse(resp, HttpServletResponse.SC_CREATED);
        try (PrintWriter out = resp.getWriter()) {
            jsonb.toJson(new CreationResponse(id, message), out);
        }
    }

    private void setupResponse(HttpServletResponse resp, int status) {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        addCorsHeaders(resp);
    }

    protected <T> T readBody(HttpServletRequest req, Class<T> clazz) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8)) {
            return jsonb.fromJson(reader, clazz);
        } catch (Exception e) {
            throw new IOException("Failed to parse JSON body", e);
        }
    }

    protected Long getAuthUserId(HttpServletRequest req) {
        Object userId = req.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("User ID missing in request context. Is AuthFilter active?");
        }
        return (Long) userId;
    }

    protected Long extractIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.length() <= 1) {
            return null;
        }
        String[] parts = pathInfo.split("/");
        if (parts.length < 2) {
            return null;
        }
        try {
            return Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        addCorsHeaders(resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void addCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    public record ErrorResponse(String error) {}
    public record SuccessResponse(String message) {}
    public record CreationResponse(Long id, String message) {}
}