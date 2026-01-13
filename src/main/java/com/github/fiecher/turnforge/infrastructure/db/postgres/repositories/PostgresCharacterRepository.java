package com.github.fiecher.turnforge.infrastructure.db.postgres.repositories;

import com.github.fiecher.turnforge.app.dtos.requests.UpdateCharacterRequest;
import com.github.fiecher.turnforge.domain.models.Character;
import com.github.fiecher.turnforge.domain.models.SizeType;
import com.github.fiecher.turnforge.domain.repositories.CharacterRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.PostgresConnectionManager;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class PostgresCharacterRepository implements CharacterRepository {

    private final PostgresConnectionManager connectionManager;

    public PostgresCharacterRepository(PostgresConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private static final String INSERT_SQL =
            "INSERT INTO characters (user_id, name, level, strength, dexterity, constitution, intelligence, wisdom, charisma, description, image, class, subclass, background, race, age, size, spellcasting_ability, money) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?::size_type, ?, ?) RETURNING id";

    private static final String SELECT_BASE_SQL =
            "SELECT id, user_id, name, level, strength, dexterity, constitution, intelligence, wisdom, charisma, description, image, class, subclass, background, race, age, size, spellcasting_ability, money, created_at, updated_at " +
                    "FROM characters";

    private static final String SELECT_BY_ID_SQL = SELECT_BASE_SQL + " WHERE id = ?";
    private static final String SELECT_BY_USER_ID_SQL = SELECT_BASE_SQL + " WHERE user_id = ?";
    private static final String SELECT_BY_NAME_AND_USER_ID_SQL = SELECT_BASE_SQL + " WHERE name = ? AND user_id = ?";
    private static final String SELECT_ALL_SQL = SELECT_BASE_SQL;
    private static final String UPDATE_SQL =
            "UPDATE characters SET user_id=?, name=?, level=?, dexterity=?, constitution=?, intelligence=?, wisdom=?, charisma=?, description=?, image=?, class=?, subclass=?, background=?, race=?, age=?, size=?::size_type, spellcasting_ability=?, money=? " +
                    "WHERE id = ?";
    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM characters WHERE id = ?";
    private static final String EXISTS_BY_NAME_AND_USER_ID_SQL =
            "SELECT EXISTS(SELECT 1 FROM characters WHERE name = ? AND user_id = ?)";

    private Character mapResultSetToCharacter(ResultSet rs) throws SQLException {
        Character character = new Character(
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getString("class")
        );

        character.setID(rs.getLong("id"));
        character.setLevel((rs.getObject("level") != null) ? rs.getShort("level") : 1);
        character.setStrength((rs.getObject("strength") != null) ? rs.getShort("strength") : 0);
        character.setDexterity((rs.getObject("dexterity") != null) ? rs.getShort("dexterity") : 0);
        character.setConstitution((rs.getObject("constitution") != null) ? rs.getShort("constitution") : 0);
        character.setIntelligence((rs.getObject("intelligence") != null) ? rs.getShort("intelligence") : 0);
        character.setWisdom((rs.getObject("wisdom") != null) ? rs.getShort("wisdom") : 0);
        character.setCharisma((rs.getObject("charisma") != null) ? rs.getShort("charisma") : 0);
        character.setDescription(rs.getString("description"));
        character.setImage(rs.getString("image"));
        character.setSubclass(rs.getString("subclass"));
        character.setBackground(rs.getString("background"));
        character.setRace(rs.getString("race"));

        Integer age = (rs.getObject("age") != null) ? rs.getInt("age") : null;
        character.setAge(age);

        String sizeString = rs.getString("size");
        if (sizeString != null) {
            character.setSize(SizeType.valueOf(sizeString.toUpperCase()));
        }

        character.setSpellcastingAbility(rs.getString("spellcasting_ability"));
        character.setMoney((rs.getObject("money") != null) ? rs.getInt("money") : 0);

        return character;
    }

    private void setStatementParams(PreparedStatement pstmt, Character entity) throws SQLException {
        int i = 1;
        pstmt.setLong(i++, entity.getUserID());
        pstmt.setString(i++, entity.getName());
        pstmt.setShort(i++, entity.getLevel());
        pstmt.setShort(i++, entity.getStrength());
        pstmt.setShort(i++, entity.getDexterity());
        pstmt.setShort(i++, entity.getConstitution());
        pstmt.setShort(i++, entity.getIntelligence());
        pstmt.setShort(i++, entity.getWisdom());
        pstmt.setShort(i++, entity.getCharisma());
        pstmt.setString(i++, entity.getDescription());
        pstmt.setString(i++, entity.getImage());
        pstmt.setString(i++, entity.getClass_());
        pstmt.setString(i++, entity.getSubclass());
        pstmt.setString(i++, entity.getBackground());
        pstmt.setString(i++, entity.getRace());

        if (entity.getAge() != null) {
            pstmt.setInt(i++, entity.getAge());
        } else {
            pstmt.setNull(i++, Types.INTEGER);
        }

        if (entity.getSize() != null) {
            pstmt.setString(i++, entity.getSize().toString());
        } else {
            pstmt.setNull(i++, Types.VARCHAR);
        }

        pstmt.setString(i++, entity.getSpellcastingAbility());
        pstmt.setInt(i++, entity.getMoney());
    }

    private short safeShort(Integer value, short defaultValue) {
        return value != null ? value.shortValue() : defaultValue;
    }

    private int safeInt(Integer value, int defaultValue) {
        return value != null ? value : defaultValue;
    }

    @Override
    public Long save(Character entity) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setStatementParams(pstmt, entity);

            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving character failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setID(id);
                    return id;
                } else {
                    throw new SQLException("Saving character failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Character save operation.", e);
        }
    }

    @Override
    public Optional<Character> findByID(Long entityID) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCharacter(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByID operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Character> findAll() {
        List<Character> characters = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                characters.add(mapResultSetToCharacter(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll operation.", e);
        }
        return characters;
    }

    @Override
    public Character update(Character entity) {
        if (entity.getID() == null) {
            throw new IllegalArgumentException("Entity ID must not be null for update operation.");
        }

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            setStatementParams(pstmt, entity);
            pstmt.setLong(20, entity.getID());

            if (pstmt.executeUpdate() == 0) {
                throw new RuntimeException("Update failed, Character with ID " + entity.getID() + " not found.");
            }
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Database error during Character base update operation.", e);
        }
    }

    @Override
    public void updateFullCharacter(UpdateCharacterRequest request) {
        Connection conn = null;

        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);

            updateBaseFields(conn, request);
            Long characterId = request.characterID();

            updateRelationsPartial(conn, characterId, request.weapons(), "characters_weapons", "weapon_id");
            updateRelationsPartial(conn, characterId, request.armor(), "characters_armor", "armor_id");
            updateRelationsPartial(conn, characterId, request.items(), "characters_items", "item_id");
            updateRelationsPartial(conn, characterId, request.abilities(), "characters_abilities", "ability_id");
            updateRelationsPartial(conn, characterId, request.traits(), "characters_traits", "trait_id");
            updateRelationsPartial(conn, characterId, request.custom_skills(), "characters_skills", "skill_id");

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Database error during full Character update operation.", e);
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
    }

    private void updateBaseFields(Connection conn, UpdateCharacterRequest request) throws SQLException {
        final String SQL =
                "UPDATE characters SET name=?, level=?, strength=?, dexterity=?, constitution=?, intelligence=?, wisdom=?, charisma=?, description=?, class=?, subclass=?, background=?, race=?, age=?, size=?::size_type, spellcasting_ability=?, money=? " +
                        "WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            int i = 1;
            pstmt.setString(i++, request.name());
            pstmt.setShort(i++, safeShort(request.level(), (short)1));
            pstmt.setShort(i++, safeShort(request.strength(), (short)0));
            pstmt.setShort(i++, safeShort(request.dexterity(), (short)0));
            pstmt.setShort(i++, safeShort(request.constitution(), (short)0));
            pstmt.setShort(i++, safeShort(request.intelligence(), (short)0));
            pstmt.setShort(i++, safeShort(request.wisdom(), (short)0));
            pstmt.setShort(i++, safeShort(request.charisma(), (short)0));
            pstmt.setString(i++, request.description());
            pstmt.setString(i++, request.characterClass());
            pstmt.setString(i++, request.subclass());
            pstmt.setString(i++, request.background());
            pstmt.setString(i++, request.race());

            if (request.age() != null) {
                pstmt.setInt(i++, request.age());
            } else {
                pstmt.setNull(i++, Types.INTEGER);
            }

            if (request.size() != null) {
                pstmt.setString(i++, request.size().toString());
            } else {
                pstmt.setNull(i++, Types.VARCHAR);
            }

            pstmt.setString(i++, request.spellcasting_ability());
            pstmt.setInt(i++, safeInt(request.money(), 0));
            pstmt.setLong(i, request.characterID());

            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Update of character base fields failed. Character ID " + request.characterID() + " not found.");
            }
        }
    }

    private Set<Long> getCurrentRelationIds(Connection conn, Long characterId, String tableName, String idColumn) throws SQLException {
        String sql = String.format("SELECT %s FROM %s WHERE character_id = ?", idColumn, tableName);
        Set<Long> ids = new HashSet<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, characterId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getLong(1));
                }
            }
        }
        return ids;
    }

    private void updateRelationsPartial(Connection conn, Long characterId, List<Long> newIds, String tableName, String idColumn) throws SQLException {
        if (newIds == null) return;

        Set<Long> currentIds = getCurrentRelationIds(conn, characterId, tableName, idColumn);
        Set<Long> newIdsSet = new HashSet<>(newIds);

        Set<Long> toDelete = new HashSet<>(currentIds);
        toDelete.removeAll(newIdsSet);

        Set<Long> toAdd = new HashSet<>(newIdsSet);
        toAdd.removeAll(currentIds);

        if (!toDelete.isEmpty()) {
            String placeholders = toDelete.stream().map(id -> "?").collect(Collectors.joining(","));
            String sql = String.format(
                    "DELETE FROM %s WHERE character_id = ? AND %s IN (%s)",
                    tableName, idColumn, placeholders
            );
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, characterId);
                int idx = 2;
                for (Long id : toDelete) {
                    pstmt.setLong(idx++, id);
                }
                pstmt.executeUpdate();
            }
        }

        if (!toAdd.isEmpty()) {
            String sql = String.format(
                    "INSERT INTO %s (character_id, %s) VALUES (?, ?)",
                    tableName, idColumn
            );
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Long id : toAdd) {
                    pstmt.setLong(1, characterId);
                    pstmt.setLong(2, id);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
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
    public List<Character> findAllByUserID(Long userID) {
        List<Character> characters = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_USER_ID_SQL)) {

            pstmt.setLong(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    characters.add(mapResultSetToCharacter(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByUserID operation.", e);
        }
        return characters;
    }

    @Override
    public Optional<Character> findByUserIDAndName(Long userID, String name) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NAME_AND_USER_ID_SQL)) {

            pstmt.setString(1, name);
            pstmt.setLong(2, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCharacter(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByNameAndUserID operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByUserIDAndName(Long userID, String name) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(EXISTS_BY_NAME_AND_USER_ID_SQL)) {

            pstmt.setString(1, name);
            pstmt.setLong(2, userID);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during existsByNameAndUserID operation.", e);
        }
    }
}