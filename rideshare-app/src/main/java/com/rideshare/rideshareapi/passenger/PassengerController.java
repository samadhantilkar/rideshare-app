package com.rideshare.rideshareapi.passenger;

import com.rideshare.rideshareapi.booking.Booking;
import com.rideshare.rideshareapi.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/passanger")
public class PassengerController {
    PassengerService passengerService;

    @Autowired
    PassengerController(PassengerService passengerService){
        this.passengerService=passengerService;
    }

    @GetMapping("/{passengerId}")
    public Passenger getPassengerDetails(@PathVariable(name = "passengerId")Long passengerId){
         return passengerService.getPassengerDetails(passengerId);
    }
}
