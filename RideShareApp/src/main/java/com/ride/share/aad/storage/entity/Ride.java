package com.ride.share.aad.storage.entity;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.storage.dao.AbstractCassandraDAO;
import org.json.JSONException;
import org.json.JSONObject;

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
    int numberOfPeople = 1;
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

    public static List<Ride> getAllRides() {
        RidesDAO ridesDAO = new RidesDAO();
        return ridesDAO.getAllRides();
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
        ridesDAO.update(rideId, startingFromLocation, destination, numberOfPeople, fare, timeOfRide, postedBy, postedAt);
        return this;
    }

    public String getRideId() {
        return rideId;
    }

    public String getStartingFromLocation() {
        return startingFromLocation;
    }

    public String getDestination() {
        return destination;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public double getFare() {
        return fare;
    }

    public long getTimeOfRide() {
        return timeOfRide;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public long getPostedAt() {
        return postedAt;
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

    public JSONObject toJson(boolean validToken) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("rideId", this.rideId);
        json.put("startingFromLocation", this.startingFromLocation);
        json.put("destination", this.destination);
        json.put("numberOfPeople", this.numberOfPeople);
        json.put("fare", this.fare);
        json.put("timeOfRide", this.timeOfRide);
        if (validToken) {
            json.put("postedBy", this.postedBy);
        }
        json.put("postedAt", this.postedAt);
        return json;
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
            getCqlSession().execute(boundStatement).forEach(row -> {
                Ride ride = new Ride();
                ride.rideId = row.getString("rideId");
                ride.startingFromLocation = row.getString("startingFromLocation");
                ride.destination = row.getString("destination");
                ride.numberOfPeople = row.getInt("numberOfPeople");
                ride.fare = row.getDouble("fare");
                ride.timeOfRide = row.getLong("timeOfRide");
                ride.postedBy = row.getString("postedBy");
                ride.postedAt = row.getLong("postedAt");
                rideList.add(ride);
            });
            return rideList;
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
                if (ride == null) {
                    ride = new Ride();
                }
                ride.rideId = row.getString("rideId");
                ride.startingFromLocation = row.getString("startingFromLocation");
                ride.destination = row.getString("destination");
                ride.numberOfPeople = row.getInt("numberOfPeople");
                ride.fare = row.getDouble("fare");
                ride.timeOfRide = row.getLong("timeOfRide");
                ride.postedBy = row.getString("postedBy");
                ride.postedAt = row.getLong("postedAt");
                return ride;
            }
            return null;
        }
    }
}
