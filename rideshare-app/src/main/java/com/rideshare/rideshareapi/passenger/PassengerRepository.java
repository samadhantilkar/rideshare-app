package com.rideshare.rideshareapi.passenger;

import com.rideshare.rideshareapi.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger,Long> {

    List<Booking> findByBookings(Long passengerId);
}
