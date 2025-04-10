package com.mxkoo.transport_management.dto;

import com.mxkoo.transport_management.constant.TruckStatus;

public record LocationTruckDTO(
        Long id,
        Coordinates coordinates,
        String licensePlate,
        TruckStatus truckStatus
) {
}
