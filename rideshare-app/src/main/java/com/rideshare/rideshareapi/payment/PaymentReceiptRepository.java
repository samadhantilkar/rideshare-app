package com.rideshare.rideshareapi.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt,Long> {
}
