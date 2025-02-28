package com.rideshare.rideshareapi.account.exception;

import com.rideshare.rideshareapi.comman.ApplicationException;

public class InvalidAccountException extends ApplicationException {
    public InvalidAccountException(String message) {
        super(message);
    }
}
