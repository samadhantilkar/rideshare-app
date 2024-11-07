package com.rideshare.rideshareapi.booking;

public enum BookingStatus {
    CANCELLING("The Booking has been cancelled due to one of many reasons"),
    SCHEDULED("The Booking is Schedule for some time in future"),
    ASSIGNING_DRIVER("The Passenger has requested for booking, but a driver is yet to be assigned"),
    REACHING_PICKUP_LOCATION("The driver has been assigned and is own their way to pickup location"),
    CAB_ARRIVED("The driver has arrived at pickup location and is waiting for driver "),
    IN_RIDE("The ride is currently in progress"),
    COMPLETED("The ride has already been completed");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }
}
