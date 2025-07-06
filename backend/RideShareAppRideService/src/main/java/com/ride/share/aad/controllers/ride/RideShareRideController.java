package com.ride.share.aad.controllers.ride;


import com.ride.share.aad.config.exceptions.InvalidAuthRequest;
import com.ride.share.aad.storage.dao.RideDAO;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import com.ride.share.aad.utils.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    RideService rideService;

    @PostMapping("/post")
    @ResponseBody
    public ResponseEntity<Ride> postRide(@RequestBody Ride ride, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        ride.setPostedBy(user); // Set the user who posted the ride
        ride.setPostedAt(System.currentTimeMillis());
        return ResponseEntity.ok().body(rideDAO.save(ride));
    }

    @GetMapping("/{rideId}")
    @ResponseBody
    public ResponseEntity<Ride> getRide(@PathVariable String rideId) throws Exception {
        Optional<Ride> optionalRide = rideDAO.findById(rideId);
        return optionalRide.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{rideId}")
    @ResponseBody
    public ResponseEntity<Ride> editRide(@PathVariable String rideId, @RequestBody Ride updatedRide, @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        Optional<Ride> optionalRide = rideDAO.findById(rideId);
        if (optionalRide.isPresent()) {
            if (!optionalRide.get().getPostedBy().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("You are not authorized to edit this ride");
            }
            Ride savedRide = rideService.editRide(updatedRide, optionalRide.get());
            return ResponseEntity.ok(savedRide);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by/user")
    @ResponseBody
    public ResponseEntity<Page<Ride>> getUserPostedRides(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timeOfRide") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok().body(rideDAO.findByPostedByUserId(user.getUserId(), pageable));
    }

    /**
     * GET /by/destination
     *
     * Returns a paginated list of rides with the given destination.
     *
     * @param destination the destination to search for
     * @param page the page number to return (0-based)
     * @param size the number of items per page
     * @param sortBy the field to sort the results by
     * @param sortOrder the order to sort the results by
     * @param authorizationHeader the authorization header to check for authentication
     *
     * @return a paginated list of rides with the given destination
     *
     * @throws InvalidAuthRequest if the authorization header is invalid
     */
    @GetMapping("/by/destination") // Changed from POST to GET
    public ResponseEntity<Page<Ride>> getRidesByDestination(
            @RequestParam String destination, // Changed from @RequestBody to @RequestParam
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timeOfRide") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestHeader(name = "Authorization", required = false) @DefaultValue("XXX") String authorizationHeader) {

        // 1. Create Pageable object for pagination and sorting
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        try {
            // Authenticated users get the full data
            User user = requestAuthUtils.getUser(authorizationHeader);
            Page<Ride> ridesPage = rideDAO.findByDestinationContainingIgnoreCase(destination, pageable);
            return ResponseEntity.ok().body(ridesPage);
        } catch (InvalidAuthRequest e) {
            // Unauthenticated users get data with 'postedBy' field nulled out
            Page<Ride> ridesPage = rideDAO.findByDestinationContainingIgnoreCase(destination, pageable);

            // 2. Use page.map() to transform the content while preserving pagination details
            Page<Ride> ridesWithoutPostedBy = ridesPage.map(ride -> {
                ride.setPostedBy(null);
                return ride;
            });
            return ResponseEntity.ok().body(ridesWithoutPostedBy);
        }
    }

    @GetMapping("/by/source") // Changed from POST to GET
    public ResponseEntity<Page<Ride>> getRidesBySource(
            @RequestParam String source, // Changed from @RequestBody to @RequestParam and renamed
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timeOfRide") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestHeader(name = "Authorization", required = false) @DefaultValue("XXX") String authorizationHeader) {

        // 1. Create Pageable object
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        try {
            // Authenticated users get the full data
            User user = requestAuthUtils.getUser(authorizationHeader);
            Page<Ride> ridesPage = rideDAO.findByStartingFromLocationContainingIgnoreCase(source, pageable);
            return ResponseEntity.ok().body(ridesPage);
        } catch (InvalidAuthRequest e) {
            // Unauthenticated users get data with 'postedBy' field nulled out
            Page<Ride> ridesPage = rideDAO.findByStartingFromLocationContainingIgnoreCase(source, pageable);

            // 2. Use page.map() to transform the content
            Page<Ride> ridesWithoutPostedBy = ridesPage.map(ride -> {
                ride.setPostedBy(null);
                return ride;
            });
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
    public ResponseEntity<Page<Ride>> getRides(
            @RequestParam(defaultValue = "0") int page, // Page numbers are 0-based in Spring Data
            @RequestParam(defaultValue = "10") int size, // Add a size parameter for page size
            @RequestParam(defaultValue = "timeOfRide") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestHeader(name = "Authorization", required = false) @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        try {
            User user = requestAuthUtils.getUser(authorizationHeader);
            Page<Ride> ridesPage = rideDAO.findAll(pageable);
            return ResponseEntity.ok().body(ridesPage);
        } catch (InvalidAuthRequest iae) {
            logger.error("Failed to get rides posted data {}", iae.getMessage(), iae);
            Page<Ride> ridesPage = rideDAO.findAll(pageable);
            ridesPage.forEach(ride -> ride.setPostedBy(null));
            return ResponseEntity.ok().body(ridesPage);

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
