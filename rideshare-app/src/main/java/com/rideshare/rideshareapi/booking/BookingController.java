package com.rideshare.rideshareapi.booking;

import com.rideshare.rideshareapi.booking.DTO.RequestBookingRequestDTO;
import com.rideshare.rideshareapi.booking.DTO.RequestBookingResponseDTO;
import com.rideshare.rideshareapi.booking.DTO.UpdateRouteRequestDTO;
import com.rideshare.rideshareapi.booking.DTO.UpdateRouteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

//    BookingController(BookingServiceImpl bookingServiceImpl){
//        this.bookingServiceImpl = bookingServiceImpl;
//    }

    @PostMapping("{passengerId}/bookings/")
    public RequestBookingResponseDTO requestBooking(@PathVariable(name = "passengerId") Long passengerId,
                                                    @RequestBody RequestBookingRequestDTO requestDTO){
        return bookingService.requestBooking(passengerId,requestDTO);

    }

    @PatchMapping("{passengerId}/bookings/{bookingId}")
    public UpdateRouteResponseDTO updateRoute(@PathVariable(name = "passengerId") Long passengerId,
                                              @PathVariable(name = "bookingId")Long BookingId,
                                              @RequestBody UpdateRouteRequestDTO requestDTO){
        return bookingService.updateRoute(passengerId,BookingId, requestDTO);
    }

    @GetMapping("{passengerId}/booking/{bookingId}")
    public Booking getBooking(@PathVariable(name = "passengerId")Long passengerId,
                              @PathVariable(name = "bookingId")Long bookingId){
        return bookingService.getBooking(passengerId,bookingId);
    }

    @GetMapping("{passengerID}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name = "passengerId")Long passengerId){
        return bookingService.getAllBookings(passengerId);
    }

    @PostMapping("{passengerId}/bookings/{bookingId}")
    public void retryBooking(@PathVariable(name = "passengerId")Long passengerId,
                             @PathVariable(name = "bookingId")Long bookingId){
        bookingService.retryBooking(passengerId,bookingId);
    }

    @DeleteMapping("{passengerId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "passengerId") Long passengerId,
                              @PathVariable(name = "BookingId") Long bookingId){
        bookingService.cancelBooking(passengerId,bookingId);
    }
}
