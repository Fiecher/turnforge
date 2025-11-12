package com.github.fiecher.turnforge.infrastructure.db.postgres.repositories;

import com.github.fiecher.turnforge.domain.models.Armor;
import com.github.fiecher.turnforge.domain.repositories.ArmorRepository;
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

public class PostgresArmorRepository implements ArmorRepository {

    private final PostgresConnectionManager connectionManager;

    public PostgresArmorRepository(PostgresConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private static final String SELECT_FIELDS =
            "id, name, description, image, ac, armor_type, weight, price, created_at, updated_at";

    private static final String INSERT_SQL =
            "INSERT INTO armor (name, description, image, ac, armor_type, weight, price) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

    private static final String SELECT_BY_ID_SQL =
            "SELECT " + SELECT_FIELDS + " FROM armor WHERE id = ?";

    private static final String SELECT_BY_NAME_SQL =
            "SELECT " + SELECT_FIELDS + " FROM armor WHERE name = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT " + SELECT_FIELDS + " FROM armor";

    private static final String UPDATE_SQL =
            "UPDATE armor SET name=?, description=?, image=?, ac=?, armor_type=?, weight=?, price=? " +
                    "WHERE id = ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM armor WHERE id = ?";

    private static final String DELETE_BY_NAME_SQL =
            "DELETE FROM armor WHERE name = ?";

    private static final String EXISTS_BY_NAME_SQL =
            "SELECT EXISTS(SELECT 1 FROM armor WHERE name = ?)";


    private Armor mapResultSetToArmor(ResultSet rs) throws SQLException {
        Armor armor = new Armor();
        armor.setID(rs.getLong("id"));
        armor.setName(rs.getString("name"));
        armor.setDescription(rs.getString("description"));
        armor.setImage(rs.getString("image"));
        armor.setAC(rs.getShort("ac"));

        armor.setType(rs.getString("armor_type"));

        double weight = rs.getDouble("weight");
        armor.setWeight(rs.wasNull() ? null : weight);

        int price = rs.getInt("price");
        armor.setPrice(rs.wasNull() ? null : price);

        return armor;
    }


    private void setStatementParams(PreparedStatement pstmt, Armor entity) throws SQLException {
        int i = 1;
        pstmt.setString(i++, entity.getName());
        pstmt.setString(i++, entity.getDescription());
        pstmt.setString(i++, entity.getImage());
        pstmt.setShort(i++, entity.getAC());

        pstmt.setString(i++, entity.getType());

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
    public Long save(Armor entity) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setStatementParams(pstmt, entity);

            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving armor failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setID(id);
                    return id;
                } else {
                    throw new SQLException("Saving armor failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Armor save operation.", e);
        }
    }

    @Override
    public Optional<Armor> findByID(Long entityID) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToArmor(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByID operation.", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Armor> findAll() {
        List<Armor> armorList = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                armorList.add(mapResultSetToArmor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll operation.", e);
        }

        return armorList;
    }

    @Override
    public Armor update(Armor entity) {
        if (entity.getID() == null) {
            throw new IllegalArgumentException("Entity ID must not be null for update operation.");
        }
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            setStatementParams(pstmt, entity);
            pstmt.setLong(8, entity.getID());

            if (pstmt.executeUpdate() == 0) {
                throw new RuntimeException("Update failed, Armor with ID " + entity.getID() + " not found.");
            }
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Armor update operation.", e);
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
    public Optional<Armor> findByName(String name) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME_SQL)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToArmor(rs));
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

