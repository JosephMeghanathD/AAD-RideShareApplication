package com.ride.share.aad.utils.service;

import com.ride.share.aad.storage.dao.RideDAO;
import com.ride.share.aad.storage.dao.UserDAO;
import com.ride.share.aad.storage.dao.chat.ChatDAO;
import com.ride.share.aad.storage.dao.chat.ChatMessageDAO;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.storage.entity.chat.Chat;
import com.ride.share.aad.storage.entity.chat.ChatMessage;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import com.ride.share.aad.utils.entity.ChatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RideService {

    @Autowired
    RideDAO rideDAO;

    public Ride editRide(Ride updatedRide, Optional<Ride> optionalRide) {
        Ride existingRide = optionalRide.get();
        // Update the existing ride properties
        existingRide.setStartingFromLocation(updatedRide.getStartingFromLocation());
        existingRide.setDestination(updatedRide.getDestination());
        existingRide.setNumberOfPeople(updatedRide.getNumberOfPeople());
        existingRide.setFare(updatedRide.getFare());
        existingRide.setTimeOfRide(updatedRide.getTimeOfRide());
        // Save the updated ride
        Ride savedRide = rideDAO.save(existingRide);
        return savedRide;
    }

    public List<Ride> getRidesWithoutPostedBy() {
        List<Ride> ridesWithoutPostedBy = rideDAO.findAll();
        return getRidesWithoutPostedBy(ridesWithoutPostedBy);
    }

    public static List<Ride> getRidesWithoutPostedBy(List<Ride> ridesWithoutPostedBy) {
        ridesWithoutPostedBy.forEach(ride -> ride.setPostedBy(null));
        return ridesWithoutPostedBy;
    }
}
