package com.ride.share.aad.storage.entity.chat;

import com.ride.share.aad.storage.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.StringJoiner;


@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "VARCHAR(36)")
    String chatMessageId;

    @ManyToOne
    User fromUserId;
    String message;
    long timeStamp;
    @ManyToOne
    Chat chat;

    public ChatMessage(User fromUserId, String message, long timeStamp) {
        this.fromUserId = fromUserId;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public User getFromUserId() {
        return fromUserId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(String chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public void setFromUserId(User fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ChatMessage.class.getSimpleName() + "[", "]").add("fromUserId='" + fromUserId + "'").add("message='" + message + "'").add("timeStamp=" + timeStamp).toString();
    }
}
