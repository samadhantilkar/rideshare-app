package com.rideshare.rideshareapi.booking;

import com.rideshare.rideshareapi.Location.ExactLocation;
import com.rideshare.rideshareapi.account.exception.InvalidPassengerException;
import com.rideshare.rideshareapi.booking.DTO.*;
import com.rideshare.rideshareapi.booking.OTP.OTPService;
import com.rideshare.rideshareapi.booking.Review.Review;
import com.rideshare.rideshareapi.booking.Review.ReviewRepository;
import com.rideshare.rideshareapi.booking.exception.InvalidActionForBookingStateException;
import com.rideshare.rideshareapi.booking.exception.InvalidBookingException;
import com.rideshare.rideshareapi.booking.exception.InvalidOTPException;
import com.rideshare.rideshareapi.constant.Constants;
import com.rideshare.rideshareapi.driver.DTO.startRideRequestDTO;
import com.rideshare.rideshareapi.driver.Driver;
import com.rideshare.rideshareapi.driver.DriverRepository;
import com.rideshare.rideshareapi.driver.Exception.InvalidDriverException;
import com.rideshare.rideshareapi.messageQueue.MessageQueue;
import com.rideshare.rideshareapi.notification.NotificationService;
import com.rideshare.rideshareapi.passenger.Passenger;
import com.rideshare.rideshareapi.passenger.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final OTPService otpService;
    private final MessageQueue messageQueue;
    private final Constants constants;
    private final NotificationService notificationService;
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;

    private Passenger getPassenger(Long passengerId){
        Optional<Passenger> optionalPassenger=passengerRepository.findById(passengerId);
        if(optionalPassenger.isEmpty()){
            throw new InvalidPassengerException("No passenger With Id:"+passengerId);
        }
        return optionalPassenger.get();
    }

    @Override
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

    @Override
    public List<Booking> getAllBookings(Long passengerId) {
        Optional<Passenger> optionalPassenger=passengerRepository.findById(passengerId);
        if(optionalPassenger.isEmpty()){
            throw new InvalidPassengerException("No Passenger with id " +passengerId);
        }

        return optionalPassenger.get().getBookings();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void cancelBooking(Long passengerId, Long bookingId) {
        Passenger passenger=getPassenger(passengerId);
        Booking booking=getBooking(passengerId,bookingId);
        cancelByPassenger(passenger,booking);
        bookingRepository.save(booking);

    }

    private void cancelByPassenger(Passenger passenger,Booking booking){
        try {
                if(booking.getBookingStatus().equals(BookingStatus.ASSIGNING_DRIVER)|| booking.getBookingStatus().equals(BookingStatus.REACHING_PICKUP_LOCATION)||
                    booking.getBookingStatus().equals(BookingStatus.SCHEDULED)|| booking.getBookingStatus().equals(BookingStatus.CAB_ARRIVED)){
                        throw new InvalidActionForBookingStateException("Can not cancel the booking now.");
                }
                booking.setBookingStatus(BookingStatus.CANCELLING);
                booking.setDriver(null);
                booking.getNotifiedDrivers().clear();
        }
        catch (Exception e){
            notificationService.notify(passenger.getPhoneNumber()
            ,"Can not cancel the booking now. If the ride is in progress, ask your driver to end the ride");
            throw e;
        }
    }

    @Override
    public Booking findByBookingId(Long bookingId) {
        Optional<Booking> optionalBooking=bookingRepository.findById(bookingId);
        if(optionalBooking.isEmpty()){
            throw new InvalidBookingException("No Booking With Id:"+bookingId);
        }
        return optionalBooking.get();
    }

    @Override
    public void acceptBooking(Long driverId, Long bookingId) {
        Driver driver=getDriver(driverId);
        Booking booking=findByBookingId(bookingId);
        acceptBooking(driver,booking);
    }

    private Driver getDriver(Long driverId){
        Optional<Driver> optionalDriver=driverRepository.findById(driverId);
        if (optionalDriver.isEmpty()){
            throw new InvalidDriverException("No driver found with id:"+driverId);
        }
        return optionalDriver.get();
    }

    @Override
    public void createBooking(Booking booking){
        if(booking.getStartTime().after(new Date())){
            booking.setBookingStatus(BookingStatus.SCHEDULED);
            messageQueue.sendMessage(constants.getSchedulingTopicName(),new SchedulingService.Message(booking));
        }else{
            booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
            otpService.sendRideStartOTP(booking.getRideStartOPT());
            messageQueue.sendMessage(constants.getLocationTrackingTopicName(),new DriverMatchingService.Message(booking));
        }
        bookingRepository.save(booking);
        passengerRepository.save(booking.getPassenger());
    }

    private void acceptBooking(Driver driver, Booking booking){
        if(!booking.getBookingStatus().equals(BookingStatus.ASSIGNING_DRIVER)){
            return;
        }
        if(!canAccept(constants.getMaxWaitTimeForPreviousRide(),driver)){
            notificationService.notify(driver.getPhoneNumber(),"can not accept booking");
            return;
        }
        booking.setDriver(driver);
        driver.setActiveBooking(booking);
        booking.getNotifiedDrivers().clear();
        driver.getAcceptableBooking().clear();
        notificationService.notify(booking.getPassenger().getPhoneNumber(),driver.getName()+" ia Arriving at pickup location");
        notificationService.notify(driver.getPhoneNumber(),"Booking accepted");
        bookingRepository.save(booking);
        driverRepository.save(driver);
    }

    private boolean canAccept(int maxWaitTimeForPreviousRide,Driver driver){
        if(driver.getIsAvailable() && driver.getActiveBooking()==null){
            return true;
        }
        return driver.getActiveBooking().getExpectedCompletionTime().before(
                DateUtils.addMinutes(new Date(), maxWaitTimeForPreviousRide));
    }

    @Override
    public void cancelByDriver(Long driverId, Long bookingId) {
        Driver driver=getDriver(driverId);
        Booking booking=findByBookingId(bookingId);
        booking.setDriver(null);
        driver.setActiveBooking(null);
        driver.getAcceptableBooking().remove(booking);
        notificationService.notify(booking.getPassenger().getPhoneNumber(),"Reassigning driver");
        notificationService.notify(driver.getPhoneNumber(),"Booking has been cancel");
        retryBooking(booking.getPassenger().getId(),bookingId);
    }

    @Override
    public void startRide(Long driverId, Long bookingId, startRideRequestDTO requestDTO) {
        Driver driver=getDriver(driverId);
        Booking booking=findByBookingId(bookingId);
        if(booking.getBookingStatus().equals(BookingStatus.CAB_ARRIVED)){
            throw new InvalidActionForBookingStateException("Can not start the ride before the ride driver has reached the pickup point");
        }
        Instant otpExpiryTime=booking.getCreatedAt().toInstant().plus(Duration.ofMinutes(constants.getRideStartOTPExpiryMinutes()));
        if(!booking.getRideStartOPT().getCode().equals(requestDTO.getCode()) || otpExpiryTime.isBefore(Instant.now())){
            throw new InvalidOTPException();
        }
        booking.setStartTime(new Date());
        booking.getPassenger().setActiveBooking(booking);
        booking.setBookingStatus(BookingStatus.IN_RIDE);
        bookingRepository.save(booking);
    }

    @Override
    public void endRide(Long driverId, Long bookingId) {
        Driver driver=getDriver(driverId);
        Booking booking=findByBookingId(bookingId);
        if(booking.getBookingStatus().equals(BookingStatus.IN_RIDE)){
            throw new InvalidActionForBookingStateException("The ride has not started yet");
        }
        driver.setActiveBooking(null);
        booking.setEndTime(new Date());
        booking.getPassenger().setActiveBooking(null);
        booking.setBookingStatus(BookingStatus.COMPLETED);
        driverRepository.save(driver);
        bookingRepository.save(booking);
    }

    @Override
    public void rateRide(Long driverId, Long bookingId, RideRateRequestDTO requestDTO) {
        Driver driver=getDriver(driverId);
        Booking booking=findByBookingId(bookingId);
        Review review=modelMapper.map(requestDTO, Review.class);
        booking.setReviewByDriver(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }
}
