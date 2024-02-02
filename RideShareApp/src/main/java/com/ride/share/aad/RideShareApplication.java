package com.ride.share.aad;

import com.ride.share.aad.storage.service.CassandraStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RideShareApplication {

    @Autowired
    public static void main(String[] args) {
        SpringApplication.run(RideShareApplication.class, args);
        CassandraStorageService.init();
    }

}
