package com.ride.share.aad.utils.entity;

import com.ride.share.aad.storage.dao.UserDAO;
import com.ride.share.aad.storage.entity.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserUtils {

    @Autowired
    UserDAO userDAO;

}
