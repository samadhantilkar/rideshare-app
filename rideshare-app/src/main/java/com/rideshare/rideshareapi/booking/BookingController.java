package com.rideshare.rideshareapi.booking;

import com.rideshare.rideshareapi.booking.DTO.RequestBookingRequestDTO;
import com.rideshare.rideshareapi.booking.DTO.RequestBookingResponseDTO;
import com.rideshare.rideshareapi.booking.DTO.UpdateRouteRequestDTO;
import com.rideshare.rideshareapi.booking.DTO.UpdateRouteResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingController {

    private final BookingServiceImpl bookingServiceImpl;

    BookingController(BookingServiceImpl bookingServiceImpl){
        this.bookingServiceImpl = bookingServiceImpl;
    }

    @PostMapping("{passengerId}/bookings/")
    public RequestBookingResponseDTO requestBooking(@PathVariable(name = "passengerId") Long passengerId,
                                                    @RequestBody RequestBookingRequestDTO requestDTO){
        return bookingServiceImpl.requestBooking(passengerId,requestDTO);

    }

    @PatchMapping("{passengerId}/bookings/{bookingId}")
    public UpdateRouteResponseDTO updateRoute(@PathVariable(name = "passengerId") Long passengerId,
                                              @PathVariable(name = "bookingId")Long BookingId,
                                              @RequestBody UpdateRouteRequestDTO requestDTO){
        return bookingServiceImpl.updateRoute(passengerId,BookingId, requestDTO);
    }

    @GetMapping("{passengerId}/booking/{bookingId}")
    public Booking getBooking(@PathVariable(name = "passengerId")Long passengerId,
                              @PathVariable(name = "bookingId")Long bookingId){
        return bookingServiceImpl.getBooking(passengerId,bookingId);
    }

    @GetMapping("{passengerID}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name = "passengerId")Long passengerId){
        return bookingServiceImpl.getAllBookings(passengerId);
    }

    @PostMapping("{passengerId}/bookings/{bookingId}")
    public void retryBooking(@PathVariable(name = "passengerId")Long passengerId,
                             @PathVariable(name = "bookingId")Long bookingId){
        bookingServiceImpl.retryBooking(passengerId,bookingId);
    }

    @DeleteMapping("{passengerId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "passengerId") Long passengerId,
                              @PathVariable(name = "BookingId") Long bookingId){
        bookingServiceImpl.cancelBooking(passengerId,bookingId);
    }
}
