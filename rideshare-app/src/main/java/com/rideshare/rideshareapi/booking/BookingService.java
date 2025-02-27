package com.rideshare.rideshareapi.booking;

import com.rideshare.rideshareapi.booking.DTO.*;
import com.rideshare.rideshareapi.driver.DTO.startRideRequestDTO;
import com.rideshare.rideshareapi.passenger.Passenger;

import java.util.List;

public interface BookingService {
    void createBooking(Booking booking);

    RequestBookingResponseDTO requestBooking(Long passengerId, RequestBookingRequestDTO requestDTO);

    List<Booking> getAllBookings(Long passengerId);

    Booking getBooking(Long passengerId, Long bookingId);

    UpdateRouteResponseDTO updateRoute(Long passengerId, Long bookingId, UpdateRouteRequestDTO requestDTO);

    void retryBooking(Long passengerId, Long bookingId);

    void cancelBooking(Long passengerId, Long bookingId);

    void acceptBooking(Long driverId, Long bookingId);

    Booking findByBookingId(Long bookingId);

    void cancelByDriver(Long driverId, Long bookingId);

    void startRide(Long driverId, Long bookingId, startRideRequestDTO requestDTO);

    void endRide(Long driverId, Long bookingId);

    void rateRide(Long driverId, Long bookingId, RideRateRequestDTO requestDTO);
}
