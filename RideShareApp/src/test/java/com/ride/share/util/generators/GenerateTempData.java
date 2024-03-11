package com.ride.share.util.generators;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.utils.entity.UserUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.ride.share.aad.storage.service.CassandraStorageService.getCqlSession;
import static java.lang.System.exit;

public class GenerateTempData {

    public static final boolean SAVE = true;
    public static final int NO_OF_USERS = 100;
    public static final int NO_OF_RIDES = 1000;
    private static final boolean RESET_DATA = true;

    public static void main(String[] args) {
        if (RESET_DATA) {
            resetData();
        }
        List<User> users = UserDataGenerator.generateUserData(NO_OF_USERS);
        users.add(new User("wow", "wow", "wow@gmail.edu", User.Role.Rider, System.currentTimeMillis()/1000, "123"));
        System.out.println("Users:");
        for (User user : users) {
            System.out.println(user);
            if (SAVE) {
                user.save();
            }
        }
        List<Ride> rides = RideDataGenerator.generateRideData(SAVE ? UserUtils.getAllUsers() : users, NO_OF_RIDES);
        System.out.println("Rides:");
        for (Ride ride : rides) {
            System.out.println(ride);
            if (SAVE) {
                ride.save();
            }
        }

        List<Chat> chats = ChatDataGenerator.generateRandomConversations(users, 110, 40);
        for (Chat chat : chats) {
            System.out.println(chat);
            if (SAVE) {
                chat.save();
            }
        }
        exit(0);
    }

    public static void resetData() {
        CqlSession cqlSession = getCqlSession();

        // Reset rides table
        BoundStatement deleteRidesStatement = cqlSession.prepare("TRUNCATE rides").bind();
        cqlSession.execute(deleteRidesStatement);

        // Reset users table
        BoundStatement deleteUsersStatement = cqlSession.prepare("TRUNCATE users").bind();
        cqlSession.execute(deleteUsersStatement);

        // Reset users table
        BoundStatement deleteChatsStatement = cqlSession.prepare("TRUNCATE chats").bind();
        cqlSession.execute(deleteChatsStatement);

    }

    public static long getRandomTimestampInLastTwoDaysFrom() {
        long currentTimeMillis = System.currentTimeMillis();
        return getRandomTimestampInLastTwoDaysFrom(currentTimeMillis);
    }

    public static long getRandomTimestampInLastTwoDaysFrom(long currentTimeMillis) {
        long twoDaysAgoMillis = currentTimeMillis - (86400000 * 2);
        return ThreadLocalRandom.current().nextLong(twoDaysAgoMillis, currentTimeMillis + 1);
    }
}
