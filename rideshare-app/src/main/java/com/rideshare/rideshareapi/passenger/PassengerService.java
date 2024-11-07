package com.rideshare.rideshareapi.passenger;

import com.rideshare.rideshareapi.account.exception.InvalidPassengerException;
import com.rideshare.rideshareapi.booking.Booking;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;

    PassengerService(PassengerRepository passengerRepository){
        this.passengerRepository=passengerRepository;
    }

    public Passenger getPassengerDetails(Long passengerId){
        Optional<Passenger> optionalPassenger= passengerRepository.findById(passengerId);
        if(optionalPassenger.isEmpty()){
            throw new InvalidPassengerException("No Passenger With Id:"+passengerId);
        }
        return optionalPassenger.get();
    }


}
