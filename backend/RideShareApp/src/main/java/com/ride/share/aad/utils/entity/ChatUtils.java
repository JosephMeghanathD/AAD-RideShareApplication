package com.ride.share.aad.utils.entity;


import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatUtils {

    public static String getChatID(String userID1, String userID2) {
        return Stream.of(userID1, userID2).sorted().collect(Collectors.joining("_"));
    }


    public static JSONObject toJson(ChatMessage chatMessage) {
        JSONObject chatMessageJson = new JSONObject();
        chatMessageJson.put("fromUserId", chatMessage.getFromUserId());
        chatMessageJson.put("message", chatMessage.getMessage());
        chatMessageJson.put("timeStamp", chatMessage.getTimeStamp());
        return chatMessageJson;
    }
}
