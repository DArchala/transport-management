package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.constant.TruckStatus;
import com.mxkoo.transport_management.entity.Road;
import com.mxkoo.transport_management.repository.RoadRepository;
import com.mxkoo.transport_management.entity.Truck;
import com.mxkoo.transport_management.repository.TruckRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TruckStatusService {

    private final TruckRepository truckRepository;
    private final RoadRepository roadRepository;

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?")
    public void checkTruckStatuses() {
        List<Truck> trucks = truckRepository.findAll();
        synchronized (trucks) {
            for (Truck truck : trucks) {
                setStatusForTruck(truck);
                truckRepository.save(truck);
            }
        }
    }

    public void setStatusForTruck(Truck truck) {
        Optional<Road> firstRoad = roadRepository.findFirstByTruckIdOrderByDepartureDateAsc(truck.getId());

        if (firstRoad.isPresent()) {
            Road road = firstRoad.get();
            LocalDate today = LocalDate.now();

            if (!today.isBefore(road.getDepartureDate()) && !today.isAfter(road.getArrivalDate())) {
                truck.setTruckStatus(TruckStatus.ON_THE_WAY);
            }
            else {
                truck.setTruckStatus(TruckStatus.WAITING_FOR_ROAD);
            }
        }
        else {
            truck.setTruckStatus(TruckStatus.WAITING_FOR_ROAD);
        }
    }

}
