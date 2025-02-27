package com.rideshare.rideshareapi.passenger;

import com.rideshare.rideshareapi.Location.ExactLocation;
import com.rideshare.rideshareapi.account.exception.InvalidPassengerException;
import com.rideshare.rideshareapi.booking.Booking;
import com.rideshare.rideshareapi.booking.BookingRepository;
import com.rideshare.rideshareapi.booking.BookingService;
import com.rideshare.rideshareapi.booking.BookingServiceImpl;
import com.rideshare.rideshareapi.booking.OTP.OTP;
import com.rideshare.rideshareapi.booking.exception.InvalidBookingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService{
    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @Override
    public Passenger getPassengerDetails(Long passengerId){
       return getPassengerFromId(passengerId);
    }

    @Override
    public List<Booking> getAllBooking(Long passengerId) {
        List<Booking> bookings=passengerRepository.findByBookings(passengerId);
        return bookings;
    }

    @Override
    public Booking getBooking(Long passengerId, Long bookingId) {
        Passenger passenger=getPassengerFromId(passengerId);
        Booking booking=getPassengerBookingFromId(passenger,bookingId);
        return booking;
    }

    @Override
    public void requestBooking(Long passengerId, Booking data) {
        Passenger passenger=getPassengerFromId(passengerId);
        List<ExactLocation> route=new ArrayList<>();

        data.getRoute().forEach( location ->{
            route.add(ExactLocation.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
        });

        Booking booking=Booking.builder()
                .rideStartOPT(OTP.builder()
                        .code("0000")
                        .sendToNumber(passenger.getPhoneNumber())
                        .build())
                .route(route)
                .passenger(passenger)
                .bookingType(data.getBookingType())
                .scheduleTime(data.getScheduleTime())
                .build();
        bookingService.createBooking(booking);
    }



    private Booking getPassengerBookingFromId(Passenger passenger,Long bookingId){
        Booking booking=bookingRepository.findById(bookingId).orElseThrow( ()-> new InvalidBookingException("No Booking Found with Id:"+bookingId));
        if(!booking.getPassenger().equals(passenger)){
            throw new InvalidBookingException("Passenger "+passenger.getBookings()+" has no such booking "+bookingId);
        }
        return booking;
    }

    private Passenger getPassengerFromId(Long passengerId){
        return passengerRepository.findById(passengerId).orElseThrow( ()-> new InvalidPassengerException("No Passenger Found with Id:"+passengerId));
    }


}
