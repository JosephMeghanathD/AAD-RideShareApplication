package com.ride.share.aad.controllers.chat;

import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import com.ride.share.aad.utils.entity.ChatUtils;
import org.json.JSONObject;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rs/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareChatController {
    @PostMapping("/send/{toUserId}")
    @ResponseBody
    public String sendMessage(@RequestBody String chatMessage, @PathVariable("toUserId") String toUserId, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Chat chat = new Chat(user.getUserId(), toUserId);
        JSONObject chatJson = new JSONObject(chatMessage);
        chat.addMessage(ChatUtils.fromJson(chatJson.getString("message"), user));
        chat.save();
        return ChatUtils.toJson(chat).toString();
    }

    @GetMapping("/{toUserId}")
    @ResponseBody
    public String getMessages(@PathVariable("toUserId") String toUserId, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Chat chat = new Chat(user.getUserId(), toUserId);
        return ChatUtils.toJson(chat).toString();
    }

    @GetMapping("/conversations")
    @ResponseBody
    public String getAllChats(@RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        ChatUtils.getAllChats(user.getUserId());
        return ChatUtils.getAllChats(user.getUserId()).toString();
    }


}
