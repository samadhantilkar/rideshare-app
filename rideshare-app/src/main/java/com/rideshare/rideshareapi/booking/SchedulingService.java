package com.rideshare.rideshareapi.booking;

import com.rideshare.rideshareapi.Location.LocationTrackingService;
import com.rideshare.rideshareapi.constant.Constants;
import com.rideshare.rideshareapi.messageQueue.MQMessage;
import com.rideshare.rideshareapi.messageQueue.MessageQueue;
import com.rideshare.rideshareapi.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final MessageQueue messageQueue;
    private final Constants constants;
    private final LocationTrackingService locationTrackingService;
    private final NotificationService notificationService;
    private final BookingRepository bookingRepository;
    private final BookingServiceImpl bookingServiceImpl;

    Set<Booking> scheduledBookings=new HashSet<>();


    @Scheduled(fixedRate = 1000)
    public void consumer(){
        MQMessage m= messageQueue.consumeMessage(constants.getSchedulingTopicName());

        if(null==m) return;

        Message message=(Message) m;

        schedule(message.getBooking());
    }

    @Scheduled(fixedRate = 60000)
    public void process(){
        Set<Booking> newScheduledBookings=new HashSet<>();

        for(Booking booking:scheduledBookings){
            if(DateUtils.addMinutes(new Date(),constants.getBookingProcessBeforeTime()).after(booking.getScheduleTime())){
                bookingServiceImpl.acceptBooking(booking.getDriver().getId(),booking.getId());
            }else {
                newScheduledBookings.add(booking);
            }
        }

        scheduledBookings=newScheduledBookings;
    }

    public void schedule(Booking booking){
        scheduledBookings.add(booking);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message implements MQMessage{
        private Booking booking;
    }
}
