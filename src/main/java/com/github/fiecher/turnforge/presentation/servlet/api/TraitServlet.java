package com.github.fiecher.turnforge.presentation.servlet.api;

import com.github.fiecher.turnforge.app.dtos.requests.CreateTraitRequest;
import com.github.fiecher.turnforge.app.dtos.requests.UpdateTraitRequest;
import com.github.fiecher.turnforge.app.usecase.trait.*;
import com.github.fiecher.turnforge.domain.models.Trait;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TraitServlet extends BaseServlet {
    private static CreateTraitUseCase createUC;
    private static GetTraitUseCase getUC;
    private static UpdateTraitUseCase updateUC;
    private static DeleteTraitUseCase deleteUC;

    public static void setDependencies(CreateTraitUseCase c, GetTraitUseCase g, UpdateTraitUseCase u, DeleteTraitUseCase d) {
        createUC = Objects.requireNonNull(c); getUC = Objects.requireNonNull(g); updateUC = Objects.requireNonNull(u); deleteUC = Objects.requireNonNull(d);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractIdFromPath(req.getPathInfo());
        Optional<List<Trait>> result = getUC.execute(id);
        if (result.isEmpty() || result.get().isEmpty()) sendError(resp, 404, "Trait not found");
        else sendJson(resp, result.get());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        CreateTraitRequest body = readBody(req, CreateTraitRequest.class);
        sendCreated(resp, createUC.execute(body), "Trait created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }

        UpdateTraitRequest body = readBody(req, UpdateTraitRequest.class);
        UpdateTraitRequest finalReq = new UpdateTraitRequest(id, body.name(), body.description(), body.image(), body.prerequisites(), body.trait_type());
        sendJson(resp, updateUC.execute(finalReq));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }
        deleteUC.execute(id);
        sendSuccess(resp, "Trait deleted");
    }
}