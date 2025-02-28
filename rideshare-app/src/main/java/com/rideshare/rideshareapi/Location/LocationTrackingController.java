package com.rideshare.rideshareapi.Location;

import com.rideshare.rideshareapi.driver.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationTrackingController {

    private final LocationTrackingService locationTrackingService;

    @PutMapping("/driver/{driverId}")
    public ResponseEntity<Void> updateDriveLocation(@PathVariable Long driverId, @RequestBody ExactLocation data){
        locationTrackingService.updateDriverLocation(driverId,data);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/passenger/{passengerId}")
    public ResponseEntity<Void> updatePassengerLocation(@PathVariable Long passengerId, @RequestBody ExactLocation location){
        //Only trigger every 30 second if the passenger is active
        locationTrackingService.updatePassengerLocation(passengerId,location);
        return ResponseEntity.noContent().build();
    }
}
