package com.ride.share.aad.storage.entity;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.storage.dao.AbstractCassandraDAO;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class Token {

    public static final String TOKENS_TABLE = "tokens";

    static {
        TokensDAO tokensDAO = new TokensDAO();
        tokensDAO.createTable();
    }

    transient TokensDAO tokensDAO;
    private String userId;
    private String jwtToken;
    private UserRole userRole;
    private long expirationTime; // Expiration time in milliseconds

    public Token() {
        this.tokensDAO = new TokensDAO();
    }

    public Token(String userId) {
        this();
        this.userId = userId;
        tokensDAO.mapToEntity(userId, this);
    }

    public Token(String userId, String jwtToken, UserRole userRole, long expirationTime) {
        this();
        this.userId = userId;
        this.jwtToken = jwtToken;
        this.userRole = userRole;
        this.expirationTime = expirationTime;
    }

    public static List<Token> getAllTokens() {
        TokensDAO tokensDAO = new TokensDAO();
        return tokensDAO.getAllTokens();
    }

    public static Token getToken(JSONObject tokenJson) throws Exception {
        String userId = tokenJson.getString("userId");
        return new Token(userId,
                tokenJson.getString("jwtToken"),
                UserRole.valueOf(tokenJson.getString("userRole")),
                System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3)); // Expiration time set to 3 hours from now
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("userId", this.userId);
        json.put("jwtToken", this.jwtToken);
        json.put("userRole", this.userRole.toString());
        json.put("expirationTime", this.expirationTime);
        return json;
    }

    public Token save() {
        tokensDAO.insert(userId, jwtToken, userRole.toString(), expirationTime);
        return this;
    }

    public Token delete() {
        tokensDAO.delete(userId);
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public enum UserRole {
        Admin, User
    }

    public static class TokensDAO extends AbstractCassandraDAO<Token> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " + TOKENS_TABLE + " " + "(userId TEXT PRIMARY KEY, jwtToken TEXT, userRole TEXT, expirationTime BIGINT)");

        public static PreparedStatement INSERT_STMT;

        public static PreparedStatement DELETE_STMT;

        public static PreparedStatement SELECT_STMT;

        @Override
        public PreparedStatement getCreateStmt() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStmt() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + TOKENS_TABLE + " (userId, jwtToken, userRole, expirationTime) VALUES (?, ?, ?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStmt() {
            return null;
        }

        @Override
        public PreparedStatement getDeleteStmt() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + TOKENS_TABLE + " WHERE userId = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStmt() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + TOKENS_TABLE + " WHERE userId = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Token mapToEntity(String key, Token token) {
            ResultSet boundStatement = get(key);
            if (boundStatement != null) {
                Row row = boundStatement.one();
                if (row != null) {
                    if (token == null) {
                        token = new Token();
                    }
                    token.userId = row.getString("userId");
                    token.jwtToken = row.getString("jwtToken");
                    token.userRole = UserRole.valueOf(row.getString("userRole"));
                    token.expirationTime = row.getLong("expirationTime");
                    return token;
                }
            }
            return null;
        }

        public List<Token> getAllTokens() {
            List<Token> tokenList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + TOKENS_TABLE).bind();
            getCqlSession().execute(boundStatement).forEach(row -> {
                Token token = new Token();
                token.userId = row.getString("userId");
                token.jwtToken = row.getString("jwtToken");
                token.userRole = UserRole.valueOf(row.getString("userRole"));
                token.expirationTime = row.getLong("expirationTime");
                tokenList.add(token);
            });
            return tokenList;
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Token.class.getSimpleName() + "[", "]")
                .add("userId='" + userId + "'")
                .add("jwtToken='" + jwtToken + "'")
                .add("userRole=" + userRole)
                .add("expirationTime=" + expirationTime)
                .toString();
    }
}
