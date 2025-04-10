package com.mxkoo.transport_management.dto.road;

import com.mxkoo.transport_management.constant.RoadStatus;

import java.time.LocalDate;

public record GetRoadLocationResponse(Long id,
                                      String from,
                                      String[] via,
                                      String to,
                                      LocalDate departureDate,
                                      LocalDate arrivalDate,
                                      Double distance,
                                      Double price,
                                      Long driverId,
                                      Long truckId,
                                      RoadStatus roadStatus) {
}
