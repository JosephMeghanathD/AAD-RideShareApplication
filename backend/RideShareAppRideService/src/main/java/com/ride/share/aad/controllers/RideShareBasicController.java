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

    @ExceptionHandler(InvalidAuthRequest.class)
    public ResponseEntity<ErrorResponse> handleAuthException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/health")
    public ResponseEntity<List<String>> health() {
        return new ResponseEntity<>(List.of("healthy"), HttpStatus.OK);
    }
}
