package com.rideshare.rideshareapi.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExactLocationRepository extends JpaRepository<ExactLocation,Long> {
}
