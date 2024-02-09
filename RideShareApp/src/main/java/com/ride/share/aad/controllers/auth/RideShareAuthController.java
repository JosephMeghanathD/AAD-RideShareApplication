package com.ride.share.aad.controllers.auth;

import com.ride.share.aad.config.scurity.InvalidAuthRequest;
import com.ride.share.aad.config.scurity.RequestAuthHelper;
import com.ride.share.aad.storage.entity.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rs/public")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareAuthController {
    private static final Logger logger = LoggerFactory.getLogger(RideShareAuthController.class);


    @PostMapping("/login")
    @ResponseBody
    public String logIn(@RequestBody String userDataString, @RequestParam(value = "role", defaultValue = "User") String role) throws Exception {
        try {
            return RequestAuthHelper.login(new JSONObject(userDataString), role);
        } catch (Exception e) {
            logger.error("Failed to login", e);
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/signUp")
    @ResponseBody
    public String signUp(@RequestBody String userDataString) throws Exception {
        User user = User.getUser(new JSONObject(userDataString));
        user.save();
        return user.toJson().toString();
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
