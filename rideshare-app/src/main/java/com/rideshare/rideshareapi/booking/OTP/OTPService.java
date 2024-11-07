package com.rideshare.rideshareapi.booking.OTP;

public interface OTPService {
    void sendPhoneNumberConfirmationOPT(OTP otp);

    void sendRideStartOTP(OTP otp);

    OTP generate(String phoneNumber);
}
