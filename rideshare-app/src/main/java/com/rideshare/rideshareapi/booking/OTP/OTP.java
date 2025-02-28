package com.rideshare.rideshareapi.booking.OTP;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "opt")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class OTP extends BaseEntity {
    private String code;
    private String sendToNumber;
}
