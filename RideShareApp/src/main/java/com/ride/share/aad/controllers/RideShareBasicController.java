package com.ride.share.aad.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rs")
@CrossOrigin(origins = "http://localhost:3000")
public class RideShareBasicController {

    @GetMapping("/home/text")
    @ResponseBody
    public String homeText() {
        return "This is RideShare Home, Welcome";
    }

    @GetMapping("/public/home/text")
    @ResponseBody
    public String publicHomeText() {
        return "This is RideShare Home, Public";
    }
}
