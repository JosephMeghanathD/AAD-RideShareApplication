package com.ride.share.aad.utils.entity;

import com.ride.share.aad.storage.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUtils {

    @Autowired
    UserDAO userDAO;

}
