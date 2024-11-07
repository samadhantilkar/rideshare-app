package com.rideshare.rideshareapi.payment;

import com.rideshare.rideshareapi.comman.model.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
