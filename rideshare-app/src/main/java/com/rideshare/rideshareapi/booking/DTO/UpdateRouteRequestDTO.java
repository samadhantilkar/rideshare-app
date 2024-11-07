package com.rideshare.rideshareapi.booking.DTO;

import com.rideshare.rideshareapi.Location.ExactLocation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class UpdateRouteRequestDTO {
    List<ExactLocation> routes;
}
