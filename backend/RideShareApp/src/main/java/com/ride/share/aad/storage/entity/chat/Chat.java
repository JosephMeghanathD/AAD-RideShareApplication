package com.ride.share.aad.storage.entity.chat;

import com.ride.share.aad.storage.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.ride.share.aad.utils.entity.ChatUtils.getChatID;

@Entity
@Table(name = "chats")
public class Chat {


    @Id
    String chatId;

    @ManyToOne
    User userID1;
    @ManyToOne
    User userID2;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ChatMessage> messages;

    public Chat() {
    }

    public Chat(User userID1, User userID2, List<ChatMessage> messages) {
        this.chatId = getChatID(userID1.getName(), userID2.getName());
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

    public User getUserID1() {
        return userID1;
    }

    public void setUserID1(User userID1) {
        this.userID1 = userID1;
    }

    public User getUserID2() {
        return userID2;
    }

    public void setUserID2(User userID2) {
        this.userID2 = userID2;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public Chat addMessage(ChatMessage message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
        return this;
    }

}
