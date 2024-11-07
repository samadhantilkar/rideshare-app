package com.rideshare.rideshareapi.car;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import com.rideshare.rideshareapi.driver.Driver;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "car")
public class Car extends BaseEntity {
    @ManyToOne
    private Color color;

    private String plateNumber;

    private String brandAndModel;

    @Enumerated(value = EnumType.STRING)
    private CarType carType;

    @OneToOne
    private Driver driver;
}
