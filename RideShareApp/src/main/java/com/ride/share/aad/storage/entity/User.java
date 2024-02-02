package com.ride.share.aad.storage.entity;


import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.storage.dao.AbstractCassandraDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class User {

    public static final String USERS_TABLE = "users";

    static {
        UsersDAO usersDAO = new UsersDAO();
        usersDAO.createTable();
    }

    transient UsersDAO usersDAO;
    private String userId;
    private String name;
    private String emailId;
    private Role role;
    private long lastSeen;

    public User() {
        this.usersDAO = new UsersDAO();
    }

    public User(String userId, String name, String emailId, Role role, long lastSeen) {
        super();
        this.userId = userId;
        this.name = name;
        this.emailId = emailId;
        this.role = role;
        this.lastSeen = lastSeen;
    }

    public static List<User> getAllUsers() {
        UsersDAO usersDAO = new UsersDAO();
        return usersDAO.getAllUsers();
    }

    public static void main(String[] args) {
        User user = new User();
        user.setUserId("123");
        user.setName("John Doe");
        user.setEmailId("john@example.com");
        user.setRole(User.Role.Rider);
        user.setLastSeen(System.currentTimeMillis());

        // Saving the Users object
        user.save();

        for (User users : getAllUsers()) {
            System.out.println(users);
        }
    }

    public User save() {
        usersDAO.insert(userId, userId, name, emailId, role.toString(), lastSeen);
        return this;
    }

    public User delete() {
        usersDAO.delete(userId);
        return this;
    }

    public User update() {
        usersDAO.update(userId, userId, name, emailId, role.toString(), lastSeen);
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    enum Role {
        Rider, Driver
    }

    public static class UsersDAO extends AbstractCassandraDAO<User> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " " + "(userId TEXT PRIMARY KEY, name TEXT, emailId TEXT, role TEXT, lastSeen BIGINT)");
        public static PreparedStatement INSERT_STMT;

        public static PreparedStatement UPDATE_STMT;

        public static PreparedStatement DELETE_STMT;

        public static PreparedStatement SELECT_STMT;

        @Override
        public PreparedStatement getCreateStmt() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStmt() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + USERS_TABLE + " (userId, name, emailId, role, lastSeen) VALUES (?, ?, ?, ?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStmt() {
            if (UPDATE_STMT == null) {
                UPDATE_STMT = getCqlSession().prepare("UPDATE " + USERS_TABLE + " SET name = ?, emailId = ?, role = ?, lastSeen = ? WHERE userId = ?");
            }
            return UPDATE_STMT;
        }

        @Override
        public PreparedStatement getDeleteStmt() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + USERS_TABLE + " WHERE userId = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStmt() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + USERS_TABLE + " WHERE userId = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public User mapToEntity(String key) {
            Row row = get(key).one();

            if (row != null) {
                User user = new User();
                user.userId = row.getString("userId");
                user.name = row.getString("name");
                user.emailId = row.getString("emailId");
                user.role = Role.valueOf(row.getString("role"));
                user.lastSeen = row.getLong("lastSeen");
                return user;
            }
            return null;
        }

        public List<User> getAllUsers() {
            List<User> userList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + USERS_TABLE).bind();
            getCqlSession().execute(boundStatement).forEach(row -> {
                User user = new User();
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

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("userId='" + userId + "'")
                .add("name='" + name + "'")
                .add("emailId='" + emailId + "'")
                .add("role=" + role)
                .add("lastSeen=" + lastSeen)
                .toString();
    }
}
