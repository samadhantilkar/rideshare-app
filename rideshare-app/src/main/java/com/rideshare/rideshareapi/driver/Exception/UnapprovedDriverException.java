package com.rideshare.rideshareapi.driver.Exception;

import com.rideshare.rideshareapi.comman.ApplicationException;

public class UnapprovedDriverException extends ApplicationException {
    public UnapprovedDriverException(String message) {
        super(message);
    }
}
