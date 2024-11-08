package com.rideshare.rideshareapi.driver.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class startRideRequestDTO {
    private String code;
    private String sendToNumber;
}
