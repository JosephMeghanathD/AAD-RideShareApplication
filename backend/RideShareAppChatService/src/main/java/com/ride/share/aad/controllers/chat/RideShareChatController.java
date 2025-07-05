package com.ride.share.aad.controllers.chat;

import com.ride.share.aad.storage.dao.chat.ChatDAO;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import com.ride.share.aad.utils.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rs/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareChatController {

    @Autowired
    ChatDAO chatDAO;

    @Autowired
    RequestAuthUtils requestAuthUtils;

    @Autowired
    ChatService chatService;

    @PostMapping("/send/{toUserId}")
    @ResponseBody
    public ResponseEntity<Chat> sendMessage(@RequestBody ChatMessage chatMessage, @PathVariable("toUserId") String toUserId, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        Chat chat = chatService.addMessage(chatMessage, toUserId, authorizationHeader);
        Optional<Chat> byId = chatDAO.findById(chat.getChatId());
        if (byId.isPresent()) {
            byId.get().getMessages().sort(Comparator.comparing(ChatMessage::getTimeStamp));
            return ResponseEntity.ok().body(byId.get());
        }
        throw new Exception("failed to get chat ID");
    }

    @GetMapping("/{toUserId}")
    @ResponseBody
    public ResponseEntity<Chat> getMessages(@PathVariable("toUserId") String toUserId, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        Chat chat = chatService.getChat(toUserId, authorizationHeader);
        chat.getMessages().sort(Comparator.comparing(ChatMessage::getTimeStamp));
        return ResponseEntity.ok().body(chat);
    }

    @GetMapping("/conversations")
    @ResponseBody
    public ResponseEntity<List<Chat>> getAllChats(@RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        List<Chat> mergedList = new ArrayList<>(user.getChatsWithUserID1());
        mergedList.addAll(user.getChatsWithUserID2());
        return ResponseEntity.ok().body(mergedList);
    }
}
