package com.rideshare.rideshareapi.booking.exception;

import com.rideshare.rideshareapi.comman.ApplicationException;

public class InvalidOTPException extends ApplicationException {
  public InvalidOTPException(){
    super("Invalid OTP");
  }
}
