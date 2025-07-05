package com.ride.share.aad.utils.auth;

import com.ride.share.aad.config.exceptions.InvalidAuthRequest;
import com.ride.share.aad.storage.dao.TokenDAO;
import com.ride.share.aad.storage.dao.UserDAO;
import com.ride.share.aad.storage.entity.Token;
import com.ride.share.aad.storage.entity.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RequestAuthUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestAuthUtils.class);

    @Autowired
    UserDAO userDAO;

    @Autowired
    TokenDAO tokenDAO;

    private static String generateToken(User user) {
        return UUID.randomUUID().toString();
    }

    public Token login(JSONObject userLoginRequest, String role) throws InvalidAuthRequest {
        String userName = userLoginRequest.getString("username");
        Optional<User> user = userDAO.findByUserName(userName);
        if (user.isPresent() && user.get().validate(userLoginRequest.getString("password"))) {
            user.get().setLastSeen(System.currentTimeMillis());
            userDAO.save(user.get());
            Token token = new Token(user.get(), generateToken(user.get()), Token.UserRole.valueOf(role));
            tokenDAO.save(token);
            return token;
        }
        throw new InvalidAuthRequest("Invalid username or password");
    }

    public boolean isValidToken(String authorizationHeader) throws InvalidAuthRequest {
        Optional<Token> t = tokenDAO.findByJwtToken(authorizationHeader);
        return t.isPresent();
    }

    public User getUser(String authorizationHeader) throws InvalidAuthRequest {
        Optional<Token> t = tokenDAO.findByJwtToken(authorizationHeader);
        if (t.isEmpty()) {
            logger.error("Invalid token invalid auth found: {}", authorizationHeader);
            throw new InvalidAuthRequest("Invalid auth expected: 1 found: "+ t);
        }
        t.get().getUser().setLastSeen(System.currentTimeMillis());
        userDAO.save(t.get().getUser());
        return t.get().getUser();
    }
}
