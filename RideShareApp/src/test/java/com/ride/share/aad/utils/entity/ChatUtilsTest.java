package com.ride.share.aad.utils.entity;

import com.ride.share.aad.storage.entity.chat.Chat;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;

import static com.ride.share.aad.utils.entity.ChatUtils.getChatID;

class ChatUtilsTest {

    @Test
    public void getChatIdTest() {
        assertChatID("abc", "", "_abc");
        assertChatID("abc", "abc", "abc_abc");
        assertChatID("", "", "_");

    }
    public void assertChatID(String u1, String u2, String expected) {
        Assert.eq(getChatID(u1, u2), expected, "Invalid");
    }

    @Test
    public void getChat() {
    }
}