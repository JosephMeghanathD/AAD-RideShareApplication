package com.ride.share.aad.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static com.ride.share.aad.utils.entity.ChatUtils.getChatID;

@Entity
@Table(name = "chats")
public class Chat {


    @Id
    String chatId;

    @ManyToOne
    String userID1;
    @ManyToOne
    String userID2;

    List<ChatMessage> messages = new ArrayList<>();

    public Chat() {
    }

    public Chat(String chatId, String userID1, String userID2, List<ChatMessage> messages) {
        this.chatId = getChatID(userID1, userID2);
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.messages = messages;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserID1() {
        return userID1;
    }

    public void setUserID1(String userID1) {
        this.userID1 = userID1;
    }

    public String getUserID2() {
        return userID2;
    }

    public void setUserID2(String userID2) {
        this.userID2 = userID2;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public Chat addMessage(ChatMessage message) {
        messages.add(message);
        return this;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public static class ChatMessage {
        String fromUserId;
        String message;
        long timeStamp;

        public ChatMessage(String fromUserId, String message, long timeStamp) {
            this.fromUserId = fromUserId;
            this.message = message;
            this.timeStamp = timeStamp;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public String getMessage() {
            return message;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", ChatMessage.class.getSimpleName() + "[", "]").add("fromUserId='" + fromUserId + "'").add("message='" + message + "'").add("timeStamp=" + timeStamp).toString();
        }
    }
}
