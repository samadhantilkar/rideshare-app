package com.rideshare.rideshareapi.driver;

import com.rideshare.rideshareapi.booking.BookingService;
import com.rideshare.rideshareapi.booking.DTO.RideRateRequestDTO;
import com.rideshare.rideshareapi.booking.Review.Review;
import com.rideshare.rideshareapi.driver.DTO.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver")
public class DriverController {
    private final DriverService driverService;
    private final BookingService bookingService;
    public DriverController(DriverService driverService,BookingService bookingService) {
        this.driverService = driverService;
        this.bookingService=bookingService;
    }

    @GetMapping("/{driverId}")
    public DriverDetailResponseDTO getDriverDetail(@PathVariable(name = "driverId")Long driverId){
        return driverService.getDriverDetail(driverId);
    }

    @GetMapping("{driverId}/bookings")
    public List<BookingDetailResponseDTO> getAllBookings(@PathVariable(name = "driverId")Long driverId){
        return driverService.getAllBookings(driverId);
    }

    @GetMapping("{driverId}/bookings/{bookingId}")
    public BookingDetailResponseDTO getBooking(@PathVariable(name = "driverId")Long driverId,
                                               @PathVariable(name = "bookingId")Long bookingId){
        return driverService.getDriverBookingFromId(driverId,bookingId);
    }

    @PostMapping("{driverId}/bookings/{bookingId}")
    public void acceptBooking(@PathVariable(name = "driverId")Long driverId,
                              @PathVariable(name = "bookingId")Long bookingId){
        bookingService.acceptBooking(driverId,bookingId);
    }

    @DeleteMapping("{driverId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "driverId")Long driverId,
                              @PathVariable(name = "bookingId")Long bookingId){
        bookingService.cancelByDriver(driverId,bookingId);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/start")
    public void startRide(@PathVariable(name = "driverId")Long driverId,
                          @PathVariable(name = "bookingId")Long bookingId,
                          @RequestBody startRideRequestDTO requestDTO){
        bookingService.startRide(driverId,bookingId,requestDTO);
    }

    @PatchMapping("{driverId}/bookings/{booking}/end")
    public void endRide(@PathVariable(name = "driverId")Long driverId,
                        @PathVariable(name = "bookingId")Long bookingId){
        bookingService.endRide(driverId,bookingId);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/rate")
    public void rateRide(@PathVariable(name = "driverId")Long driverId, @PathVariable(name = "bookingId")Long bookingId,
                         @RequestBody RideRateRequestDTO requestDTO){
        bookingService.rateRide(driverId,bookingId,requestDTO);
    }
}