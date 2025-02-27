package com.rideshare.rideshareapi.passenger;

import com.rideshare.rideshareapi.booking.Booking;

import java.util.List;

public interface PassengerService {

    public Passenger getPassengerDetails(Long passengerId);

    List<Booking> getAllBooking(Long passengerId);

    Booking getBooking(Long passengerId, Long bookingId);

    void requestBooking(Long passengerId, Booking data);
}
