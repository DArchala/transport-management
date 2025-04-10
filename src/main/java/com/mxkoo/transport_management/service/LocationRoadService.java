package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.dto.road.GetRoadLocationResponse;
import com.mxkoo.transport_management.repository.RoadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationRoadService {

    private final RoadRepository roadRepository;

    public List<GetRoadLocationResponse> getRoadLocation() {
        return roadRepository.findAll()
                             .stream()
                             .map(road -> new GetRoadLocationResponse(
                                     road.getId(),
                                     road.getFrom(),
                                     road.getVia(),
                                     road.getTo(),
                                     road.getDepartureDate(),
                                     road.getArrivalDate(),
                                     road.getDistance(),
                                     road.getPrice(),
                                     road.getDriver()
                                         .getId(),
                                     road.getTruck()
                                         .getId(),
                                     road.getRoadStatus()
                             ))
                             .collect(Collectors.toList());
    }

}
