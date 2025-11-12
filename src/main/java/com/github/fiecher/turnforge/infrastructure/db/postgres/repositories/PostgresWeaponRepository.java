package com.github.fiecher.turnforge.infrastructure.db.postgres.repositories;

import com.github.fiecher.turnforge.domain.models.Weapon;
import com.github.fiecher.turnforge.domain.repositories.WeaponRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.PostgresConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresWeaponRepository implements WeaponRepository {

    private final PostgresConnectionManager connectionManager;

    public PostgresWeaponRepository(PostgresConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    private static final String INSERT_SQL =
            "INSERT INTO weapons (name, description, image, damage, type, properties, weight, price) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

    private static final String SELECT_BASE_SQL =
            "SELECT id, name, description, image, damage, type, properties, weight, price, created_at, updated_at FROM weapons";

    private static final String SELECT_BY_ID_SQL = SELECT_BASE_SQL + " WHERE id = ?";
    private static final String SELECT_BY_NAME_SQL = SELECT_BASE_SQL + " WHERE name = ?";
    private static final String SELECT_ALL_SQL = SELECT_BASE_SQL;

    private static final String UPDATE_SQL =
            "UPDATE weapons SET name=?, description=?, image=?, damage=?, type=?, properties=?, weight=?, price=? " +
                    "WHERE id = ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM weapons WHERE id = ?";

    private static final String DELETE_BY_NAME_SQL =
            "DELETE FROM weapons WHERE name = ?";

    private static final String EXISTS_BY_NAME_SQL =
            "SELECT EXISTS(SELECT 1 FROM weapons WHERE name = ?)";


    private Weapon mapResultSetToWeapon(ResultSet rs) throws SQLException {
        Weapon weapon = new Weapon(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("image"),
                rs.getString("damage"),
                rs.getString("type"),
                rs.getString("properties"),
                null,
                null
        );

        double weight = rs.getDouble("weight");
        if (!rs.wasNull()) {
            weapon.setWeight(weight);
        }

        int price = rs.getInt("price");
        if (!rs.wasNull()) {
            weapon.setPrice(price);
        }

        return weapon;
    }

    private void setStatementParams(PreparedStatement pstmt, Weapon entity) throws SQLException {
        int i = 1;
        pstmt.setString(i++, entity.getName());
        pstmt.setString(i++, entity.getDescription());
        pstmt.setString(i++, entity.getImage());
        pstmt.setString(i++, entity.getDamage());
        pstmt.setString(i++, entity.getType());
        pstmt.setString(i++, entity.getProperties());

        if (entity.getWeight() != null) {
            pstmt.setDouble(i++, entity.getWeight());
        } else {
            pstmt.setNull(i++, Types.DOUBLE);
        }

        if (entity.getPrice() != null) {
            pstmt.setInt(i, entity.getPrice());
        } else {
            pstmt.setNull(i, Types.INTEGER);
        }
    }

    @Override
    public Long save(Weapon entity) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setStatementParams(pstmt, entity);

            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving weapon failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setID(id);
                    return id;
                } else {
                    throw new SQLException("Saving weapon failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Weapon save operation.", e);
        }
    }

    @Override
    public Optional<Weapon> findByID(Long entityID) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToWeapon(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByID operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Weapon> findAll() {
        List<Weapon> weapons = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                weapons.add(mapResultSetToWeapon(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll operation.", e);
        }
        return weapons;
    }

    @Override
    public Weapon update(Weapon entity) {
        if (entity.getID() == null) {
            throw new IllegalArgumentException("Entity ID must not be null for update operation.");
        }

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            setStatementParams(pstmt, entity);
            pstmt.setLong(9, entity.getID());

            if (pstmt.executeUpdate() == 0) {
                throw new RuntimeException("Update failed, Weapon with ID " + entity.getID() + " not found.");
            }
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Weapon update operation.", e);
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
    public Optional<Weapon> findByName(String name) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME_SQL)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToWeapon(rs));
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
}

