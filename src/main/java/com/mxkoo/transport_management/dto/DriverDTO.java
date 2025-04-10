package com.mxkoo.transport_management.dto;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.entity.Leave;
import lombok.Builder;

import java.util.List;

@Builder
public record DriverDTO(
        Long id,
        String name,
        String lastName,
        Coordinates coordinates,
        String email,
        Long contactNumber,
        List<RoadDTO> roads,
        DriverStatus driverStatus,
        int daysOffLeft,
        List<Leave> leaves
) {}
