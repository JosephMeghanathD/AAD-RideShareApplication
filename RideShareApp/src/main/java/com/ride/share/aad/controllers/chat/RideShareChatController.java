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
    @PostMapping("/{toUserId}")
    @ResponseBody
    public String getMessages(@PathVariable("toUserId") String toUserId, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Chat chat = new Chat(user.getUserId(), toUserId);
        return ChatUtils.toJson(chat).toString();
    }

    @PostMapping("/active")
    @ResponseBody
    public String getAllChats(@RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        ChatUtils.getAllChats(user.getUserId());
        // TODO: get all chat ids
        return "";
    }


    @PostMapping("/send/{toUserId}")
    @ResponseBody
    public String sendMessage(@RequestBody String chatMessage, @PathVariable("toUserId") String toUserId, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Chat chat = new Chat(user.getUserId(), toUserId);
        JSONObject chatMessageJson = new JSONObject(chatMessage);
        chat.addMessage(ChatUtils.fromJson(chatMessageJson));
        chat.save();
        return ChatUtils.toJson(chat).toString();
    }

}
