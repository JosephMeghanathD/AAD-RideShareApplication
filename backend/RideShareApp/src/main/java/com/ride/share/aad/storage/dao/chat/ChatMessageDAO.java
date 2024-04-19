package com.ride.share.aad.storage.dao.chat;


import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageDAO extends JpaRepository<ChatMessage, String> {
}
