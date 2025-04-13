package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.component.ApplicationTime;
import com.mxkoo.transport_management.constant.TruckStatus;
import com.mxkoo.transport_management.repository.RoadRepository;
import com.mxkoo.transport_management.repository.TruckRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TruckStatusService {

    private final TruckRepository truckRepository;
    private final RoadRepository roadRepository;
    private final ApplicationTime applicationTime;

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?")
    public void checkTruckStatuses() {
        truckRepository.saveAll(truckRepository.findAll()
                                               .stream()
                                               .peek(truck -> truck.applyResolvedStatus(resolveTruckStatus(truck.getId())))
                                               .toList());
    }

    private TruckStatus resolveTruckStatus(Long truckId) {
        return roadRepository.findFirstByTruckIdOrderByDepartureDateAsc(truckId)
                             .map(road -> road.resolveTruckStatus(applicationTime.today()))
                             .orElse(TruckStatus.WAITING_FOR_ROAD);
    }

}
