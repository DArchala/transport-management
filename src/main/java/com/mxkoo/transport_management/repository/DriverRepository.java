package com.mxkoo.transport_management.repository;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findDriverByDriverStatus(DriverStatus driverStatus);
}
