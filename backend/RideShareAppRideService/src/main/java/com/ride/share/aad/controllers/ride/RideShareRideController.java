package com.ride.share.aad.controllers.ride;


import com.ride.share.aad.config.exceptions.InvalidAuthRequest;
import com.ride.share.aad.storage.dao.RideDAO;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rs/ride")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareRideController {

    private static final Logger logger = LoggerFactory.getLogger(RideShareRideController.class);

    @Autowired
    RequestAuthUtils requestAuthUtils;

    @Autowired
    RideDAO rideDAO;

    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<Ride> postRide(@RequestBody Ride ride,
                                         @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        ride.setPostedBy(user); // Set the user who posted the ride
        return ResponseEntity.ok().body(rideDAO.save(ride));
    }

    @GetMapping("/{rideId}")
    @ResponseBody
    public ResponseEntity<Ride> getRide(@PathVariable String rideId,
                                        @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        Optional<Ride> optionalRide = rideDAO.findById(rideId);
        return optionalRide.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{rideId}")
    @ResponseBody
    public ResponseEntity<Ride> editRide(@PathVariable String rideId, @RequestBody Ride updatedRide,
                                         @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        Optional<Ride> optionalRide = rideDAO.findById(rideId);
        if (optionalRide.isPresent()) {
            Ride existingRide = optionalRide.get();
            // Update the existing ride properties
            existingRide.setStartingFromLocation(updatedRide.getStartingFromLocation());
            existingRide.setDestination(updatedRide.getDestination());
            existingRide.setNumberOfPeople(updatedRide.getNumberOfPeople());
            existingRide.setFare(updatedRide.getFare());
            existingRide.setTimeOfRide(updatedRide.getTimeOfRide());
            // Save the updated ride
            Ride savedRide = rideDAO.save(existingRide);
            return ResponseEntity.ok(savedRide);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by/user")
    @ResponseBody
    public ResponseEntity<List<Ride>> getUserPostedRides(@RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        return ResponseEntity.ok().body(rideDAO.findByPostedByUserId(user.getUserId()));
    }

    @PostMapping("/by/destination")
    @ResponseBody
    public ResponseEntity<List<Ride>> getRidesByDestination(@RequestBody String searchString, @RequestHeader(name = "Authorization", required = false) @DefaultValue("XXX") String authorizationHeader) throws Exception {
        try {
            User user = requestAuthUtils.getUser(authorizationHeader);
            return ResponseEntity.ok().body(rideDAO.findByDestinationContainingIgnoreCase(searchString));
        } catch (InvalidAuthRequest e) {
            List<Ride> ridesWithoutPostedBy = rideDAO.findByDestinationContainingIgnoreCase(searchString);
            ridesWithoutPostedBy.forEach(ride -> ride.setPostedBy(null));
            return ResponseEntity.ok().body(ridesWithoutPostedBy);
        }
    }

    @PostMapping("/by/source")
    @ResponseBody
    public ResponseEntity<List<Ride>> getRidesBySource(@RequestBody String searchString, @RequestHeader(name = "Authorization", required = false) @DefaultValue("XXX") String authorizationHeader) throws Exception {
        try {
            User user = requestAuthUtils.getUser(authorizationHeader);
            return ResponseEntity.ok().body(rideDAO.findByStartingFromLocationContainingIgnoreCase(searchString));
        } catch (InvalidAuthRequest e) {
            List<Ride> ridesWithoutPostedBy = rideDAO.findByStartingFromLocationContainingIgnoreCase(searchString);
            ridesWithoutPostedBy.forEach(ride -> ride.setPostedBy(null));
            return ResponseEntity.ok().body(ridesWithoutPostedBy);
        }
    }

    @DeleteMapping("/{rideId}")
    @ResponseBody
    public ResponseEntity<Ride> deleteRide(@PathVariable String rideId, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        Optional<Ride> optionalRide = rideDAO.findById(rideId);
        if (optionalRide.isPresent()) {
            rideDAO.deleteById(rideId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rides")
    public ResponseEntity<List<Ride>> getRides(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "timeOfRide") String sortBy,
                                               @RequestParam(defaultValue = "asc") String sortOrder,
                                               @RequestHeader(name = "Authorization", required = false) @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        try {
            User user = requestAuthUtils.getUser(authorizationHeader);
            return ResponseEntity.ok().body(rideDAO.findAll());
        } catch (InvalidAuthRequest iae) {
            List<Ride> ridesWithoutPostedBy = rideDAO.findAll();
            ridesWithoutPostedBy.forEach(ride -> ride.setPostedBy(null));
            return ResponseEntity.ok().body(ridesWithoutPostedBy);
        } catch (Exception e) {
            logger.error("Failed to get rides {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    @ExceptionHandler(InvalidAuthRequest.class)
    public ResponseEntity<ErrorResponse> handleAuthException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
