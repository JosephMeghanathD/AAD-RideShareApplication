package com.ride.share.aad.controllers;

import com.ride.share.aad.config.exceptions.InvalidAuthRequest;
import com.ride.share.aad.payload.ContactRequest;
import com.ride.share.aad.services.EmailService;
import com.ride.share.aad.storage.entity.User;
import com.ride.share.aad.utils.auth.RequestAuthUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rs")
@CrossOrigin(origins = "*")
public class RideShareBasicController {
    private static final Logger logger = LoggerFactory.getLogger(RideShareBasicController.class);


    @Autowired
    RequestAuthUtils requestAuthUtils;

    @Autowired
    private EmailService emailService;

    @GetMapping("/home/text")
    @ResponseBody
    public String homeText(@RequestHeader(name = "Authorization", required = false) @DefaultValue("XXX") String authorizationHeader) throws InvalidAuthRequest {
        User user = requestAuthUtils.getUser(authorizationHeader);
        return "This is RideShare Home, Welcome " + user.getUserName();
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

    @PostMapping("/contact")
    public ResponseEntity<String> sendContactEmail(@Valid @RequestBody ContactRequest contactRequest) {
        try {
            String recipientEmail = "merlinmegha005@gmail.com";

            // Build a more detailed and user-friendly email body from the structured data
            String emailBody = String.format(
                    "You have a new message from:\n\nName: %s\nEmail: %s\n\nMessage:\n---\n%s",
                    contactRequest.name(),
                    contactRequest.email(),
                    contactRequest.message()
            );

            emailService.sendSimpleMessage(recipientEmail, contactRequest.subject(), emailBody);

            return ResponseEntity.ok("Thank you for your message! It has been sent successfully.");
        } catch (Exception e) {
            // Log the actual error for debugging
            logger.error("Failed to send email: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sorry, there was an error sending your message. Please try again later.");
        }
    }

}
