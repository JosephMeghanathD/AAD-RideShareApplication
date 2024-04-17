package com.ride.share.aad.storage.dao;


import com.ride.share.aad.storage.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatDAO extends JpaRepository<Chat, String> {
}
