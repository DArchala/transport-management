package com.mxkoo.transport_management.mapper;

import com.mxkoo.transport_management.dto.driver.GetDriverResponse;
import com.mxkoo.transport_management.dto.driver.SetDriverCoordinatesResponse;
import com.mxkoo.transport_management.dto.driver.UpdateDriverResponse;
import com.mxkoo.transport_management.entity.Driver;

public class DriverMapper {

    public static SetDriverCoordinatesResponse mapToSetDriverCoordinatesResponse(Driver driver) {
        return new SetDriverCoordinatesResponse(driver.getId(),
                                                driver.getName(),
                                                driver.getLastName(),
                                                driver.getCoordinates()
                                                      .toDto(),
                                                driver.getEmail(),
                                                driver.getContactNumber(),
                                                driver.getDriverStatus(),
                                                driver.getDaysOffLeft());
    }

    public static UpdateDriverResponse mapToUpdateDriverResponse(Driver driver) {
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

    public static GetDriverResponse mapToGetDriverResponse(Driver driver) {
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