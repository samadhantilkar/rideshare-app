package com.rideshare.rideshareapi.driver.DTO;

import com.rideshare.rideshareapi.Location.ExactLocation;
import com.rideshare.rideshareapi.booking.BookingStatus;
import com.rideshare.rideshareapi.booking.BookingType;
import com.rideshare.rideshareapi.booking.Review.Review;
import com.rideshare.rideshareapi.driver.Driver;
import com.rideshare.rideshareapi.passenger.Passenger;
import com.rideshare.rideshareapi.payment.PaymentReceipt;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Getter @Setter
@Builder
public class BookingDetailResponseDTO {
    private Passenger passenger;

    private Driver driver;

    private BookingType bookingType;

    private Review reviewByPassenger;

    private BookingStatus bookingStatus;

    private Review reviewByDriver;

    private PaymentReceipt paymentReceipt;

    private List<ExactLocation> completedRoute =new ArrayList<>();

    private Date scheduleTime;

    private Date startTime;

    private Date endTime;

    private Long totalDistanceMeters;
}
