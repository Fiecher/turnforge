package com.github.fiecher.turnforge.infrastructure.db.postgres.repositories;

import com.github.fiecher.turnforge.domain.models.User;
import com.github.fiecher.turnforge.domain.models.UserRole;
import com.github.fiecher.turnforge.domain.repositories.UserRepository;
import com.github.fiecher.turnforge.infrastructure.db.postgres.PostgresConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresUserRepository implements UserRepository {

    private final PostgresConnectionManager connectionManager;

    public PostgresUserRepository(PostgresConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    
    private static final String INSERT_SQL =
            "INSERT INTO users (password_hash, role, login) " +
                    "VALUES (?, ?::user_role, ?) RETURNING id";

    private static final String SELECT_BASE_SQL =
            "SELECT id, password_hash, role, login, created_at, updated_at FROM users";

    private static final String SELECT_BY_ID_SQL = SELECT_BASE_SQL + " WHERE id = ?";
    private static final String SELECT_BY_LOGIN_SQL = SELECT_BASE_SQL + " WHERE login = ?";
    private static final String SELECT_ALL_SQL = SELECT_BASE_SQL;

    private static final String UPDATE_SQL =
            "UPDATE users SET password_hash=?, role=?::user_role, login=? WHERE id = ?";

    private static final String DELETE_BY_ID_SQL =
            "DELETE FROM users WHERE id = ?";

    private static final String DELETE_BY_LOGIN_SQL =
            "DELETE FROM users WHERE login = ?";

    private static final String EXIST_BY_LOGIN_SQL =
            "SELECT EXISTS(SELECT 1 FROM users WHERE login = ?)";


    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String roleString = rs.getString("role");
        UserRole role = UserRole.valueOf(roleString.toUpperCase());

        return new User(
                rs.getLong("id"),
                rs.getString("login"),
                rs.getString("password_hash"),
                role
        );
    }

    private void setStatementParams(PreparedStatement pstmt, User entity) throws SQLException {
        int i = 1;
        pstmt.setString(i++, entity.getPasswordHash());

        pstmt.setString(i++, entity.getRole().name().toLowerCase());

        pstmt.setString(i, entity.getLogin());
    }

    @Override
    public Long save(User entity) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setStatementParams(pstmt, entity);

            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Saving user failed, no rows affected.");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setID(id);
                    return id;
                } else {
                    throw new SQLException("Saving user failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during User save operation.", e);
        }
    }

    @Override
    public Optional<User> findByID(Long entityID) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setLong(1, entityID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByID operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findAll operation.", e);
        }
        return users;
    }

    @Override
    public User update(User entity) {
        if (entity.getID() == null) {
            throw new IllegalArgumentException("Entity ID must not be null for update operation.");
        }

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            setStatementParams(pstmt, entity);
            pstmt.setLong(4, entity.getID());

            if (pstmt.executeUpdate() == 0) {
                throw new RuntimeException("Update failed, User with ID " + entity.getID() + " not found.");
            }
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Database error during User update operation.", e);
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
    public Optional<User> findByLogin(String login) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_LOGIN_SQL)) {

            pstmt.setString(1, login);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during findByLogin operation.", e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteByLogin(String login) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BY_LOGIN_SQL)) {

            pstmt.setString(1, login);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error during deleteByLogin operation.", e);
        }
    }

    @Override
    public boolean existByLogin(String login) {
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(EXIST_BY_LOGIN_SQL)) {

            pstmt.setString(1, login);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during existByLogin operation.", e);
        }
    }
}
