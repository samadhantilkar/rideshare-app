package com.rideshare.rideshareapi.constant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBConstantRepository extends JpaRepository<DBConstant, Long> {
}
