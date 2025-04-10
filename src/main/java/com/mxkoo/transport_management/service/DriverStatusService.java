package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.entity.Driver;
import com.mxkoo.transport_management.repository.DriverRepository;
import com.mxkoo.transport_management.entity.Leave;
import com.mxkoo.transport_management.repository.LeaveRepository;
import com.mxkoo.transport_management.entity.Road;
import com.mxkoo.transport_management.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverStatusService {

    private final DriverRepository driverRepository;
    private final RoadRepository roadRepository;
    private final LeaveRepository leaveRepository;

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?")
    public void checkDriverStatuses() {
        List<Driver> drivers = driverRepository.findAll();
        for (Driver driver : drivers) {
            setStatusForDriver(driver);
            driverRepository.save(driver);
        }
    }

    public void setStatusForDriver(Driver driver) {
        Optional<Road> firstRoad = roadRepository.findFirstByDriverIdOrderByDepartureDateAsc(driver.getId());
        Optional<Leave> firstLeave = leaveRepository.findFirstByDriverIdOrderByEndAsc(driver.getId());

        LocalDate today = LocalDate.now();
        boolean statusUpdated = false;

        if (firstRoad.isPresent() && !statusUpdated) {
            Road road = firstRoad.get();
            if (!today.isBefore(road.getDepartureDate()) && !today.isAfter(road.getArrivalDate())) {
                driver.setDriverStatus(DriverStatus.ON_THE_WAY);
                statusUpdated = true;
            }
        }

        if (firstLeave.isPresent() && !statusUpdated) {
            Leave leave = firstLeave.get();
            if (today.equals(leave.getEnd())) {
                driver.setDriverStatus(DriverStatus.WAITING_FOR_ROAD);
                statusUpdated = true;
            }
        }

        if (!statusUpdated) {
            driver.setDriverStatus(DriverStatus.WAITING_FOR_ROAD);
        }
    }


}
