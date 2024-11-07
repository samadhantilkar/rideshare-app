package com.rideshare.rideshareapi.booking;

import com.rideshare.rideshareapi.Location.ExactLocation;
import com.rideshare.rideshareapi.account.exception.InvalidPassengerException;
import com.rideshare.rideshareapi.booking.DTO.RequestBookingRequestDTO;
import com.rideshare.rideshareapi.booking.DTO.RequestBookingResponseDTO;
import com.rideshare.rideshareapi.booking.DTO.UpdateRouteRequestDTO;
import com.rideshare.rideshareapi.booking.DTO.UpdateRouteResponseDTO;
import com.rideshare.rideshareapi.booking.OTP.OTPService;
import com.rideshare.rideshareapi.booking.exception.InvalidActionForBookingStateException;
import com.rideshare.rideshareapi.booking.exception.InvalidBookingException;
import com.rideshare.rideshareapi.comman.model.Constants;
import com.rideshare.rideshareapi.messageQueue.MessageQueue;
import com.rideshare.rideshareapi.notification.ConsoleNotificationService;
import com.rideshare.rideshareapi.notification.NotificationService;
import com.rideshare.rideshareapi.passenger.Passenger;
import com.rideshare.rideshareapi.passenger.PassengerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final OTPService otpService;
    private final MessageQueue messageQueue;
    private final Constants constants;
    private final NotificationService notificationService;
    BookingService(BookingRepository bookingRepository, PassengerRepository passengerRepository,
                   OTPService otpService, MessageQueue messageQueue, Constants constants,
                   ConsoleNotificationService consoleNotificationService){
        this.bookingRepository=bookingRepository;
        this.passengerRepository=passengerRepository;
        this.otpService=otpService;
        this.messageQueue=messageQueue;
        this.constants=constants;
        this.notificationService=consoleNotificationService;
    }

    private Passenger getPassenger(Long passengerId){
        Optional<Passenger> optionalPassenger=passengerRepository.findById(passengerId);
        if(optionalPassenger.isEmpty()){
            throw new InvalidPassengerException("No passenger With Id"+passengerId);
        }
        return optionalPassenger.get();
    }

    public RequestBookingResponseDTO requestBooking(Long passengerId,RequestBookingRequestDTO requestDTO){

        Passenger passenger=getPassenger(passengerId);

        List<ExactLocation> route=new ArrayList<>();

        requestDTO.getRoute().forEach(location ->{
            route.add(ExactLocation.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
        });

        Booking booking=Booking.builder()
                .rideStartOPT(otpService.generate(passenger.getPhoneNumber()))
                .route(route)
                .passenger(passenger)
                .bookingType(requestDTO.getBookingType())
                .scheduleTime(requestDTO.getScheduleTime())
                .build();

        if(booking.getStartTime().after(new Date())){
            booking.setBookingStatus(BookingStatus.SCHEDULED);
            messageQueue.sendMessage(constants.getSchedulingTopicName(),new DriverMatchingService.Message(booking));
        }
        else{
            booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
            otpService.sendRideStartOTP(booking.getRideStartOPT());
            messageQueue.sendMessage(constants.getDriverMatchingTopicName(),new DriverMatchingService.Message(booking));
        }
        bookingRepository.save(booking);
        passengerRepository.save(booking.getPassenger());

        return new RequestBookingResponseDTO();
    }

    public List<Booking> getAllBookings(Long passengerId) {
        Optional<Passenger> optionalPassenger=passengerRepository.findById(passengerId);
        if(optionalPassenger.isEmpty()){
            throw new InvalidPassengerException("No Passenger with id " +passengerId);
        }

        return optionalPassenger.get().getBookings();
    }


    public Booking getBooking(Long passengerId, Long bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new InvalidBookingException("No Booking with id " + bookingId);
        }
        if (optionalBooking.get().getPassenger().getId().equals(passengerId)) {
            throw new InvalidBookingException("Passenger " + passengerId + " has no such booking " + bookingId);
        }
        return optionalBooking.get();
    }

    public UpdateRouteResponseDTO updateRoute(Long passengerId, Long bookingId, UpdateRouteRequestDTO requestDTO) {
        Passenger passenger=getPassenger(passengerId);
        Booking booking=getBooking(passengerId,bookingId);
        List<ExactLocation> route=new ArrayList<>(booking.getCompletedRoute());
        requestDTO.getRoutes().forEach(location ->{
            route.add(ExactLocation.builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build());
        });
        if(canChangeRoute(booking)){
            throw new InvalidActionForBookingStateException("Ride has already been completed or cancelled");
        }
        booking.setRoute(route);
        bookingRepository.save(booking);
        notificationService.notify(booking.getDriver().getPhoneNumber(),"Route Has Been Updated!");

        return UpdateRouteResponseDTO.builder()
                .routes(booking.getRoute())
                .build();
    }

    private boolean canChangeRoute(Booking booking){
        return booking.getBookingStatus().equals(BookingStatus.ASSIGNING_DRIVER)||
                booking.getBookingStatus().equals(BookingStatus.CAB_ARRIVED)||
                booking.getBookingStatus().equals(BookingStatus.IN_RIDE)||
                booking.getBookingStatus().equals(BookingStatus.SCHEDULED)||
                booking.getBookingStatus().equals(BookingStatus.REACHING_PICKUP_LOCATION);
    }

    public void retryBooking(Long passengerId, Long bookingId) {
        Passenger passenger=getPassenger(passengerId);
        Booking booking=getBooking(passengerId,bookingId);
        RequestBookingRequestDTO requestDTO= RequestBookingRequestDTO.builder()
                .bookingType(booking.getBookingType())
                .gender(passenger.getGender())
                .name(passenger.getName())
                .route(booking.getRoute())
                .scheduleTime(booking.getScheduleTime())
                .build();
        requestBooking(passengerId,requestDTO);
    }


    public void cancelBooking(Long passengerId, Long bookingId) {
        Passenger passenger=getPassenger(passengerId);
        Booking booking=getBooking(passengerId,bookingId);
    }
}
