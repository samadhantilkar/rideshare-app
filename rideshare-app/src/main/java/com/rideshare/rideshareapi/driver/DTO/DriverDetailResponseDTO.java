package com.rideshare.rideshareapi.driver.DTO;

import com.rideshare.rideshareapi.Location.ExactLocation;
import com.rideshare.rideshareapi.account.Account;
import com.rideshare.rideshareapi.booking.Booking;
import com.rideshare.rideshareapi.booking.Review.Review;
import com.rideshare.rideshareapi.car.Car;
import com.rideshare.rideshareapi.comman.model.Gender;
import com.rideshare.rideshareapi.driver.DriverApprovalStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter @Setter
@Builder
public class DriverDetailResponseDTO {
    private Account account;

    private String name;

    private String phoneNumber;

    private Gender gender;

    private Review avgRating;

    private Car car;

    private String licenseDetails;

    private Date dob;

    private DriverApprovalStatus approvalStatus;

    private List<Booking> bookings;

    private Set<Booking> acceptableBooking=new HashSet<>();

    private Booking activeBooking=null;

    private Boolean isAvailable;

    private String activeCity;

    private ExactLocation lastKnowLocation;

    private ExactLocation home;
}
