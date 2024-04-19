package com.ride.share.aad.storage.dao;


import com.ride.share.aad.storage.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenDAO extends JpaRepository<Token, String> {
    Optional<Token> findByJwtToken(String username);
}
