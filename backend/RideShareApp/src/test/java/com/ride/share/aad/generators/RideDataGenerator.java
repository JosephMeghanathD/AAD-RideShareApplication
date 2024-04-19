package com.ride.share.aad.generators;

import com.github.javafaker.Faker;
import com.ride.share.aad.GenerateTempData;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RideDataGenerator {

    public static List<Ride> generateRideData(List<User> users, int n) {
        Faker faker = new Faker();
        Random random = new Random();
        List<Ride> rides = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            User randomUser = users.get(random.nextInt(users.size()));
            if (randomUser.getLastSeen() == null) {
                continue;
            }
            String startingLocation = faker.address().streetAddress();
            String destination = faker.address().streetAddress();
            int numberOfPeople = faker.number().numberBetween(1, 5);
            double fare = faker.number().randomDouble(2, 10, 100);
            long postedAt = GenerateTempData.getRandomTimestampInLastTwoDaysFrom(randomUser.getLastSeen());
            long timeOfRide = getRandomTimeOfRide(postedAt);

            Ride ride = new Ride(startingLocation, destination, numberOfPeople, fare, timeOfRide, randomUser, postedAt);
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
