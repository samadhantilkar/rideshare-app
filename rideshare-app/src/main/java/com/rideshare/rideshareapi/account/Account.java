package com.rideshare.rideshareapi.account;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import com.rideshare.rideshareapi.role.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class Account extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @Singular
    private List<Role> roles=new ArrayList<>();
}
