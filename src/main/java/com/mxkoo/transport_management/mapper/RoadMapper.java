package com.mxkoo.transport_management.mapper;

import com.mxkoo.transport_management.dto.road.CreateRoadResponse;
import com.mxkoo.transport_management.dto.road.GetRoadResponse;
import com.mxkoo.transport_management.dto.road.UpdateRoadResponse;
import com.mxkoo.transport_management.entity.Road;

public class RoadMapper {

    public static GetRoadResponse mapToGetRoadResponse(Road road) {
        return new GetRoadResponse(road.getId(),
                                   road.getFrom(),
                                   road.getVia(),
                                   road.getTo(),
                                   road.getDepartureDate(),
                                   road.getArrivalDate(),
                                   road.getDistance(),
                                   road.getPrice(),
                                   road.getRoadStatus()
        );
    }

    public static UpdateRoadResponse mapToUpdateRoadResponse(Road road) {
        return new UpdateRoadResponse(road.getId(),
                                      road.getFrom(),
                                      road.getVia(),
                                      road.getTo(),
                                      road.getDepartureDate(),
                                      road.getArrivalDate(),
                                      road.getDistance(),
                                      road.getPrice(),
                                      road.getRoadStatus());
    }

    public static CreateRoadResponse mapToCreateRoadResponse(Road road) {
        return new CreateRoadResponse(road.getId(),
                                      road.getFrom(),
                                      road.getVia(),
                                      road.getTo(),
                                      road.getDepartureDate(),
                                      road.getArrivalDate(),
                                      road.getDistance(),
                                      road.getPrice(),
                                      road.getRoadStatus());
    }
}