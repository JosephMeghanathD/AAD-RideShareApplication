package com.ride.share.aad.storage.dao;


import com.ride.share.aad.storage.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideDAO extends JpaRepository<Ride, String> {
    /**
     * Retrieves a paginated list of rides where the destination contains the specified string,
     * ignoring case considerations.
     *
     * @param destination the string to search for within the destination field of rides
     * @param pageable    the pagination and sorting information
     * @return a page of rides with destinations containing the specified string
     */

    Page<Ride> findByDestinationContainingIgnoreCase(String destination, Pageable pageable);

    /**
     * Retrieves a paginated list of rides where the starting location contains the specified string,
     * ignoring case considerations.
     *
     * @param startingFromLocation the string to search for within the starting location field of rides
     * @param pageable            the pagination and sorting information
     * @return a page of rides with starting locations containing the specified string
     */
    Page<Ride> findByStartingFromLocationContainingIgnoreCase(String startingFromLocation, Pageable pageable);

    /**
     * Retrieves a list of rides where the postedBy user ID matches the specified user ID.
     *
     * @param userId the user ID to search for
     * @return a list of rides posted by the user with the specified user ID
     */
    Page<Ride> findByPostedByUserId(String userId, Pageable pageable);
}
