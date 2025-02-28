package com.rideshare.rideshareapi.constant;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dbconstant")
public class DBConstant extends BaseEntity {
    @Column(nullable = false,unique = true)
    private String name;
    private String value;
}