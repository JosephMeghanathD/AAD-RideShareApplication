package com.ride.share.aad.controllers.chat;

import com.ride.share.aad.storage.dao.UserDAO;
import com.ride.share.aad.storage.dao.chat.ChatDAO;
import com.ride.share.aad.storage.dao.chat.ChatMessageDAO;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import com.ride.share.aad.utils.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rs/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareChatController {

    @Autowired
    ChatDAO chatDAO;
    @Autowired
    ChatMessageDAO chatMessageDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    RequestAuthUtils requestAuthUtils;

    @Autowired
    ChatService chatService;

    @PostMapping("/send/{toUserId}")
    @ResponseBody
    public Chat sendMessage(@RequestBody ChatMessage chatMessage,
                            @PathVariable("toUserId") String toUserId,
                            @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        Chat chat = chatService.addMessage(chatMessage, toUserId, authorizationHeader);
        Optional<Chat> byId = chatDAO.findById(chat.getChatId());
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new Exception("failed to get chat ID");
    }

    @GetMapping("/{toUserId}")
    @ResponseBody
    public Chat getMessages(@PathVariable("toUserId") String toUserId, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        Chat chat = chatService.getChat(toUserId, authorizationHeader);
        return chat;
    }


    @GetMapping("/conversations")
    @ResponseBody
    public List<Chat> getAllChats(@RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        return user.getChats();
    }


}
