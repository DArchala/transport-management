package com.mxkoo.transport_management.dto.truck;

import com.mxkoo.transport_management.constant.TruckStatus;
import com.mxkoo.transport_management.dto.coordinates.CoordinatesDto;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetTruckResponse(Long id,
                               String licensePlate,
                               Integer capacity,
                               CoordinatesDto coordinates,
                               LocalDate inspectionDate,
                               TruckStatus truckStatus) {
}
