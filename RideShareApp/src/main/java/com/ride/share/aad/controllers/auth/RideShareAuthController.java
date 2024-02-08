package com.ride.share.aad.controllers.auth;

import com.ride.share.aad.storage.entity.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rs/public")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareAuthController {
    private static final Logger logger = LoggerFactory.getLogger(RideShareAuthController.class);


    @GetMapping("/login")
    @ResponseBody
    public String homeText() {
        return "This is RideShare Home, Welcome";
    }

    @PostMapping("/signUp")
    @ResponseBody
    public String publicHomeText(@RequestBody String userDataString) throws Exception {
        User user = User.getUser(new JSONObject(userDataString));
        user.save();
        return user.toJson().toString();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
