package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.dto.driver.GetDriverLocationResponse;
import com.mxkoo.transport_management.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationDriverService {

    private final DriverRepository driverRepository;

    public List<GetDriverLocationResponse> getDriverLocations() {
        return driverRepository.findAll()
                               .stream()
                               .map(driver -> new GetDriverLocationResponse(
                                       driver.getId(),
                                       driver.getCoordinates().toDto(),
                                       driver.getName(),
                                       driver.getLastName(),
                                       driver.getContactNumber(),
                                       driver.getDriverStatus()
                               ))
                               .collect(Collectors.toList());
    }
}
