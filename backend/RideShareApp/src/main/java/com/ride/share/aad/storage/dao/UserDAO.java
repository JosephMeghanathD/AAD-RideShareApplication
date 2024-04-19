package com.ride.share.aad.storage.dao;


import com.ride.share.aad.storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, String> {
    Optional<User> findByName(String username);
}
