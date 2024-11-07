package com.rideshare.rideshareapi.booking.exception;

import com.rideshare.rideshareapi.comman.model.ApplicationException;

public class InvalidActionForBookingStateException extends ApplicationException {
    public InvalidActionForBookingStateException(String message){
        super(message);
    }
}
