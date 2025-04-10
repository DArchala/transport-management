package com.mxkoo.transport_management.dto.road;

import com.mxkoo.transport_management.constant.RoadStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateRoadResponse(Long id,
                                 String from,
                                 String[] via,
                                 String to,
                                 LocalDate departureDate,
                                 LocalDate arrivalDate,
                                 Double distance,
                                 Double price,
                                 RoadStatus roadStatus) {
}
