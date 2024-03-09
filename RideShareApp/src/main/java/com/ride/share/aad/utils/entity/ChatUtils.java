package com.ride.share.aad.utils.entity;


import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatUtils {

    public static String getChatID(String userID1, String userID2) {
        return Base64.getEncoder()
                .encodeToString(Stream.of(userID1, userID2)
                        .sorted().collect(Collectors.joining("_"))
                        .getBytes());
    }

    public static Chat mapToEntity(Row row, Chat chat) {
        return new Chat(row.getString("userId1"),
                row.getString("userId2"),
                fromMessagesJson(row.getString("messages")));
    }

    public static JSONObject toJson(ChatMessage chatMessage) {
        JSONObject chatMessageJson = new JSONObject();
        chatMessageJson.put("fromUserId", chatMessage.getFromUserId());
        chatMessageJson.put("message", chatMessage.getMessage());
        chatMessageJson.put("timeStamp", chatMessage.getTimeStamp());
        return chatMessageJson;
    }

    public static ChatMessage fromJson(JSONObject chatMessageJson) {
        return new ChatMessage(chatMessageJson.getString("fromUserId"),
                chatMessageJson.getString("message"),
                chatMessageJson.getLong("timeStamp"));
    }

    public static String toMessagesJson(List<ChatMessage> messages) {
        JSONArray messagesJson = new JSONArray();
        for (ChatMessage message : messages) {
            messagesJson.put(toJson(message));
        }
        return messagesJson.toString();
    }

    public static List<ChatMessage> fromMessagesJson(String messagesJsonStr) {
        JSONArray messagesJson = new JSONArray(messagesJsonStr);
        List<ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < messagesJson.length(); i++) {
            JSONObject jsonObject = messagesJson.getJSONObject(i);
            messages.add(fromJson(jsonObject));
        }
        messages = messages.stream()
                .sorted(Comparator.comparingLong(ChatMessage::getTimeStamp))
                .collect(Collectors.toList());
        return messages;
    }
}
