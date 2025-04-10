package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.dto.LocationTruckDTO;
import com.mxkoo.transport_management.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationTruckService {

    private final TruckRepository truckRepository;

    public List<LocationTruckDTO> getTruckLocations() {
        return truckRepository.findAll()
                              .stream()
                              .map(truck -> new LocationTruckDTO(
                                      truck.getId(),
                                      truck.getCoordinates(),
                                      truck.getLicensePlate(),
                                      truck.getTruckStatus()
                              ))
                              .collect(Collectors.toList());
    }
}
