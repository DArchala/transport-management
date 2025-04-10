package com.mxkoo.transport_management.dto.driver;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.dto.coordinates.CoordinatesDto;

public record GetDriverLocationResponse(Long id,
                                        CoordinatesDto coordinates,
                                        String name,
                                        String lastName,
                                        Long contactNumber,
                                        DriverStatus driverStatus) {
}
