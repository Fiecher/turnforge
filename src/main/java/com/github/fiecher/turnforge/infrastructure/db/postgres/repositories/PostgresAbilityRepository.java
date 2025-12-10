package com.github.fiecher.turnforge.infrastructure.db.postgres.repositories;

import com.github.fiecher.turnforge.domain.models.Ability;
import com.github.fiecher.turnforge.domain.repositories.AbilityRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.PostgresConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresAbilityRepository implements AbilityRepository {

    private final PostgresConnectionManager connectionManager;

    public PostgresAbilityRepository(PostgresConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private static final String INSERT_SQL =
            "INSERT INTO abilities (name, description, image, damage, ability_type, level, time, range, components, duration) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, name, description, image, damage, ability_type, level, time, range, components, duration, created_at, updated_at " +
                    "FROM abilities WHERE id = ?";

    private static final String SELECT_BY_NAME_SQL =
            "SELECT id, name, description, image, damage, ability_type, level, time, range, components, duration, created_at, updated_at " +
                    "FROM abilities WHERE name = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT id, name, description, image, damage, ability_type, level, time, range, components, duration, created_at, updated_at FROM abilities";

    private static final String UPDATE_SQL =
            "UPDATE abilities SET name=?, description=?, image=?, damage=?, ability_type=?, level=?, time=?, range=?, components=?, duration=? " +
                    "WHERE id = ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM abilities WHERE id = ?";

    private static final String DELETE_BY_NAME_SQL =
            "DELETE FROM abilities WHERE name = ?";

    private static final String EXISTS_BY_NAME_SQL =
            "SELECT EXISTS(SELECT 1 FROM abilities WHERE name = ?)";


    private Ability mapResultSetToAbility(ResultSet rs) throws SQLException {
        Ability ability = new Ability(rs.getString("name"), rs.getString("damage"));
        ability.setID(rs.getLong("id"));
        ability.setDescription(rs.getString("description"));
        ability.setImage(rs.getString("image"));
        ability.setType(rs.getString("ability_type"));
        ability.setLevel(rs.getShort("level"));
        ability.setTime(rs.getString("time"));
        ability.setRange(rs.getString("range"));
        ability.setComponents(rs.getString("components"));
        ability.setDuration(rs.getString("duration"));
        return ability;
    }

    private void setStatementParams(PreparedStatement pstmt, Ability entity) throws SQLException {
        int i = 1;
        pstmt.setString(i++, entity.getName());
        pstmt.setString(i++, entity.getDescription());
        pstmt.setString(i++, entity.getImage());
        pstmt.setString(i++, entity.getDamage());
        pstmt.setString(i++, entity.getType());
        pstmt.setInt(i++, entity.getLevel());
        pstmt.setString(i++, entity.getTime());
        pstmt.setString(i++, entity.getRange());
        pstmt.setString(i++, entity.getComponents());
        pstmt.setString(i, entity.getDuration());
    }

    @Override
    public Long save(Ability entity) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setStatementParams(pstmt, entity);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Saving ability failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setID(id);
                    return id;
                } else {
                    throw new SQLException("Saving ability failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Ability save operation.", e);
        }
    }

    @Override
    public Optional<Ability> findByID(Long entityID) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAbility(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByID operation.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Ability> findAll() {
        List<Ability> abilities = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                abilities.add(mapResultSetToAbility(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll operation.", e);
        }

        return abilities;
    }

    @Override
    public Ability update(Ability entity) {
        if (entity.getID() == null) {
            throw new IllegalArgumentException("Entity ID must not be null for update operation.");
        }

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            int i = 1;
            pstmt.setString(i++, entity.getName());
            pstmt.setString(i++, entity.getDescription());
            pstmt.setString(i++, entity.getImage());
            pstmt.setString(i++, entity.getDamage());
            pstmt.setString(i++, entity.getType());
            pstmt.setInt(i++, entity.getLevel());
            pstmt.setString(i++, entity.getTime());
            pstmt.setString(i++, entity.getRange());
            pstmt.setString(i++, entity.getComponents());
            pstmt.setString(i++, entity.getDuration());

            pstmt.setLong(i, entity.getID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Update failed, Ability with ID " + entity.getID() + " not found.");
            }
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Ability update operation.", e);
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
    public Optional<Ability> findByName(String name) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME_SQL)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAbility(rs));
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
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Database error during existsByName operation.", e);
        }
    }

    @Override
    public List<Ability> findAllByCharacterID(Long characterID) {
        final String SQL =
                "SELECT a.* FROM abilities a " +
                        "JOIN characters_abilities ca ON a.id = ca.ability_id " +
                        "WHERE ca.character_id = ?";

        List<Ability> res = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, characterID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) res.add(mapResultSetToAbility(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

}
