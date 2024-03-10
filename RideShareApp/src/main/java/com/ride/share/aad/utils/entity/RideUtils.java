package com.ride.share.aad.utils.entity;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.ride.share.aad.storage.service.CassandraStorageService.getCqlSession;

public class RideUtils {

    public static List<Ride> getAllRides() {
        return Ride.RidesDAO.getAllRides();
    }


    public static Ride getRide(User user, JSONObject rideJson) {
        return new Ride(rideJson.getString("startingFromLocation"), rideJson.getString("destination"),
                rideJson.getInt("numberOfPeople"), rideJson.getDouble("fare"),
                rideJson.getLong("timeOfRide"), user.getUserId(), System.currentTimeMillis());
    }

    public static JSONObject toJson(Ride ride, boolean validToken) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("rideId", ride.getRideId());
        json.put("startingFromLocation", ride.getStartingFromLocation());
        json.put("destination", ride.getDestination());
        json.put("numberOfPeople", ride.getNumberOfPeople());
        json.put("fare", ride.getFare());
        json.put("timeOfRide", ride.getTimeOfRide());
        if (validToken) {
            json.put("postedBy", ride.getPostedBy());
        }
        json.put("postedAt", ride.getPostedAt());
        return json;
    }

    public static List<Ride> getRideByUser(User user) {
        return Ride.RidesDAO.getAllRideByUser(user);
    }
    public static List<Ride> getRideByDestinationOrSource(boolean type, String searchString) {
        return Ride.RidesDAO.getAllRideByDestinationOrSource(type, searchString);
    }

    public static List<Ride> getRides(BoundStatement boundStatement, List<Ride> rideList, CqlSession cqlSession) {
        cqlSession.execute(boundStatement).forEach(row -> rideList.add(mapToEntity(null, row)));
        return rideList;
    }

    public static List<Ride> getRides(BoundStatement boundStatement, List<Ride> rideList) {
        CqlSession cqlSession = getCqlSession();
        return getRides(boundStatement, rideList, cqlSession);
    }

    public static Ride mapToEntity(Ride ride, Row row) {
        if (ride == null) {
            ride = new Ride();
        }
        ride.setRideId(row.getString("rideId"));
        ride.setStartingFromLocation(row.getString("startingFromLocation"));
        ride.setDestination(row.getString("destination"));
        ride.setNumberOfPeople(row.getInt("numberOfPeople"));
        ride.setFare(row.getDouble("fare"));
        ride.setTimeOfRide(row.getLong("timeOfRide"));
        ride.setPostedBy(row.getString("postedBy"));
        ride.setPostedAt(row.getLong("postedAt"));
        return ride;
    }

    public static String getAllRidesJson(List<Ride> allRides, boolean validToken) {
        JSONObject data = new JSONObject();
        JSONArray rides = new JSONArray();
        for (Ride allRide : allRides) {
            rides.put(toJson(allRide, validToken));
        }
        data.put("rides", rides);
        return data.toString();
    }
}
