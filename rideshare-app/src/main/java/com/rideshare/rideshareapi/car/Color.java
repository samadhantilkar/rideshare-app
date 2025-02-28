package com.rideshare.rideshareapi.car;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@Table(name = "Color")
public class Color extends BaseEntity {
    @Column(unique = true,nullable = false)
    private String name;
}
