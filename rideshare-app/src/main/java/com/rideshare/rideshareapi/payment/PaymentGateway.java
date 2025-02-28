package com.rideshare.rideshareapi.payment;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "paymentGateway")
public class PaymentGateway extends BaseEntity
{
    private String name;

    @OneToMany(mappedBy = "paymentGateway")
    private Set<PaymentReceipt> receipts=new HashSet<>();
}
