package com.ride.share.aad.storage.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getUser() {
        User user = new User("123");
        System.out.println(user);
    }

    @Test
    void getUserFailTest() {
        User user = new User("1");
        System.out.println(user);
    }

    @Test
    void getAllUsers() {
        for (User users : User.getAllUsers()) {
            System.out.println(users);
        }
    }

    @Test
    void save() {
        User user = new User();
        user.setUserId("123");
        user.setName("John Doe");
        user.setEmailId("john@example.com");
        user.setRole(User.Role.Rider);
        user.setLastSeen(System.currentTimeMillis());

        // Saving the Users object
        user.save();
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}