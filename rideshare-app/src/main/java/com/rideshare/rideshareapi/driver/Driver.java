package com.rideshare.rideshareapi.driver;

import com.rideshare.rideshareapi.Location.ExactLocation;
import com.rideshare.rideshareapi.booking.Booking;
import com.rideshare.rideshareapi.booking.Review.Review;
import com.rideshare.rideshareapi.car.Car;
import com.rideshare.rideshareapi.comman.model.BaseEntity;
import com.rideshare.rideshareapi.comman.model.Gender;
import com.rideshare.rideshareapi.account.Account;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name="driver",indexes =
    @Index(columnList = "account_id", unique = true))

public class Driver extends BaseEntity {
    @OneToOne
    private Account account;

    private String name;

    private String phoneNumber;

    private Gender gender;

    @OneToOne
    private Review avgRating;

    @OneToOne(mappedBy = "driver")
    private Car car;

    private String licenseDetails;

    @Temporal(value = TemporalType.DATE)
    private Date dob;

    @Enumerated(value = EnumType.STRING)
    private DriverApprovalStatus approvalStatus;

    @OneToMany(mappedBy = "driver")
    private List<Booking> bookings;

    @ManyToMany(mappedBy = "notifiedDrivers",cascade = CascadeType.PERSIST)
    private Set<Booking> acceptableBooking=new HashSet<>();

    private Boolean activeBooking=null;

    private Boolean isAvailable;

    private String activeCity;

    @OneToOne
    private ExactLocation lastKnowLocation;

    @OneToOne
    private ExactLocation home;
}
