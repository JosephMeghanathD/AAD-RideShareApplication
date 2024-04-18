package com.ride.share.aad.utils.service;

import com.ride.share.aad.storage.dao.UserDAO;
import com.ride.share.aad.storage.dao.chat.ChatDAO;
import com.ride.share.aad.storage.dao.chat.ChatMessageDAO;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import com.ride.share.aad.utils.entity.ChatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    RequestAuthUtils requestAuthUtils;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ChatDAO chatDAO;

    @Autowired
    ChatMessageDAO chatMessageDAO;


    public Chat getChat(String toUserId, String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        Optional<User> toUser = userDAO.findById(toUserId);
        if (toUser.isEmpty()) {
            throw new Exception("Invalid to user id");
        }
        String chatID = ChatUtils.getChatID(toUser.get().getName(), user.getName());
        Optional<Chat> optionalChat = chatDAO.findById(chatID);
        Chat chat;
        if (optionalChat.isEmpty()) {
            chat = new Chat();
            chat.setUserID1(user);
            chat.setUserID2(toUser.get());
            chatDAO.save(chat);
        } else {
            chat = optionalChat.get();
        }
        return chat;
    }

    public Chat addMessage(ChatMessage chatMessage, String toUserId, String authorizationHeader) throws Exception {
        Chat chat = getChat(toUserId, authorizationHeader);
        chatMessage.setChat(chat);
        chatMessage.setTimeStamp(System.currentTimeMillis() / 1000);
        chatMessageDAO.save(chatMessage);
        return chat;
    }
}
