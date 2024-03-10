package com.ride.share.aad.utils.entity;


import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.controllers.auth.RideShareAuthController;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ride.share.aad.storage.service.CassandraStorageService.getCqlSession;

public class ChatUtils {

    private static final Logger logger = LoggerFactory.getLogger(ChatUtils.class);

    public static String getChatID(String userID1, String userID2) {
        return Stream.of(userID1, userID2).sorted().collect(Collectors.joining("_"));
    }

    public static Chat mapToEntity(Row row, Chat chat) {
        if (chat != null) {
            chat.setMessages(fromMessagesJson(row.getString("messages")));
            return chat;
        }
        return new Chat(row.getString("userId1"), row.getString("userId2"), fromMessagesJson(row.getString("messages")));
    }

    public static JSONObject toJson(ChatMessage chatMessage) {
        JSONObject chatMessageJson = new JSONObject();
        chatMessageJson.put("fromUserId", chatMessage.getFromUserId());
        chatMessageJson.put("message", chatMessage.getMessage());
        chatMessageJson.put("timeStamp", chatMessage.getTimeStamp());
        return chatMessageJson;
    }

    public static ChatMessage fromJson(JSONObject chatMessageJson) {
        return new ChatMessage(chatMessageJson.getString("fromUserId"), chatMessageJson.getString("message"), chatMessageJson.getLong("timeStamp"));
    }

    public static String toMessagesJsonStr(List<ChatMessage> messages) {
        JSONArray messagesJson = toMessagesJson(messages);
        return messagesJson.toString();
    }

    public static JSONArray toMessagesJson(List<ChatMessage> messages) {
        JSONArray messagesJson = new JSONArray();
        for (ChatMessage message : messages) {
            messagesJson.put(toJson(message));
        }
        return messagesJson;
    }


    public static List<ChatMessage> fromMessagesJson(String messagesJsonStr) {
        JSONArray messagesJson = new JSONArray(messagesJsonStr);
        List<ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < messagesJson.length(); i++) {
            JSONObject jsonObject = messagesJson.getJSONObject(i);
            messages.add(fromJson(jsonObject));
        }
        messages = messages.stream().sorted(Comparator.comparingLong(ChatMessage::getTimeStamp)).collect(Collectors.toList());
        return messages;
    }

    public static JSONObject toJson(Chat chat) {
        JSONObject chatJson = new JSONObject();
        chatJson.put("userID1", chat.getUserID1());
        chatJson.put("userID2", chat.getUserID2());
        chatJson.put("messages", toMessagesJson(chat.getMessages()));
        return chatJson;
    }

    public static List<Chat> getAllChats(PreparedStatement query) {
        List<Chat> chats = new ArrayList<>();
        ResultSet execute = getCqlSession().execute(query.bind());
        for (Row row : execute) {
            chats.add(mapToEntity(row, null));
        }
        return chats;
    }

    public static JSONArray getAllChats(String userId) {
        List<Chat> allChatsOfUser = Chat.getAllChatsOfUser(userId);
        JSONArray allChats = new JSONArray();
        for (Chat chat : allChatsOfUser) {
            try {
                String key = chat.getChatId();
                chat.getMessages().sort(Comparator.comparingLong(ChatMessage::getTimeStamp).reversed());
                allChats.put(ChatUtils.toJson(new Chat(chat.getUserID1(), chat.getUserID2(), Collections.singletonList(chat.getMessages().getFirst()))));
            } catch (Exception e) {
                logger.error("Failed to process chat: {}", chat, e);
            }
        }
        return allChats;
    }


    public static ChatMessage fromJson(String chatMessage, User user) {
        return new ChatMessage(user.getUserId(), chatMessage, System.currentTimeMillis()/1000);
    }
}
