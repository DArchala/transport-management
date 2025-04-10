package com.mxkoo.transport_management.dto;

import com.mxkoo.transport_management.constant.DriverStatus;

public record LocationDriverDTO(
        Long id,
        Coordinates coordinates,
        String name,
        String lastName,
        Long contactNumber,
        DriverStatus driverStatus
) {
}
