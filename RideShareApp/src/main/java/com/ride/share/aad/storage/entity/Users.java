package com.ride.share.aad.storage.entity;


import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.storage.dao.AbstractCassandraDAO;

import java.util.ArrayList;
import java.util.List;

public class Users {

    public static final String USERS_TABLE = "users";

    static {
        UsersDAO usersDAO = new UsersDAO();
        usersDAO.createTable();
    }

    private String userId;
    private String name;
    private String emailId;
    private Role role;
    private long lastSeen;

    enum Role {
        Rider,
        Driver
    }

    public static class UsersDAO extends AbstractCassandraDAO<Users> {

        @Override
        public PreparedStatement getCreateStmt() {
            return getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " (userId TEXT PRIMARY KEY, name TEXT, emailId TEXT, role TEXT, lastSeen BIGINT)");
        }

        @Override
        public PreparedStatement getInsertStmt() {
            return getCqlSession().prepare("INSERT INTO " + USERS_TABLE + " (userId, name, emailId, role, lastSeen) VALUES (?, ?, ?, ?, ?)");
        }

        @Override
        public PreparedStatement getUpdateStmt() {
            return getCqlSession().prepare("UPDATE " + USERS_TABLE + " SET name = ?, emailId = ?, role = ?, lastSeen = ? WHERE userId = ?");
        }

        @Override
        public PreparedStatement getDeleteStmt() {
            return getCqlSession().prepare("DELETE FROM " + USERS_TABLE + " WHERE userId = ?");
        }

        @Override
        public PreparedStatement getStmt() {
            return getCqlSession().prepare("SELECT * FROM " + USERS_TABLE + " WHERE userId = ?");
        }

        @Override
        public Users mapToEntity(String key) {
            Row row = get(key).one();

            if (row != null) {
                Users user = new Users();
                user.userId = row.getString("userId");
                user.name = row.getString("name");
                user.emailId = row.getString("emailId");
                user.role = Role.valueOf(row.getString("role"));
                user.lastSeen = row.getLong("lastSeen");
                return user;
            }
            return null;
        }
        public List<Users> getAllUsers() {
            List<Users> userList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + USERS_TABLE).bind();
            getCqlSession().execute(boundStatement).forEach(row -> {
                Users user = new Users();
                user.userId = row.getString("userId");
                user.name = row.getString("name");
                user.emailId = row.getString("emailId");
                user.role = Role.valueOf(row.getString("role"));
                user.lastSeen = row.getLong("lastSeen");
                userList.add(user);
            });
            return userList;
        }
    }

    // Static method to get all users
    public static List<Users> getAllUsers() {
        UsersDAO usersDAO = new UsersDAO();
        return usersDAO.getAllUsers();
    }
}
