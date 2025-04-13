package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.component.ApplicationTime;
import com.mxkoo.transport_management.constant.RoadStatus;
import com.mxkoo.transport_management.entity.Road;
import com.mxkoo.transport_management.repository.RoadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadStatusService {

    private final RoadRepository roadRepository;
    private final ApplicationTime applicationTime;

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?")
    public void checkRoadStatuses() {
        List<Road> roads = roadRepository.findAll();
        for (Road road : roads) {
            setStatusForRoad(road);
        }
    }

    public void setStatusForRoad(Road road) {
        var today = applicationTime.today();

        if (!today.isBefore(road.getDepartureDate()) && !today.isAfter(road.getArrivalDate())) {
            road.setRoadStatus(RoadStatus.IN_PROGRESS);
        }
        else if (today.isBefore(road.getDepartureDate())) {
            road.setRoadStatus(RoadStatus.IN_FUTURE);
        }
        else if (today.isAfter(road.getArrivalDate())) {
            road.setRoadStatus(RoadStatus.DONE);
        }
        roadRepository.save(road);
    }

}
