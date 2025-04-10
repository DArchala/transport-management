package com.mxkoo.transport_management.mapper;

import com.mxkoo.transport_management.dto.road.CreateRoadResponse;
import com.mxkoo.transport_management.dto.road.GetRoadResponse;
import com.mxkoo.transport_management.dto.road.UpdateRoadResponse;
import com.mxkoo.transport_management.entity.Road;

public class RoadMapper {

    public static GetRoadResponse mapToGetRoadResponse(Road road) {
        return GetRoadResponse.builder()
                              .id(road.getId())
                              .from(road.getFrom())
                              .via(road.getVia())
                              .to(road.getTo())
                              .departureDate(road.getDepartureDate())
                              .arrivalDate(road.getArrivalDate())
                              .distance(road.getDistance())
                              .price(road.getPrice())
                              .roadStatus(road.getRoadStatus())
                              .build();
    }

    public static UpdateRoadResponse mapToUpdateRoadResponse(Road road) {
        return UpdateRoadResponse.builder()
                                 .id(road.getId())
                                 .from(road.getFrom())
                                 .via(road.getVia())
                                 .to(road.getTo())
                                 .departureDate(road.getDepartureDate())
                                 .arrivalDate(road.getArrivalDate())
                                 .distance(road.getDistance())
                                 .price(road.getPrice())
                                 .roadStatus(road.getRoadStatus())
                                 .build();
    }

    public static CreateRoadResponse mapToCreateRoadResponse(Road road) {
        return CreateRoadResponse.builder()
                                 .id(road.getId())
                                 .from(road.getFrom())
                                 .via(road.getVia())
                                 .to(road.getTo())
                                 .departureDate(road.getDepartureDate())
                                 .arrivalDate(road.getArrivalDate())
                                 .distance(road.getDistance())
                                 .price(road.getPrice())
                                 .roadStatus(road.getRoadStatus())
                                 .build();
    }
}