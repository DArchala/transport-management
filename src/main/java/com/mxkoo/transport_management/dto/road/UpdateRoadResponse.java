package com.mxkoo.transport_management.dto.road;

import com.mxkoo.transport_management.constant.RoadStatus;

import java.time.LocalDate;

public record UpdateRoadResponse(Long id,
                                 String from,
                                 String[] via,
                                 String to,
                                 LocalDate departureDate,
                                 LocalDate arrivalDate,
                                 Double distance,
                                 Double price,
                                 RoadStatus roadStatus) {
}
