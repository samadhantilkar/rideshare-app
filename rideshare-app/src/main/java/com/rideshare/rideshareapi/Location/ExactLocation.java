package com.rideshare.rideshareapi.Location;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "exactLocation")
public class ExactLocation extends BaseEntity {
    private Double latitude;
    private Double longitude;
}
