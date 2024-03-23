package com.ride.share.aad.storage.entity.chat;

import java.util.StringJoiner;

public class ChatMessage {
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
        return new StringJoiner(", ", ChatMessage.class.getSimpleName() + "[", "]")
                .add("fromUserId='" + fromUserId + "'")
                .add("message='" + message + "'")
                .add("timeStamp=" + timeStamp)
                .toString();
    }
}
