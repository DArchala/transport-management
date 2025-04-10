package com.mxkoo.transport_management.dto.truck;

import com.mxkoo.transport_management.constant.TruckStatus;
import com.mxkoo.transport_management.dto.coordinates.CoordinatesDto;

public record GetTruckLocationResponse(Long id,
                                       CoordinatesDto coordinates,
                                       String licensePlate,
                                       TruckStatus truckStatus) {
}
