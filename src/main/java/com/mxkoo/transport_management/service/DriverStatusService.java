package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.component.ApplicationTime;
import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.repository.DriverRepository;
import com.mxkoo.transport_management.repository.LeaveRepository;
import com.mxkoo.transport_management.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverStatusService {

    private final DriverRepository driverRepository;
    private final RoadRepository roadRepository;
    private final LeaveRepository leaveRepository;
    private final ApplicationTime applicationTime;

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?")
    public void checkDriverStatuses() {
        var drivers = driverRepository.findAll()
                                      .stream()
                                      .collect(Collectors.toMap(driver -> driver, driver -> resolveDriverStatus(driver.getId())))
                                      .entrySet()
                                      .stream()
                                      .filter(entry -> entry.getValue()
                                                            .isPresent())
                                      .peek(entry -> entry.getKey()
                                                          .applyResolvedStatus(entry.getValue()
                                                                                    .get()))
                                      .map(Map.Entry::getKey)
                                      .toList();
        driverRepository.saveAll(drivers);
    }

    public Optional<DriverStatus> resolveDriverStatus(Long driverId) {
        var driverStatusFromRoad = roadRepository.findFirstByDriverIdOrderByDepartureDateAsc(driverId)
                                                 .map(road -> road.isDriverOnTheWay(applicationTime.today()) ? DriverStatus.ON_THE_WAY : null);

        if (driverStatusFromRoad.isPresent()) {
            return driverStatusFromRoad;
        }

        return leaveRepository.findFirstByDriverIdOrderByEndAsc(driverId)
                              .map(leave -> leave.endsWith(applicationTime.today()) ? DriverStatus.WAITING_FOR_ROAD : null);
    }

}
