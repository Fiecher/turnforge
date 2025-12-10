package com.github.fiecher.turnforge.presentation.servlet.api;

import com.github.fiecher.turnforge.app.dtos.requests.CreateArmorRequest;
import com.github.fiecher.turnforge.app.dtos.requests.UpdateArmorRequest;
import com.github.fiecher.turnforge.app.usecase.armor.*;
import com.github.fiecher.turnforge.domain.models.Armor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ArmorServlet extends BaseServlet {
    private static CreateArmorUseCase createUC;
    private static GetArmorUseCase getUC;
    private static UpdateArmorUseCase updateUC;
    private static DeleteArmorUseCase deleteUC;

    public static void setDependencies(CreateArmorUseCase c, GetArmorUseCase g, UpdateArmorUseCase u, DeleteArmorUseCase d) {
        createUC = Objects.requireNonNull(c); getUC = Objects.requireNonNull(g); updateUC = Objects.requireNonNull(u); deleteUC = Objects.requireNonNull(d);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractIdFromPath(req.getPathInfo());
        Optional<List<Armor>> result = getUC.execute(id);

        if (id == null) {
            sendJson(resp, result.orElse(List.of()));
            return;
        }

        if (result.isEmpty() || result.get().isEmpty()) {
            sendError(resp, 404, "Armor with ID " + id + " not found");
        } else {
            sendJson(resp, result.get().get(0));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        CreateArmorRequest body = readBody(req, CreateArmorRequest.class);
        sendCreated(resp, createUC.execute(body), "Armor created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }

        UpdateArmorRequest body = readBody(req, UpdateArmorRequest.class);
        UpdateArmorRequest finalReq = new UpdateArmorRequest(id, body.name(), body.description(), body.image(), body.AC(), body.type(), body.weight(), body.price());
        sendJson(resp, updateUC.execute(finalReq));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }
        deleteUC.execute(id);
        sendSuccess(resp, "Armor deleted");
    }
}