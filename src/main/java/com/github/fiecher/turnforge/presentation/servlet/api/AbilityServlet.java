package com.github.fiecher.turnforge.presentation.servlet.api;

import com.github.fiecher.turnforge.app.dtos.requests.CreateAbilityRequest;
import com.github.fiecher.turnforge.app.dtos.requests.UpdateAbilityRequest;
import com.github.fiecher.turnforge.app.usecase.ability.CreateAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.DeleteAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.GetAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.UpdateAbilityUseCase;
import com.github.fiecher.turnforge.domain.models.Ability;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AbilityServlet extends BaseServlet {

    private static CreateAbilityUseCase createAbilityUseCase;
    private static GetAbilityUseCase getAbilityUseCase;
    private static UpdateAbilityUseCase updateAbilityUseCase;
    private static DeleteAbilityUseCase deleteAbilityUseCase;

    public static void setDependencies(
            CreateAbilityUseCase c,
            GetAbilityUseCase g,
            UpdateAbilityUseCase u,
            DeleteAbilityUseCase d) {
        createAbilityUseCase = Objects.requireNonNull(c);
        getAbilityUseCase = Objects.requireNonNull(g);
        updateAbilityUseCase = Objects.requireNonNull(u);
        deleteAbilityUseCase = Objects.requireNonNull(d);
    }

    private boolean checkDependencies(HttpServletResponse resp) throws IOException {
        if (createAbilityUseCase == null || getAbilityUseCase == null || updateAbilityUseCase == null || deleteAbilityUseCase == null) {
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ability use cases are not initialized.");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkDependencies(resp)) return;

        try {
            Long abilityIdFromPath = extractIdFromPath(req.getPathInfo());
            Optional<List<Ability>> result = getAbilityUseCase.execute(abilityIdFromPath);

            if (result.isEmpty() || result.get().isEmpty()) {
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Ability not found.");
                return;
            }
            sendJson(resp, result.get());

        } catch (Exception e) {
            System.err.println("GET Ability Error: " + e.getMessage());
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkDependencies(resp)) return;

        try {
            getAuthUserId(req);

            CreateAbilityRequest requestBody = readBody(req, CreateAbilityRequest.class);

            Long newId = createAbilityUseCase.execute(requestBody);

            sendCreated(resp, newId, "Ability created successfully.");

        } catch (IllegalStateException e) {
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required to create resources.");
        } catch (IOException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request format or missing data.");
        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (Exception e) {
            System.err.println("POST Ability Error: " + e.getMessage());
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkDependencies(resp)) return;

        try {
            getAuthUserId(req);

            Long abilityId = extractIdFromPath(req.getPathInfo());

            if (abilityId == null) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ability ID is required in the path for update.");
                return;
            }

            UpdateAbilityRequest requestBody = readBody(req, UpdateAbilityRequest.class);

            UpdateAbilityRequest finalRequest = new UpdateAbilityRequest(
                    abilityId,
                    requestBody.name(),
                    requestBody.description(),
                    requestBody.image(),
                    requestBody.damage(),
                    requestBody.type(),
                    requestBody.level(),
                    requestBody.time(),
                    requestBody.range(),
                    requestBody.components(),
                    requestBody.duration()
            );

            Ability updatedAbility = updateAbilityUseCase.execute(finalRequest);

            sendJson(resp, updatedAbility);

        } catch (IllegalStateException e) {
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required to update resources.");
        } catch (IOException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request format or missing data.");
        } catch (Exception e) {
            System.err.println("PUT Ability Error: " + e.getMessage());
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkDependencies(resp)) return;

        try {
            getAuthUserId(req);

            Long abilityId = extractIdFromPath(req.getPathInfo());

            if (abilityId == null) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Ability ID is required in the path for DELETE operation.");
                return;
            }

            deleteAbilityUseCase.execute(abilityId);

            sendSuccess(resp, "Ability deleted successfully.");

        } catch (IllegalStateException e) {
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required to delete resources.");
        } catch (Exception e) {
            System.err.println("DELETE Ability Error: " + e.getMessage());
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }
}