package com.rideshare.rideshareapi.driver;

import com.rideshare.rideshareapi.driver.DTO.BookingDetailResponseDTO;
import com.rideshare.rideshareapi.driver.DTO.ChangeAvailabilityRequestDTO;
import com.rideshare.rideshareapi.driver.DTO.ChangeAvailabilityResponseDTO;
import com.rideshare.rideshareapi.driver.DTO.DriverDetailResponseDTO;

import java.util.List;

public interface DriverService {
    DriverDetailResponseDTO getDriverDetail(Long driverId);

    BookingDetailResponseDTO getDriverBookingFromId(Long driverId, Long bookingId);

    List<BookingDetailResponseDTO> getAllBookings(Long driverId);

    ChangeAvailabilityResponseDTO changeAvailability(Long driverId, ChangeAvailabilityRequestDTO requestDTO);


}
