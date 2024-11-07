package com.rideshare.rideshareapi.role;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "role")
public class Role extends BaseEntity {
    @Column(unique = true,nullable = false)
    private String name;

    private String description;
}
//Role Base Authentication
//Permision based Authentication