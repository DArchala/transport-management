package com.mxkoo.transport_management.mapper;

import com.mxkoo.transport_management.dto.truck.GetTruckResponse;
import com.mxkoo.transport_management.dto.truck.SetTruckCoordinatesResponse;
import com.mxkoo.transport_management.dto.truck.UpdateTruckResponse;
import com.mxkoo.transport_management.entity.Truck;

public class TruckMapper {

    public static GetTruckResponse mapToGetTruckResponse(Truck truck) {
        return GetTruckResponse.builder()
                               .id(truck.getId())
                               .licensePlate(truck.getLicensePlate())
                               .capacity(truck.getCapacity())
                               .coordinates(truck.getCoordinates().toDto())
                               .inspectionDate(truck.getInspectionDate())
                               .truckStatus(truck.getTruckStatus())
                               .build();

    }

    public static UpdateTruckResponse mapToUpdateTruckResponse(Truck truck) {
        return UpdateTruckResponse.builder()
                                  .id(truck.getId())
                                  .licensePlate(truck.getLicensePlate())
                                  .capacity(truck.getCapacity())
                                  .coordinates(truck.getCoordinates().toDto())
                                  .inspectionDate(truck.getInspectionDate())
                                  .truckStatus(truck.getTruckStatus())
                                  .build();
    }

    public static SetTruckCoordinatesResponse mapToSetTruckCoordinatesResponse(Truck truck) {
        return SetTruckCoordinatesResponse.builder()
                                          .id(truck.getId())
                                          .licensePlate(truck.getLicensePlate())
                                          .capacity(truck.getCapacity())
                                          .coordinates(truck.getCoordinates().toDto())
                                          .inspectionDate(truck.getInspectionDate())
                                          .truckStatus(truck.getTruckStatus())
                                          .build();

    }
}