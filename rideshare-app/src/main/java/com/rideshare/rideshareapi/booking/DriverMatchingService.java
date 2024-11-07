package com.rideshare.rideshareapi.booking;

import com.rideshare.rideshareapi.messageQueue.MQMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class DriverMatchingService {
    @AllArgsConstructor
    @Getter @Setter
    public static class Message implements MQMessage{
        private Booking booking;

        @Override
        public String toString(){
            return String.format("Need to Find driver for %s",booking.toString());
        }

    }
}
