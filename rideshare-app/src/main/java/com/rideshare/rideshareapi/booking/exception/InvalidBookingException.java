package com.rideshare.rideshareapi.booking.exception;

import com.rideshare.rideshareapi.comman.ApplicationException;

public class InvalidBookingException extends ApplicationException {
    public InvalidBookingException(String message) {
        super(message);
    }
}
