package com.github.fiecher.turnforge.infrastructure.db.postgres.repositories;

import com.github.fiecher.turnforge.domain.models.Item;
import com.github.fiecher.turnforge.domain.repositories.ItemRepository;
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

public class PostgresItemRepository implements ItemRepository {

    private final PostgresConnectionManager connectionManager;

    public PostgresItemRepository(PostgresConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    private static final String INSERT_SQL =
            "INSERT INTO items (name, description, image, weight, price) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id";

    private static final String SELECT_BASE_SQL =
            "SELECT id, name, description, image, weight, price, created_at, updated_at FROM items";

    private static final String SELECT_BY_ID_SQL = SELECT_BASE_SQL + " WHERE id = ?";
    private static final String SELECT_BY_NAME_SQL = SELECT_BASE_SQL + " WHERE name = ?";
    private static final String SELECT_ALL_SQL = SELECT_BASE_SQL;

    private static final String UPDATE_SQL =
            "UPDATE items SET name=?, description=?, image=?, weight=?, price=? " +
                    "WHERE id = ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM items WHERE id = ?";

    private static final String DELETE_BY_NAME_SQL =
            "DELETE FROM items WHERE name = ?";

    private static final String EXISTS_BY_NAME_SQL =
            "SELECT EXISTS(SELECT 1 FROM items WHERE name = ?)";

    private Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("image"),
                null,
                null
        );

        double weight = rs.getDouble("weight");
        item.setWeight(rs.wasNull() ? null : weight);

        int price = rs.getInt("price");
        item.setPrice(rs.wasNull() ? null : price);

        return item;
    }

    private void setStatementParams(PreparedStatement pstmt, Item entity) throws SQLException {
        int i = 1;
        pstmt.setString(i++, entity.getName());
        pstmt.setString(i++, entity.getDescription());
        pstmt.setString(i++, entity.getImage());

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
    public Long save(Item entity) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setStatementParams(pstmt, entity);

            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving item failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setID(id);
                    return id;
                } else {
                    throw new SQLException("Saving item failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Item save operation.", e);
        }
    }

    @Override
    public Optional<Item> findByID(Long entityID) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByID operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll operation.", e);
        }
        return items;
    }

    @Override
    public Item update(Item entity) {
        if (entity.getID() == null) {
            throw new IllegalArgumentException("Entity ID must not be null for update operation.");
        }

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            setStatementParams(pstmt, entity);
            pstmt.setLong(6, entity.getID());

            if (pstmt.executeUpdate() == 0) {
                throw new RuntimeException("Update failed, Item with ID " + entity.getID() + " not found.");
            }
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Item update operation.", e);
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
    public Optional<Item> findByName(String name) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME_SQL)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToItem(rs));
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
    public List<Item> findAllByCharacterID(Long characterID) {
        final String SQL =
                "SELECT i.id, i.name, i.description, i.image, i.weight, i.price, i.created_at, i.updated_at " +
                        "FROM items i " +
                        "JOIN characters_items ci ON i.id = ci.item_id " +
                        "WHERE ci.character_id = ?";

        List<Item> result = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setLong(1, characterID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error fetching items for character " + characterID, e);
        }
        return result;
    }

}