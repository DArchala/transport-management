package com.mxkoo.transport_management.dto;

import com.mxkoo.transport_management.constant.RoadStatus;
import lombok.Builder;

import java.time.LocalDate;
@Builder
public record RoadDTO(
        Long id,
        String from,
        String[] via,
        String to,
        LocalDate departureDate,
        LocalDate arrivalDate,
        Double distance,
        Double price,
        TruckDTO truckDTO,
        DriverDTO driverDTO,
        RoadStatus roadStatus
) {
}
