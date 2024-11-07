package com.rideshare.rideshareapi.booking;

import com.rideshare.rideshareapi.Location.ExactLocation;
import com.rideshare.rideshareapi.booking.OTP.OTP;
import com.rideshare.rideshareapi.booking.Review.Review;
import com.rideshare.rideshareapi.comman.model.BaseEntity;
import com.rideshare.rideshareapi.driver.Driver;
import com.rideshare.rideshareapi.passenger.Passenger;
import com.rideshare.rideshareapi.payment.PaymentReceipt;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name ="booking",indexes ={
    @Index(columnList = "driver_id"),
    @Index(columnList = "passenger_id"),
})
public class Booking extends BaseEntity {
    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Driver driver;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Driver> notifiedDrivers=new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    private BookingType bookingType;

    @OneToOne
    private Review reviewByPassenger;

    @Enumerated(value = EnumType.STRING)
    private BookingStatus bookingStatus;

    @OneToOne
    private Review reviewByDriver;

    @OneToOne
    private PaymentReceipt paymentReceipt;

    @OneToMany
    @JoinTable(
            name = "booking_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "exact_location_id"),
            indexes = {@Index(columnList = "booking_id")}
    )
    private List<ExactLocation> route=new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_completed_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "exact_location_id"),
            indexes = {@Index(columnList = "booking_id")}
    )
    @OrderColumn(name = "location_index")
    private List<ExactLocation> completedRoute =new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleTime;

    @Temporal(value=TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedCompletionTime;

    private Long totalDistanceMeters;

    @OneToOne
    private OTP rideStartOPT;
}
