package com.mxkoo.transport_management.mapper;

import com.mxkoo.transport_management.dto.truck.GetTruckResponse;
import com.mxkoo.transport_management.dto.truck.SetTruckCoordinatesResponse;
import com.mxkoo.transport_management.dto.truck.UpdateTruckResponse;
import com.mxkoo.transport_management.entity.Truck;

public class TruckMapper {

    public static GetTruckResponse mapToGetTruckResponse(Truck truck) {
        return new GetTruckResponse(truck.getId(),
                                    truck.getLicensePlate(),
                                    truck.getCapacity(),
                                    truck.getCoordinates()
                                         .toDto(),
                                    truck.getInspectionDate(),
                                    truck.getTruckStatus());
    }

    public static UpdateTruckResponse mapToUpdateTruckResponse(Truck truck) {
        return new UpdateTruckResponse(truck.getId(),
                                       truck.getLicensePlate(),
                                       truck.getCapacity(),
                                       truck.getCoordinates()
                                            .toDto(),
                                       truck.getInspectionDate(),
                                       truck.getTruckStatus());
    }

    public static SetTruckCoordinatesResponse mapToSetTruckCoordinatesResponse(Truck truck) {
        return new SetTruckCoordinatesResponse(truck.getId(),
                                               truck.getLicensePlate(),
                                               truck.getCapacity(),
                                               truck.getCoordinates()
                                                    .toDto(),
                                               truck.getInspectionDate(),
                                               truck.getTruckStatus());
    }
}