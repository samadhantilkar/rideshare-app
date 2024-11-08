package com.rideshare.rideshareapi.driver.Exception;

import com.rideshare.rideshareapi.comman.ApplicationException;

public class InvalidDriverException extends ApplicationException {
    public InvalidDriverException(String message){
        super(message);
    }
}
