package com.ride.share.aad.storage.dao;

import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

public interface DataAccessObject<T> {
    // prepared statements
    public PreparedStatement getCreateStmt();
    public PreparedStatement getInsertStmt();
    public PreparedStatement getUpdateStmt();
    public PreparedStatement getDeleteStmt();
    public PreparedStatement getStmt();

    // process statements
    public ResultSet createTable();
    public ResultSet insert(String key, Object... values);
    public ResultSet get(String key);
    public ResultSet update(String key, Object... values);
    public ResultSet delete(String key);
    public T mapToEntity(String key, T entity);
}
