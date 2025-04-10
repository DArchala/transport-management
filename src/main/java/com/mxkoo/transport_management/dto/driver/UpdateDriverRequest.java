package com.mxkoo.transport_management.dto.driver;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.dto.coordinates.CoordinatesDto;

public record UpdateDriverRequest(Long id,
                                  String name,
                                  String lastName,
                                  CoordinatesDto coordinates,
                                  String email,
                                  Long contactNumber,
                                  DriverStatus driverStatus,
                                  int daysOffLeft) {
}
