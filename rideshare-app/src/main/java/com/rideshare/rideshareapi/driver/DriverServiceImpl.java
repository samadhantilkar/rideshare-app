package com.rideshare.rideshareapi.driver;

import com.rideshare.rideshareapi.booking.Booking;
import com.rideshare.rideshareapi.booking.BookingRepository;
import com.rideshare.rideshareapi.booking.BookingServiceImpl;
import com.rideshare.rideshareapi.booking.exception.InvalidBookingException;
import com.rideshare.rideshareapi.constant.Constants;
import com.rideshare.rideshareapi.driver.DTO.BookingDetailResponseDTO;
import com.rideshare.rideshareapi.driver.DTO.ChangeAvailabilityRequestDTO;
import com.rideshare.rideshareapi.driver.DTO.ChangeAvailabilityResponseDTO;
import com.rideshare.rideshareapi.driver.DTO.DriverDetailResponseDTO;
import com.rideshare.rideshareapi.driver.Exception.InvalidDriverException;
import com.rideshare.rideshareapi.driver.Exception.UnapprovedDriverException;
import com.rideshare.rideshareapi.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService{

    private final DriverRepository driverRepository;
    private final BookingRepository bookingRepository;
    private final BookingServiceImpl bookingServiceImpl;
    private final ModelMapper modelMapper;
    private final Constants constants;
    private final NotificationService notificationService;


    @Override
    public DriverDetailResponseDTO getDriverDetail(Long driverId) {
        Driver driver= getDriver(driverId);
        return DriverDetailResponseDTO.builder()
                .account(driver.getAccount())
                .acceptableBooking(driver.getAcceptableBooking())
                .licenseDetails(driver.getLicenseDetails())
                .activeBooking(driver.getActiveBooking())
                .activeCity(driver.getActiveCity())
                .approvalStatus(driver.getApprovalStatus())
                .home(driver.getHome())
                .gender(driver.getGender())
                .name(driver.getName())
                .avgRating(driver.getAvgRating())
                .isAvailable(driver.getIsAvailable())
                .bookings(driver.getBookings())
                .phoneNumber(driver.getPhoneNumber())
                .lastKnowLocation(driver.getLastKnowLocation())
                .car(driver.getCar())
                .dob(driver.getDob())
                .build();
    }

    @Override
    public BookingDetailResponseDTO getDriverBookingFromId(Long driverId, Long bookingId) {
        Booking booking= bookingServiceImpl.findByBookingId(bookingId);
        if(!booking.getDriver().getId().equals(driverId)){
           throw new  InvalidBookingException("Driver With Id:"+driverId +" No Booking Found for Id:"+bookingId);
        }
        return modelMapper.map(booking, BookingDetailResponseDTO.class);
    }

    @Override
    public List<BookingDetailResponseDTO> getAllBookings(Long driverId) {
        Driver driver= getDriver(driverId);
        List<BookingDetailResponseDTO > bookings=new ArrayList<>();
        for(Booking booking:driver.getBookings()){
            bookings.add(modelMapper.map(booking, BookingDetailResponseDTO.class));
        }
        return bookings;
    }

    @Override
    public ChangeAvailabilityResponseDTO changeAvailability(Long driverId, ChangeAvailabilityRequestDTO requestDTO) {
        Driver driver= getDriver(driverId);
        if(driver.getIsAvailable() && !driver.getApprovalStatus().equals(DriverApprovalStatus.APPROVED)){
            throw new UnapprovedDriverException("Driver Approval Pending or Denied"+driver.getId());
        }
        driver.setIsAvailable(requestDTO.isAvailable());
        driverRepository.save(driver);
        return ChangeAvailabilityResponseDTO.builder()
                .isAvailable(driver.getIsAvailable())
                .name(driver.getName())
                .build();
    }

    private Driver getDriver(Long driverId){
        Optional<Driver> optionalDriver=driverRepository.findById(driverId);
        if (optionalDriver.isEmpty()){
            throw new InvalidDriverException("No driver found with id:"+driverId);
        }
        return optionalDriver.get();
    }

}
