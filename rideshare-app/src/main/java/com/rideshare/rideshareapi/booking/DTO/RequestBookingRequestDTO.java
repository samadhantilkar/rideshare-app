package com.rideshare.rideshareapi.booking.DTO;

import com.rideshare.rideshareapi.Location.ExactLocation;
import com.rideshare.rideshareapi.booking.BookingType;
import com.rideshare.rideshareapi.comman.model.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@Builder
public class RequestBookingRequestDTO {
    private String name;
    private Gender gender;
    private BookingType bookingType;
    private Date scheduleTime;
    List<ExactLocation> route;
}
