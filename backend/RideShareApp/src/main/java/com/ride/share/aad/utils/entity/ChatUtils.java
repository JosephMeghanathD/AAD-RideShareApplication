package com.ride.share.aad.utils.entity;

import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.Chat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatUtils {

    private static final Logger logger = LoggerFactory.getLogger(ChatUtils.class);

    public static String getChatID(String userID1, String userID2) {
        return Stream.of(userID1, userID2).sorted().collect(Collectors.joining("_"));
    }


    public static JSONObject toJson(Chat.ChatMessage chatMessage) {
        JSONObject chatMessageJson = new JSONObject();
        chatMessageJson.put("fromUserId", chatMessage.getFromUserId());
        chatMessageJson.put("message", chatMessage.getMessage());
        chatMessageJson.put("timeStamp", chatMessage.getTimeStamp());
        return chatMessageJson;
    }

    public static Chat.ChatMessage fromJson(JSONObject chatMessageJson) {
        return new Chat.ChatMessage(chatMessageJson.getString("fromUserId"), chatMessageJson.getString("message"), chatMessageJson.getLong("timeStamp"));
    }

    public static String toMessagesJsonStr(List<Chat.ChatMessage> messages) {
        JSONArray messagesJson = toMessagesJson(messages);
        return messagesJson.toString();
    }

    public static JSONArray toMessagesJson(List<Chat.ChatMessage> messages) {
        JSONArray messagesJson = new JSONArray();
        for (Chat.ChatMessage message : messages) {
            messagesJson.put(toJson(message));
        }
        return messagesJson;
    }


    public static List<Chat.ChatMessage> fromMessagesJson(String messagesJsonStr) {
        JSONArray messagesJson = new JSONArray(messagesJsonStr);
        List<Chat.ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < messagesJson.length(); i++) {
            JSONObject jsonObject = messagesJson.getJSONObject(i);
            messages.add(fromJson(jsonObject));
        }
        messages = messages.stream().sorted(Comparator.comparingLong(Chat.ChatMessage::getTimeStamp)).collect(Collectors.toList());
        return messages;
    }

    public static JSONObject toJson(Chat chat) {
        JSONObject chatJson = new JSONObject();
        chatJson.put("userID1", chat.getUserID1());
        chatJson.put("userID2", chat.getUserID2());
        chatJson.put("messages", toMessagesJson(chat.getMessages()));
        return chatJson;
    }

    public static Chat.ChatMessage fromJson(String chatMessage, User user) {
        return new Chat.ChatMessage(user.getUserId(), chatMessage, System.currentTimeMillis()/1000);
    }
}
