package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.component.ApplicationTime;
import com.mxkoo.transport_management.repository.RoadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoadStatusService {

    private final RoadRepository roadRepository;
    private final ApplicationTime applicationTime;

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?")
    public void checkRoadStatuses() {
        roadRepository.saveAll(roadRepository.findAll()
                                             .stream()
                                             .peek(road -> road.applyResolvedStatus(road.resolveRoadStatus(applicationTime.today())))
                                             .toList());
    }

}
