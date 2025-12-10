package com.github.fiecher.turnforge.infrastructure.db.postgres.repositories;

import com.github.fiecher.turnforge.domain.models.Trait;
import com.github.fiecher.turnforge.domain.repositories.TraitRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.PostgresConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresTraitRepository implements TraitRepository {

    private final PostgresConnectionManager connectionManager;

    public PostgresTraitRepository(PostgresConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    private static final String INSERT_SQL =
            "INSERT INTO traits (name, description, image, prerequisites, trait_type) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id";

    private static final String SELECT_BASE_SQL =
            "SELECT id, name, description, image, prerequisites, trait_type, created_at, updated_at FROM traits";

    private static final String SELECT_BY_ID_SQL = SELECT_BASE_SQL + " WHERE id = ?";
    private static final String SELECT_BY_NAME_SQL = SELECT_BASE_SQL + " WHERE name = ?";
    private static final String SELECT_ALL_SQL = SELECT_BASE_SQL;

    private static final String UPDATE_SQL =
            "UPDATE traits SET name=?, description=?, image=?, prerequisites=?, trait_type=? WHERE id = ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM traits WHERE id = ?";

    private static final String DELETE_BY_NAME_SQL =
            "DELETE FROM traits WHERE name = ?";

    private static final String EXISTS_BY_NAME_SQL =
            "SELECT EXISTS(SELECT 1 FROM traits WHERE name = ?)";


    private Trait mapResultSetToTrait(ResultSet rs) throws SQLException {
        return new Trait(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("image"),
                rs.getString("prerequisites"),
                rs.getString("trait_type")
        );
    }

    private void setStatementParams(PreparedStatement pstmt, Trait entity) throws SQLException {
        int i = 1;
        pstmt.setString(i++, entity.getName());
        pstmt.setString(i++, entity.getDescription());
        pstmt.setString(i++, entity.getImage());
        pstmt.setString(i++, entity.getPrerequisites());
        pstmt.setString(i, entity.getTrait_type());
    }


    @Override
    public Long save(Trait entity) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setStatementParams(pstmt, entity);

            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving trait failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setID(id);
                    return id;
                } else {
                    throw new SQLException("Saving trait failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Trait save operation.", e);
        }
    }

    @Override
    public Optional<Trait> findByID(Long entityID) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTrait(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByID operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Trait> findAll() {
        List<Trait> traits = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                traits.add(mapResultSetToTrait(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll operation.", e);
        }
        return traits;
    }

    @Override
    public Trait update(Trait entity) {
        if (entity.getID() == null) {
            throw new IllegalArgumentException("Entity ID must not be null for update operation.");
        }

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            setStatementParams(pstmt, entity);
            pstmt.setLong(6, entity.getID());

            if (pstmt.executeUpdate() == 0) {
                throw new RuntimeException("Update failed, Trait with ID " + entity.getID() + " not found.");
            }
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Trait update operation.", e);
        }
    }

    @Override
    public void deleteByID(Long entityID) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error during deleteByID operation.", e);
        }
    }

    @Override
    public Optional<Trait> findByName(String name) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME_SQL)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTrait(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByName operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteByName(String name) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BY_NAME_SQL)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error during deleteByName operation.", e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(EXISTS_BY_NAME_SQL)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during existsByName operation.", e);
        }
    }

    @Override
    public List<Trait> findAllByCharacterID(Long characterID) {
        final String SQL =
                "SELECT t.id, t.name, t.description, t.image, t.prerequisites, t.trait_type, t.created_at, t.updated_at " +
                        "FROM traits t " +
                        "JOIN characters_traits ct ON t.id = ct.trait_id " +
                        "WHERE ct.character_id = ?";

        List<Trait> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, characterID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToTrait(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error fetching traits for character " + characterID, e);
        }
        return result;
    }

}

