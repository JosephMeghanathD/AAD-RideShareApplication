package com.ride.share.aad.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rs/public")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareAuthController {

    @GetMapping("/login")
    @ResponseBody
    public String homeText() {
        return "This is RideShare Home, Welcome";
    }

    @GetMapping("/signUp")
    @ResponseBody
    public String publicHomeText() {
        return "This is RideShare Home, Public";
    }
}
