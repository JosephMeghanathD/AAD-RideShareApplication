package com.ride.share.aad.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tokens")
public class Token {

    @ManyToOne
    private User user;
    @Id
    private String jwtToken;
    private UserRole userRole = null;

    public Token() {
    }

    public Token(User userId, String jwtToken, UserRole userRole) {
        this.user = userId;
        this.jwtToken = jwtToken;
        this.userRole = userRole;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public enum UserRole {
        Admin, User
    }
}
