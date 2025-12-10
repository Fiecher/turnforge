package com.github.fiecher.turnforge.presentation.servlet.api;

import com.github.fiecher.turnforge.app.dtos.requests.CreateItemRequest;
import com.github.fiecher.turnforge.app.dtos.requests.UpdateItemRequest;
import com.github.fiecher.turnforge.app.usecase.item.*;
import com.github.fiecher.turnforge.domain.models.Item;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ItemServlet extends BaseServlet {
    private static CreateItemUseCase createUC;
    private static GetItemUseCase getUC;
    private static UpdateItemUseCase updateUC;
    private static DeleteItemUseCase deleteUC;

    public static void setDependencies(CreateItemUseCase c, GetItemUseCase g, UpdateItemUseCase u, DeleteItemUseCase d) {
        createUC = Objects.requireNonNull(c); getUC = Objects.requireNonNull(g); updateUC = Objects.requireNonNull(u); deleteUC = Objects.requireNonNull(d);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractIdFromPath(req.getPathInfo());
        Optional<List<Item>> result = getUC.execute(id);
        if (result.isEmpty() || result.get().isEmpty()) sendError(resp, 404, "Item not found");
        else sendJson(resp, result.get());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        CreateItemRequest body = readBody(req, CreateItemRequest.class);
        sendCreated(resp, createUC.execute(body), "Item created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }

        UpdateItemRequest body = readBody(req, UpdateItemRequest.class);
        UpdateItemRequest finalReq = new UpdateItemRequest(id, body.name(), body.description(), body.image(), body.weight(), body.price());
        sendJson(resp, updateUC.execute(finalReq));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }
        deleteUC.execute(id);
        sendSuccess(resp, "Item deleted");
    }
}