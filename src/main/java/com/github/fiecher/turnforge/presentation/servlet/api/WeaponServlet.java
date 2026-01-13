package com.github.fiecher.turnforge.presentation.servlet.api;

import com.github.fiecher.turnforge.app.dtos.requests.CreateWeaponRequest;
import com.github.fiecher.turnforge.app.dtos.requests.UpdateWeaponRequest;
import com.github.fiecher.turnforge.app.usecase.weapon.*;
import com.github.fiecher.turnforge.app.usecase.weapon.CreateWeaponUseCase;
import com.github.fiecher.turnforge.domain.models.Weapon;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WeaponServlet extends BaseServlet {
    private static CreateWeaponUseCase createUC;
    private static GetWeaponUseCase getUC;
    private static UpdateWeaponUseCase updateUC;
    private static DeleteWeaponUseCase deleteUC;

    public static void setDependencies(CreateWeaponUseCase c, GetWeaponUseCase g, UpdateWeaponUseCase u, DeleteWeaponUseCase d) {
        createUC = Objects.requireNonNull(c); getUC = Objects.requireNonNull(g); updateUC = Objects.requireNonNull(u); deleteUC = Objects.requireNonNull(d);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractIdFromPath(req.getPathInfo());
        Optional<List<Weapon>> result = getUC.execute(id);
        if (result.isEmpty() || result.get().isEmpty()) sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Weapon not found");
        else sendJson(resp, result.get());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        CreateWeaponRequest body = readBody(req, CreateWeaponRequest.class);
        Long id = createUC.execute(body);
        sendCreated(resp, id, "Weapon created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }

        UpdateWeaponRequest body = readBody(req, UpdateWeaponRequest.class);
        UpdateWeaponRequest finalReq = new UpdateWeaponRequest(id, body.name(), body.description(), body.image(), body.damage(), body.type(), body.properties(), body.weight(), body.price());

        sendJson(resp, updateUC.execute(finalReq));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }
        deleteUC.execute(id);
        sendSuccess(resp, "Weapon deleted");
    }
}