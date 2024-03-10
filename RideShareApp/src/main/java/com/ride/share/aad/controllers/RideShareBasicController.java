package com.ride.share.aad.controllers;

import com.ride.share.aad.config.scurity.exceptions.InvalidAuthRequest;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import com.ride.share.aad.utils.entity.RideUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rs")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareBasicController {

    @GetMapping("/home/text")
    @ResponseBody
    public String homeText(@RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = RequestAuthUtils.getUser(authorizationHeader);
        return "This is RideShare Home, Welcome " + user.getUserId();
    }

    @GetMapping("/public/home/text")
    @ResponseBody
    public String publicHomeText() {
        return "This is RideShare Home, Public";
    }

    @GetMapping("/rides")
    public String getRides(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "timeOfRide") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader
    ) throws InvalidAuthRequest {
        boolean validToken = RequestAuthUtils.isValidToken(authorizationHeader);
        List<Ride> allRides = RideUtils.getAllRides();
        JSONObject data = new JSONObject();
        JSONArray rides = new JSONArray();
        for (Ride allRide : allRides) {
            rides.put(RideUtils.toJson(allRide, validToken));
        }
        data.put("rides", rides);
        return data.toString();
    }

    @ExceptionHandler(InvalidAuthRequest.class)
    public ResponseEntity<ErrorResponse> handleAuthException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
