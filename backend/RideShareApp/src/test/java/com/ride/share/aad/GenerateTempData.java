package com.ride.share.aad;

import com.ride.share.aad.generators.ChatDataGenerator;
import com.ride.share.aad.generators.RideDataGenerator;
import com.ride.share.aad.generators.UserDataGenerator;
import com.ride.share.aad.storage.dao.RideDAO;
import com.ride.share.aad.storage.dao.UserDAO;
import com.ride.share.aad.storage.dao.chat.ChatDAO;
import com.ride.share.aad.storage.dao.chat.ChatMessageDAO;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class GenerateTempData implements CommandLineRunner {

    public static final boolean SAVE = true;
    public static final int NO_OF_USERS = 100;
    public static final int NO_OF_RIDES = 1000;
    private static final boolean RESET_DATA = true;
    @Autowired
    UserDAO userDAO;
    @Autowired
    RideDAO rideDAO;
    @Autowired
    ChatDAO chatDAO;
    @Autowired
    ChatMessageDAO chatMessageDAO;

    public static long getRandomTimestampInLastTwoDaysFrom() {
        long currentTimeMillis = System.currentTimeMillis();
        return getRandomTimestampInLastTwoDaysFrom(currentTimeMillis);
    }

    public static long getRandomTimestampInLastTwoDaysFrom(long currentTimeMillis) {
        long twoDaysAgoMillis = currentTimeMillis - (86400000 * 2);
        return ThreadLocalRandom.current().nextLong(twoDaysAgoMillis, currentTimeMillis + 1);
    }

    public static void main(String[] args) {
        SpringApplication.run(GenerateTempData.class, args);
    }

    public void resetData() {
        chatDAO.deleteAll();
        rideDAO.deleteAll();
        userDAO.deleteAll();
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (RESET_DATA) {
            resetData();
        }
        List<User> users = UserDataGenerator.generateUserData(NO_OF_USERS);
        users.add(new User("wow", "wow", "wow@gmail.edu", "123", User.Role.Rider, GenerateTempData.getRandomTimestampInLastTwoDaysFrom()));
        users.add(new User("wow1", "wow1", "wow@gmail.edu", "123", User.Role.Driver, GenerateTempData.getRandomTimestampInLastTwoDaysFrom()));
        System.out.println("Users:");
        for (User user : users) {
            System.out.println(user);
            if (SAVE) {
                userDAO.save(user);
            }
        }
        List<Ride> rides = RideDataGenerator.generateRideData(SAVE ? userDAO.findAll() : users, NO_OF_RIDES);
        System.out.println("Rides:");
        for (Ride ride : rides) {
            System.out.println(ride);
            if (SAVE) {
                rideDAO.save(ride);
            }
        }

        List<Chat> chats = ChatDataGenerator.generateRandomConversations(SAVE ? userDAO.findAll() : users, 110, 40);
        for (Chat chat : chats) {
            System.out.println(chat);
            if (SAVE) {
                for (ChatMessage message : chat.getMessages()) {
                    chatMessageDAO.save(message);
                }
                chatDAO.save(chat);
            }
        }
        System.out.println("generated all data");
    }
}
