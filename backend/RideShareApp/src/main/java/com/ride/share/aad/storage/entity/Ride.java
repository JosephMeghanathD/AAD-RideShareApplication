package com.ride.share.aad.storage.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "rides")
public class Ride {

    public static final String RIDES_TABLE = "rides";


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "VARCHAR(36)")
    String rideId;
    String startingFromLocation;
    String destination;
    long numberOfPeople = 1;
    double fare;
    long timeOfRide;

    @ManyToOne
    String postedBy; // userid of the person who posted it
    long postedAt; // when this ride message was posted

    public Ride() {
    }

    public Ride(String startingFromLocation, String destination, int numberOfPeople, double fare, long timeOfRide, String postedBy, long postedAt) {
        this.rideId = UUID.randomUUID().toString();
        this.startingFromLocation = startingFromLocation;
        this.destination = destination;
        this.numberOfPeople = numberOfPeople;
        this.fare = fare;
        this.timeOfRide = timeOfRide;
        this.postedBy = postedBy;
        this.postedAt = postedAt;
    }

    public Ride(String rideId, String startingFromLocation, String destination, long numberOfPeople, double fare, long timeOfRide, String postedBy, long postedAt) {
        this.rideId = rideId;
        this.startingFromLocation = startingFromLocation;
        this.destination = destination;
        this.numberOfPeople = numberOfPeople;
        this.fare = fare;
        this.timeOfRide = timeOfRide;
        this.postedBy = postedBy;
        this.postedAt = postedAt;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getStartingFromLocation() {
        return startingFromLocation;
    }

    public void setStartingFromLocation(String startingFromLocation) {
        this.startingFromLocation = startingFromLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(long numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public long getTimeOfRide() {
        return timeOfRide;
    }

    public void setTimeOfRide(long timeOfRide) {
        this.timeOfRide = timeOfRide;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }
}
