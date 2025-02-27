package com.rideshare.rideshareapi.Location;

public interface LocationTrackingService {
    void updateDriverLocation(Long driverId, ExactLocation data);

    void updatePassengerLocation(Long passengerId, ExactLocation location);
}
