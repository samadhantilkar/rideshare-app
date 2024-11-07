package com.rideshare.rideshareapi.account.exception;

import com.rideshare.rideshareapi.comman.model.ApplicationException;

public class InvalidPassengerException extends ApplicationException {
    public InvalidPassengerException(String message) {
        super(message);
    }
}
