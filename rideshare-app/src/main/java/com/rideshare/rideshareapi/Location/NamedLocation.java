package com.rideshare.rideshareapi.Location;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "namedLocation")
@Getter @Setter
public class NamedLocation extends BaseEntity {
    @OneToOne
    private ExactLocation exactLocation;

    private String name;
    private String zipcode;
    private String city;
    private String country;
    private String state;
}
