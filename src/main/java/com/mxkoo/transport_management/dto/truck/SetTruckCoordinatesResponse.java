package com.mxkoo.transport_management.dto.truck;

import com.mxkoo.transport_management.constant.TruckStatus;
import com.mxkoo.transport_management.dto.coordinates.CoordinatesDto;

import java.time.LocalDate;

public record SetTruckCoordinatesResponse(Long id,
                                          String licensePlate,
                                          Integer capacity,
                                          CoordinatesDto coordinates,
                                          LocalDate inspectionDate,
                                          TruckStatus truckStatus) {
}
