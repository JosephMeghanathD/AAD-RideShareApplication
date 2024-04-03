package com.ride.share.aad.controllers.ride;


import com.ride.share.aad.config.scurity.exceptions.InvalidAuthRequest;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import com.ride.share.aad.utils.entity.RideUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/rs/ride")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareRideController {

    private static final Logger logger = LoggerFactory.getLogger(RideShareRideController.class);

    @PostMapping("/post")
    @ResponseBody
    public String postRide(@RequestBody String rideJson,
                            @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Ride ride = RideUtils.getRide(user, new JSONObject(rideJson));
        ride.save();
        return RideUtils.toJson(ride, true).toString();
    }

    @GetMapping("/{rideId}")
    @ResponseBody
    public String getRide(@PathVariable String rideId,
                            @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Ride ride = new Ride(rideId);
        return RideUtils.toJson(ride, true).toString();
    }

    @PutMapping("/{rideId}")
    @ResponseBody
    public String editRide(@PathVariable String rideId, @RequestBody String rideJson,
                            @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Ride ride = new Ride(rideId);
        if (!Objects.equals(ride.getPostedBy(), user.getUserId())) {
            throw new InvalidAuthRequest("Invalid userId for operation");
        }
        Ride updatedRide = RideUtils.getRide(user, new JSONObject(rideJson));
        ride.setDestination(updatedRide.getDestination());
        ride.setFare(updatedRide.getFare());
        ride.setStartingFromLocation(updatedRide.getStartingFromLocation());
        ride.setNumberOfPeople(updatedRide.getNumberOfPeople());
        ride.setTimeOfRide(updatedRide.getTimeOfRide());
        ride.setPostedAt(System.currentTimeMillis());
        ride.update();
        return RideUtils.toJson(ride, true).toString();
    }

    @GetMapping("/by/user")
    @ResponseBody
    public String getUserPostedRides(@RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        List<Ride> allRides = RideUtils.getRideByUser(user);
        return RideUtils.getAllRidesJson(allRides, true);
    }

    @PostMapping("/by/destination")
    @ResponseBody
    public String getRidesByDestination(@RequestBody String searchString,
                                     @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        List<Ride> allRides = RideUtils.getRideByDestinationOrSource(true, searchString);
        return RideUtils.getAllRidesJson(allRides, true);
    }

    @PostMapping("/by/source")
    @ResponseBody
    public String getRidesBySource(@RequestBody String searchString,
                                        @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        List<Ride> allRides = RideUtils.getRideByDestinationOrSource(false, searchString);
        return RideUtils.getAllRidesJson(allRides, true);
    }

    @DeleteMapping("/{rideId}")
    @ResponseBody
    public String deleteRide(@PathVariable String rideId,
                                   @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        Ride ride = new Ride(rideId);
        if (!Objects.equals(ride.getPostedBy(), user.getUserId())) {
            throw new InvalidAuthRequest("Invalid userId for operation");
        }
        ride.delete();
        return RideUtils.toJson(ride, true).toString();
    }

    @GetMapping("/rides")
    public String getRides(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "timeOfRide") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader
    ) throws InvalidAuthRequest {
        try {
            boolean validToken = RequestAuthUtils.isValidToken(authorizationHeader);
            List<Ride> allRides = RideUtils.getAllRides();
            JSONObject data = new JSONObject();
            JSONArray rides = new JSONArray();
            for (Ride allRide : allRides) {
                rides.put(RideUtils.toJson(allRide, validToken));
            }
            data.put("rides", rides);
            return data.toString();
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
