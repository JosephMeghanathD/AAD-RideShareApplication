package com.ride.share.aad.generators;

import com.github.javafaker.Faker;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import com.ride.share.aad.utils.entity.ChatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatDataGenerator {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static List<Chat> generateRandomConversations(List<User> allUsers, int numberOfChats, int numberOfMessages) {
        long timeStamp = System.currentTimeMillis() / 1000;
        ArrayList<Chat> chats = new ArrayList<>();
        for (int i = 0; i < numberOfChats; i++) {
            User user = allUsers.get(random.nextInt(allUsers.size()));
            User user1 = allUsers.get(random.nextInt(allUsers.size()));
            User senderID;
            Chat chat = new Chat();
            chat.setChatId(ChatUtils.getChatID(user.getUserId(), user1.getUserId()));
            for (int i1 = 0; i1 < numberOfMessages; i1++) {
                if (random.nextBoolean()) {
                    senderID = user1;
                } else {
                    senderID = user;
                }
                String sentence = faker.lorem().sentence();
                ChatMessage chatMessage = new ChatMessage(senderID, sentence, timeStamp + i + i1);
                chat.addMessage(chatMessage);
            }
            chats.add(chat);
        }
        return chats;
    }
}
