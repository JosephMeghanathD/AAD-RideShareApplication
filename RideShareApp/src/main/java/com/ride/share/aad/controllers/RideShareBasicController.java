package com.ride.share.aad.controllers;

import com.ride.share.aad.config.scurity.InvalidAuthRequest;
import com.ride.share.aad.config.scurity.RequestAuthHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rs")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareBasicController {

    @GetMapping("/home/text")
    @ResponseBody
    public String homeText(@RequestHeader("Authorization") String authorizationHeader) throws InvalidAuthRequest {
        if (!RequestAuthHelper.isValidToken(authorizationHeader)) {
            throw new InvalidAuthRequest("Need to login before getting data");
        }
        return "This is RideShare Home, Welcome";
    }

    @GetMapping("/public/home/text")
    @ResponseBody
    public String publicHomeText() {
        return "This is RideShare Home, Public";
    }

    @GetMapping("/text")
    public String getRides(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "timeOfRide") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        return "";
    }
    @ExceptionHandler(InvalidAuthRequest.class)
    public ResponseEntity<ErrorResponse> handleAuthException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
