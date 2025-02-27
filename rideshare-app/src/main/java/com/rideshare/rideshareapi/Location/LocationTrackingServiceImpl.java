package com.rideshare.rideshareapi.Location;

import com.rideshare.rideshareapi.account.exception.InvalidPassengerException;
import com.rideshare.rideshareapi.constant.Constants;
import com.rideshare.rideshareapi.driver.Driver;
import com.rideshare.rideshareapi.driver.DriverRepository;
import com.rideshare.rideshareapi.driver.Exception.InvalidDriverException;
import com.rideshare.rideshareapi.messageQueue.MQMessage;
import com.rideshare.rideshareapi.messageQueue.MessageQueue;
import com.rideshare.rideshareapi.passenger.Passenger;
import com.rideshare.rideshareapi.passenger.PassengerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationTrackingServiceImpl implements LocationTrackingService{

    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final MessageQueue messageQueue;
    private final Constants constants;

    private Driver getDriverFromId(Long driverId){
        return driverRepository.findById(driverId).orElseThrow(()-> new InvalidDriverException("No Driver found with Id: "+driverId));
    }

    @Override
    public void updateDriverLocation(Long driverId, ExactLocation data) {
        //once every 3 second for each active driver
        Driver driver=getDriverFromId(driverId);
//        TODO: check if the driver has an active booking
//               update the booking completedRoute based on the driver's location
//               update the expected complete time

        ExactLocation location= ExactLocation.builder()
                .longitude(data.getLongitude())
                .latitude(data.getLatitude())
                .build();

        messageQueue.sendMessage(
                constants.getDriverMatchingTopicName(),
                new LocationTrackingServiceImpl.Message(driver,location)
        );
    }

    @Override
    public void updatePassengerLocation(Long passengerId, ExactLocation location) {
        Passenger passenger=getPassengerFromId(passengerId);
        passenger.setLastKnownLocation(ExactLocation.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build());
        passengerRepository.save(passenger);
    }

    @AllArgsConstructor
    public static class Message implements MQMessage{
        private Driver driver;
        private ExactLocation location;
    }

    private Passenger getPassengerFromId(Long passengerId){
        return passengerRepository.findById(passengerId).orElseThrow( ()-> new InvalidPassengerException("No Passenger found with Id:"+passengerId));
    }


}
