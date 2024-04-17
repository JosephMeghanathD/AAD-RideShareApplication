package com.ride.share.aad.storage.dao;


import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideDAO extends JpaRepository<Ride, String> {
}
