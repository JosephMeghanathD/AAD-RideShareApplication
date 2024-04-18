package com.ride.share.aad.controllers.auth;

import com.ride.share.aad.config.exceptions.InvalidAuthRequest;
import com.ride.share.aad.storage.dao.RideDAO;
import com.ride.share.aad.storage.dao.UserDAO;
import com.ride.share.aad.storage.entity.Token;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rs")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareAuthController {
    private static final Logger logger = LoggerFactory.getLogger(RideShareAuthController.class);

    @Autowired
    UserDAO userDAO;

    @Autowired
    RideDAO rideDAO;

    @Autowired
    RequestAuthUtils requestAuthUtils;

    @GetMapping("/get/{userID}")
    @ResponseBody
    public User getUser(@PathVariable("userID") String userID,
                          @RequestHeader("Authorization") @DefaultValue("XXX") String authorizationHeader) throws Exception {
        User user = requestAuthUtils.getUser(authorizationHeader);
        return user;
    }

    @PostMapping("/public/login")
    @ResponseBody
    public Token logIn(@RequestBody String userDataString, @RequestParam(value = "role", defaultValue = "User") String role) throws Exception {
        return requestAuthUtils.login(new JSONObject(userDataString), role);
    }


    @PostMapping("/public/signUp")
    @ResponseBody
    public User signUp(@RequestBody User user) throws Exception {
        userDAO.save(user);
        return user;
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidAuthRequest.class)
    public ResponseEntity<ErrorResponse> handleAuthException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
