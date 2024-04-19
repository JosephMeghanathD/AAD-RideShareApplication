package com.ride.share.aad.storage.dao;


import com.ride.share.aad.storage.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideDAO extends JpaRepository<Ride, String> {
    List<Ride> findByDestinationContainingIgnoreCase(String destination);

    List<Ride> findByStartingFromLocationContainingIgnoreCase(String startingFromLocation);

    List<Ride> findByPostedByUserId(String userId);
}
