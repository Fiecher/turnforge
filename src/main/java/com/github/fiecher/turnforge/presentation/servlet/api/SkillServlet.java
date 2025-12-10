package com.github.fiecher.turnforge.presentation.servlet.api;

import com.github.fiecher.turnforge.app.dtos.requests.CreateSkillRequest;
import com.github.fiecher.turnforge.app.dtos.requests.UpdateSkillRequest;
import com.github.fiecher.turnforge.app.usecase.skill.*;
import com.github.fiecher.turnforge.domain.models.Skill;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SkillServlet extends BaseServlet {
    private static CreateSkillUseCase createUC;
    private static GetSkillUseCase getUC;
    private static UpdateSkillUseCase updateUC;
    private static DeleteSkillUseCase deleteUC;

    public static void setDependencies(CreateSkillUseCase c, GetSkillUseCase g, UpdateSkillUseCase u, DeleteSkillUseCase d) {
        createUC = Objects.requireNonNull(c); getUC = Objects.requireNonNull(g); updateUC = Objects.requireNonNull(u); deleteUC = Objects.requireNonNull(d);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractIdFromPath(req.getPathInfo());
        Optional<List<Skill>> result = getUC.execute(id);
        if (result.isEmpty() || result.get().isEmpty()) sendError(resp, 404, "Skill not found");
        else sendJson(resp, result.get());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        CreateSkillRequest body = readBody(req, CreateSkillRequest.class);
        sendCreated(resp, createUC.execute(body), "Skill created");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }

        UpdateSkillRequest body = readBody(req, UpdateSkillRequest.class);
        UpdateSkillRequest finalReq = new UpdateSkillRequest(id, body.name(), body.description());
        sendJson(resp, updateUC.execute(finalReq));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getAuthUserId(req);
        Long id = extractIdFromPath(req.getPathInfo());
        if (id == null) { sendError(resp, 400, "ID required"); return; }
        deleteUC.execute(id);
        sendSuccess(resp, "Skill deleted");
    }
}