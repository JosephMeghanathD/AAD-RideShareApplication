package com.ride.share.aad.generators;

import com.github.javafaker.Faker;
import com.ride.share.aad.GenerateTempData;
import com.ride.share.aad.storage.entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UserDataGenerator {

    private static final Faker faker = new Faker();
    private static final Set<String> usedUserIds = new HashSet<>();

    public static List<User> generateUserData(int n) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            User user = generateRandomUser();
            users.add(user);
        }
        return users;
    }

    private static User generateRandomUser() {
        String name = faker.name().fullName();
        String userId = generateUniqueUserId(name);
        String emailId = generateEmailId(name);
        String password = faker.internet().password();
        User.Role role = faker.random().nextBoolean() ? User.Role.Rider : User.Role.Driver;
        long lastSeen = GenerateTempData.getRandomTimestampInLastTwoDaysFrom();

        return new User(userId, name, emailId, password, role, lastSeen);
    }

    private static String generateUniqueUserId(String name) {
        String[] nameParts = name.split(" ");
        String fn = nameParts[0];
        String ln = nameParts[nameParts.length - 1];

        String one = fn.substring(0, (int) (Math.random() * (fn.length())) + 1);
        String two = getRandomIds(0, 3);
        String three = "";
        if (ln.length() >= 3) {
            three = ln.substring(ln.length() - 3);
        }
        String four = getRandomIds(1, 5);
        String uniqueID = one + two + three + "_" + four;
        String userId = uniqueID.toLowerCase();

        if (usedUserIds.contains(userId)) {
            return generateUniqueUserId(name);
        }
        usedUserIds.add(userId);
        return userId;
    }

    private static String getRandomIds(int min, int max) {
        return faker.number().digits(faker.number().numberBetween(min, max));
    }

    private static String generateEmailId(String name) {
        String digits = getRandomIds(1, 5);
        return name.replaceAll("\\s+", "").toLowerCase() + digits + "@aadrs.edu";
    }
}
