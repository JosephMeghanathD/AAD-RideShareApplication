package com.ride.share.aad.storage.entity;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.storage.dao.AbstractCassandraDAO;
import com.ride.share.aad.utils.entity.RideUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class Ride {

    public static final String RIDES_TABLE = "rides";

    static {
        RidesDAO ridesDAO = new RidesDAO();
        ridesDAO.createTable();
    }

    transient RidesDAO ridesDAO;

    String rideId;
    String startingFromLocation;
    String destination;
    long numberOfPeople = 1;
    double fare;
    long timeOfRide;

    // meta data
    String postedBy; // userid of the person who posted it
    long postedAt; // when this ride message was posted

    public Ride() {
        this.ridesDAO = new RidesDAO();
    }

    public Ride(String rideId) {
        this();
        this.rideId = rideId;
        ridesDAO.mapToEntity(rideId, this);
    }

    public Ride(String startingFromLocation, String destination, int numberOfPeople, double fare, long timeOfRide, String postedBy, long postedAt) {
        this();
        this.rideId = UUID.randomUUID().toString();
        this.startingFromLocation = startingFromLocation;
        this.destination = destination;
        this.numberOfPeople = numberOfPeople;
        this.fare = fare;
        this.timeOfRide = timeOfRide;
        this.postedBy = postedBy;
        this.postedAt = postedAt;
    }

    public Ride save() {
        ridesDAO.insert(rideId, rideId, startingFromLocation, destination, numberOfPeople, fare, timeOfRide, postedBy, postedAt);
        return this;
    }

    public Ride delete() {
        ridesDAO.delete(rideId);
        return this;
    }

    public Ride update() {
        ridesDAO.update(rideId, startingFromLocation, destination, numberOfPeople, fare, timeOfRide, postedBy, postedAt, rideId);
        return this;
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

    public Long getNumberOfPeople() {
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

    public void setRidesDAO(RidesDAO ridesDAO) {
        this.ridesDAO = ridesDAO;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Ride.class.getSimpleName() + "[", "]")
                .add("rideId='" + rideId + "'")
                .add("startingFromLocation='" + startingFromLocation + "'")
                .add("destination='" + destination + "'")
                .add("numberOfPeople=" + numberOfPeople)
                .add("fare=" + fare)
                .add("timeOfRide=" + timeOfRide)
                .add("postedBy='" + postedBy + "'")
                .add("postedAt=" + postedAt)
                .toString();
    }

    public static class RidesDAO extends AbstractCassandraDAO<Ride> {

        public static final PreparedStatement CREATE_STMT = getCqlSession().prepare("CREATE TABLE IF NOT EXISTS " +
                RIDES_TABLE + " " + "(rideId TEXT PRIMARY KEY, startingFromLocation TEXT, destination TEXT, " +
                "numberOfPeople BIGINT, fare DOUBLE, timeOfRide BIGINT, postedBy TEXT, postedAt BIGINT)");
        public static PreparedStatement INSERT_STMT;
        public static PreparedStatement UPDATE_STMT;
        public static PreparedStatement DELETE_STMT;
        public static PreparedStatement SELECT_STMT;

        public static List<Ride> getAllRides() {
            List<Ride> rideList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + RIDES_TABLE).bind();
            return RideUtils.getRides(boundStatement, rideList);
        }

        public static List<Ride> getAllRideByUser(User user) {
            List<Ride> rideList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + RIDES_TABLE + " WHERE postedBy='" + user.getUserId() + "' ALLOW FILTERING;").bind();
            Logger logger = LoggerFactory.getLogger(Ride.class);
            logger.info(boundStatement.getPreparedStatement().getQuery());
            return RideUtils.getRides(boundStatement, rideList);
        }

        public static List<Ride> getAllRideByDestinationOrSource(boolean type, String searchString) {
            List<Ride> rideList = new ArrayList<>();
            BoundStatement boundStatement = getCqlSession().prepare("SELECT * FROM " + RIDES_TABLE + " WHERE " +
                    (type ? "destination" : "startingFromLocation") + " LIKE '%" + searchString + "%'").bind();
            return RideUtils.getRides(boundStatement, rideList);
        }

        @Override
        public PreparedStatement getCreateStmt() {
            return CREATE_STMT;
        }

        @Override
        public PreparedStatement getInsertStmt() {
            if (INSERT_STMT == null) {
                INSERT_STMT = getCqlSession().prepare("INSERT INTO " + RIDES_TABLE
                        + " (rideId, startingFromLocation, destination, numberOfPeople, fare, timeOfRide, postedBy, postedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            }
            return INSERT_STMT;
        }

        @Override
        public PreparedStatement getUpdateStmt() {
            if (UPDATE_STMT == null) {
                UPDATE_STMT = getCqlSession().prepare("UPDATE " + RIDES_TABLE
                        + " SET startingFromLocation = ?, destination = ?, numberOfPeople = ?, fare = ?, timeOfRide = ?, postedBy = ?, postedAt = ? WHERE rideId = ?");
            }
            return UPDATE_STMT;
        }

        @Override
        public PreparedStatement getDeleteStmt() {
            if (DELETE_STMT == null) {
                DELETE_STMT = getCqlSession().prepare("DELETE FROM " + RIDES_TABLE + " WHERE rideId = ?");
            }
            return DELETE_STMT;
        }

        @Override
        public PreparedStatement getStmt() {
            if (SELECT_STMT == null) {
                SELECT_STMT = getCqlSession().prepare("SELECT * FROM " + RIDES_TABLE + " WHERE rideId = ?");
            }
            return SELECT_STMT;
        }

        @Override
        public Ride mapToEntity(String key, Ride ride) {
            Row row = get(key).one();

            if (row != null) {
                return RideUtils.mapToEntity(ride, row);
            }
            return null;
        }

    }

}
