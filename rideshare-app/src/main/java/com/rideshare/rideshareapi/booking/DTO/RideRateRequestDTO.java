package com.rideshare.rideshareapi.booking.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RideRateRequestDTO {
    private Integer ratingOutOfFive;
    private String note;
}
