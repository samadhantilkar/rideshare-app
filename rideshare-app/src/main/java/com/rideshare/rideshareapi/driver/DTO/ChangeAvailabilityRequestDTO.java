package com.rideshare.rideshareapi.driver.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class ChangeAvailabilityRequestDTO {
    private boolean isAvailable;
}
