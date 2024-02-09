package com.ride.share.aad.config.scurity;

public class InvalidAuthRequest extends Exception {
    private final String message;

    public InvalidAuthRequest(String message) {
        super(message);
        this.message = message;
    }
}
