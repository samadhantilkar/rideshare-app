package com.rideshare.rideshareapi.payment;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@Table(name = "paymentReceipt")
public class PaymentReceipt extends BaseEntity {
    private Double amount;

    @ManyToOne()
    private PaymentGateway paymentGateway;

    private String details;
}
