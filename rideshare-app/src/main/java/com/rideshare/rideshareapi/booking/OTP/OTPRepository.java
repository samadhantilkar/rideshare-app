package com.rideshare.rideshareapi.booking.OTP;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP,Long> {
}
