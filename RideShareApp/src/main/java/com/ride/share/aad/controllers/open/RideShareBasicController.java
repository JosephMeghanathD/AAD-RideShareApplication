package com.ride.share.aad.controllers.open;

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
}
