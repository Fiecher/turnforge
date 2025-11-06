package com.github.fiecher.turnforge.infrastructure.db.postgres.repositories;

import com.github.fiecher.turnforge.domain.models.Skill;
import com.github.fiecher.turnforge.domain.repositories.SkillRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.PostgresConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresSkillRepository implements SkillRepository {

    private static final String INSERT_SQL =
            "INSERT INTO skills (name, description) VALUES (?, ?) RETURNING id";

    private static final String SELECT_BASE_SQL =
            "SELECT id, name, description, created_at, updated_at FROM skills";

    private static final String SELECT_BY_ID_SQL = SELECT_BASE_SQL + " WHERE id = ?";
    private static final String SELECT_BY_NAME_SQL = SELECT_BASE_SQL + " WHERE name = ?";
    private static final String SELECT_ALL_SQL = SELECT_BASE_SQL;

    private static final String UPDATE_SQL =
            "UPDATE skills SET name=?, description=? WHERE id = ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM skills WHERE id = ?";

    private static final String DELETE_BY_NAME_SQL =
            "DELETE FROM skills WHERE name = ?";

    private static final String EXISTS_BY_NAME_SQL =
            "SELECT EXISTS(SELECT 1 FROM skills WHERE name = ?)";


    private Skill mapResultSetToSkill(ResultSet rs) throws SQLException {
        return new Skill(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description")
        );
    }

    private void setStatementParams(PreparedStatement pstmt, Skill entity) throws SQLException {
        int i = 1;
        pstmt.setString(i++, entity.getName());
        pstmt.setString(i, entity.getDescription());
    }

    @Override
    public Long save(Skill entity) {
        try (Connection conn = PostgresConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setStatementParams(pstmt, entity);

            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving skill failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setID(id);
                    return id;
                } else {
                    throw new SQLException("Saving skill failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Skill save operation.", e);
        }
    }

    @Override
    public Optional<Skill> findByID(Long entityID) {
        try (Connection conn = PostgresConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSkill(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByID operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Skill> findAll() {
        List<Skill> skills = new ArrayList<>();
        try (Connection conn = PostgresConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                skills.add(mapResultSetToSkill(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll operation.", e);
        }
        return skills;
    }

    @Override
    public Skill update(Skill entity) {
        if (entity.getID() == null) {
            throw new IllegalArgumentException("Entity ID must not be null for update operation.");
        }

        try (Connection conn = PostgresConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            setStatementParams(pstmt, entity);
            pstmt.setLong(3, entity.getID());

            if (pstmt.executeUpdate() == 0) {
                throw new RuntimeException("Update failed, Skill with ID " + entity.getID() + " not found.");
            }
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Skill update operation.", e);
        }
    }

    @Override
    public void deleteByID(Long entityID) {
        try (Connection conn = PostgresConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error during deleteByID operation.", e);
        }
    }

    @Override
    public Optional<Skill> findByName(String name) {
        try (Connection conn = PostgresConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME_SQL)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSkill(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByName operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteByName(String name) {
        try (Connection conn = PostgresConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BY_NAME_SQL)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error during deleteByName operation.", e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        try (Connection conn = PostgresConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(EXISTS_BY_NAME_SQL)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during existsByName operation.", e);
        }
    }
}