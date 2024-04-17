package com.ride.share.aad.storage.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @OneToMany(cascade = CascadeType.ALL)
    List<Token> tokens;
    @OneToMany(cascade = CascadeType.ALL)
    List<Ride> rides;
    @OneToMany(cascade = CascadeType.ALL)
    List<Chat> chats;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "VARCHAR(36)")
    private String userId;
    private String name;
    private String emailId;
    private String password;
    private Role role;
    private long lastSeen;


    public User() {
    }

    public User(String userId, String name, String emailId, String password, Role role, long lastSeen) {
        this.userId = userId;
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.role = role;
        this.lastSeen = lastSeen;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean validate(String password) {
        return password.equals(this.password);
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

    public enum Role {
        Rider, Driver
    }
}
