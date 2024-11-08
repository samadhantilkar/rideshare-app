package com.rideshare.rideshareapi.constant;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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