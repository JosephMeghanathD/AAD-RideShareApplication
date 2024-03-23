package com.ride.share.util.generators;

import com.github.javafaker.Faker;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.utils.entity.UserUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.exit;

public class RideDataGenerator {

    public static void main(String[] args) {
        List<User> users = UserUtils.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found. Exiting.");
            return;
        }

        int n = 10; // Number of records to generate
        List<Ride> rides = generateRideData(users, n);
        for (Ride ride : rides) {
            System.out.println(ride);
        }
        exit(0);
    }

    public static List<Ride> generateRideData(List<User> users, int n) {
        Faker faker = new Faker();
        Random random = new Random();
        List<Ride> rides = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            User randomUser = users.get(random.nextInt(users.size()));
            String startingLocation = faker.address().streetAddress();
            String destination = faker.address().streetAddress();
            int numberOfPeople = faker.number().numberBetween(1, 5);
            double fare = faker.number().randomDouble(2, 10, 100);
            long postedAt = GenerateTempData.getRandomTimestampInLastTwoDaysFrom(randomUser.getLastSeen());
            long timeOfRide = getRandomTimeOfRide(postedAt);

            Ride ride = new Ride(startingLocation, destination, numberOfPeople, fare, timeOfRide, randomUser.getUserId(), postedAt);
            rides.add(ride);
        }
        return rides;
    }

    public static long getRandomTimeOfRide(long postedAt) {
        Instant now = Instant.now();
        long nowMillis = now.toEpochMilli();
        long twoWeeksInMillis = Duration.ofDays(14).toMillis();
        return ThreadLocalRandom.current().nextLong(postedAt, Math.min(postedAt + twoWeeksInMillis, nowMillis + 1));
    }
}
