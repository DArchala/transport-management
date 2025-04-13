package com.mxkoo.transport_management.mapper;

import com.mxkoo.transport_management.dto.driver.GetDriverResponse;
import com.mxkoo.transport_management.dto.driver.UpdateDriverCoordinatesResponse;
import com.mxkoo.transport_management.dto.driver.UpdateDriverResponse;
import com.mxkoo.transport_management.entity.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public UpdateDriverCoordinatesResponse toUpdateDriverCoordinatesResponse(Driver driver) {
        return new UpdateDriverCoordinatesResponse(driver.getId(),
                                                   driver.getName(),
                                                   driver.getLastName(),
                                                   driver.getCoordinates()
                                                      .toDto(),
                                                   driver.getEmail(),
                                                   driver.getContactNumber(),
                                                   driver.getDriverStatus(),
                                                   driver.getDaysOffLeft());
    }

    public UpdateDriverResponse toUpdateDriverResponse(Driver driver) {
        return new UpdateDriverResponse(driver.getId(),
                                        driver.getName(),
                                        driver.getLastName(),
                                        driver.getCoordinates()
                                              .toDto(),
                                        driver.getEmail(),
                                        driver.getContactNumber(),
                                        driver.getDriverStatus(),
                                        driver.getDaysOffLeft());
    }

    public GetDriverResponse toGetDriverResponse(Driver driver) {
        return new GetDriverResponse(driver.getId(),
                                     driver.getName(),
                                     driver.getLastName(),
                                     driver.getCoordinates()
                                           .toDto(),
                                     driver.getEmail(),
                                     driver.getContactNumber(),
                                     driver.getDriverStatus(),
                                     driver.getDaysOffLeft());
    }

}