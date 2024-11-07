package com.rideshare.rideshareapi.booking.OTP;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class ConsoleOTPService implements OTPService {
    private static final Random r=new Random();

    @Override
    public void sendPhoneNumberConfirmationOPT(OTP otp) {
        System.out.printf("Confirm Phone Number %s: OTP - %s",otp.getSendToNumber(),otp.getCode());
    }

    @Override
    public void sendRideStartOTP(OTP otp) {
        System.out.printf("%s - share this opt with your driver to start the ride %s",otp.getSendToNumber(),otp.getCode());
    }

    public OTP generate(String phoneNumber) {
        return OTP.builder()
                .code(r.ints(9000).toString())
                .sendToNumber(phoneNumber)
                .build();
    }
}
