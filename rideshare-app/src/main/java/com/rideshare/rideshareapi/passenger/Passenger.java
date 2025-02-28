package com.rideshare.rideshareapi.passenger;

import com.rideshare.rideshareapi.Location.ExactLocation;
import com.rideshare.rideshareapi.booking.Booking;
import com.rideshare.rideshareapi.account.Account;
import com.rideshare.rideshareapi.booking.Review.Review;
import com.rideshare.rideshareapi.comman.model.BaseEntity;
import com.rideshare.rideshareapi.comman.model.Gender;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="passenger")
@Getter @Setter
public class Passenger extends BaseEntity {

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings=new ArrayList<>();

    @OneToOne
    private Booking activeBooking=null;

    @Temporal(value = TemporalType.DATE)
    private Date dob;

    private String phoneNumber;

    @OneToOne
    private ExactLocation home;
    @OneToOne
    private ExactLocation work;
    @OneToOne
    private ExactLocation lastKnownLocation;

    @OneToOne
    private Review avgRating;
}
