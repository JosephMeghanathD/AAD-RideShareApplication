package com.ride.share.aad.utils.entity;


import com.ride.share.aad.storage.entity.Ride;
import com.ride.share.aad.storage.entity.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideUtils {


    public static Ride getRide(User user, JSONObject rideJson) {
        return new Ride(rideJson.getString("startingFromLocation"), rideJson.getString("destination"),
                rideJson.getInt("numberOfPeople"), rideJson.getDouble("fare"),
                rideJson.getLong("timeOfRide"), user, System.currentTimeMillis());
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
