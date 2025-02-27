package com.rideshare.rideshareapi.passenger;

import com.rideshare.rideshareapi.booking.Booking;
import com.rideshare.rideshareapi.booking.BookingService;
import com.rideshare.rideshareapi.booking.BookingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passanger")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;
    private final BookingService bookingService;

    @GetMapping("/{passengerId}")
    public Passenger getPassengerDetails(@PathVariable(name = "passengerId")Long passengerId){
         return passengerService.getPassengerDetails(passengerId);
    }

    @GetMapping("{passengerId}/bookings/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable(name = "passengerId") Long passengerId,
                                              @PathVariable(name = "bookingId") Long bookingId){
       Booking booking= passengerService.getBooking(passengerId,bookingId);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/{passengerId}/bookings")
    public ResponseEntity<List<Booking>> getAllBooking(@PathVariable Long passengerId){
        List<Booking> bookings= passengerService.getAllBooking(passengerId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("{passengerId}/bookings")
    public ResponseEntity<Void> requestBooking(@PathVariable(name = "passengerId") Long passengerId,
                                               @RequestBody Booking data){
        passengerService.requestBooking(passengerId,data);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}")
    public void updateRoute(@PathVariable(name = "passengerId") Long passengerId,
                            @PathVariable(name = "bookingId") Long bookingId,
                            @RequestBody Booking data){
        bookingService.updateRoute(passengerId,bookingId,data);
    }

}


