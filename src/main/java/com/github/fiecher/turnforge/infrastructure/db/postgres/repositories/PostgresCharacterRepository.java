package com.github.fiecher.turnforge.infrastructure.db.postgres.repositories;

import com.github.fiecher.turnforge.domain.models.Character;
import com.github.fiecher.turnforge.domain.models.SizeType;
import com.github.fiecher.turnforge.domain.repositories.CharacterRepository;
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
            "UPDATE characters SET user_id=?, name=?, level=?, strength=?, dexterity=?, constitution=?, intelligence=?, wisdom=?, charisma=?, description=?, image=?, class=?, subclass=?, background=?, race=?, age=?, size=?::size_type, spellcasting_ability=?, money=? " +
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
        character.setLevel(rs.getShort("level"));

        character.setStrength(rs.getShort("strength"));
        character.setDexterity(rs.getShort("dexterity"));
        character.setConstitution(rs.getShort("constitution"));
        character.setIntelligence(rs.getShort("intelligence"));
        character.setWisdom(rs.getShort("wisdom"));
        character.setCharisma(rs.getShort("charisma"));

        character.setDescription(rs.getString("description"));
        character.setImage(rs.getString("image"));
        character.setSubclass(rs.getString("subclass"));
        character.setBackground(rs.getString("background"));
        character.setRace(rs.getString("race"));

        int age = rs.getInt("age");
        character.setAge(rs.wasNull() ? null : age);

        String sizeString = rs.getString("size");
        if (sizeString != null) {
            character.setSize(SizeType.valueOf(sizeString.toUpperCase()));
        }

        character.setSpellcastingAbility(rs.getString("spellcasting_ability"));
        character.setMoney(rs.getInt("money"));

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

        pstmt.setString(i++, entity.getSize().toString());

        pstmt.setString(i++, entity.getSpellcastingAbility());
        pstmt.setInt(i, entity.getMoney());
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
            throw new RuntimeException("Database error during Character update operation.", e);
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