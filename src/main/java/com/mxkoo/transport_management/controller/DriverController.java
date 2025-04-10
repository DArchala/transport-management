package com.mxkoo.transport_management.controller;

import com.mxkoo.transport_management.dto.driver.*;
import com.mxkoo.transport_management.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDriver(@Valid @RequestBody CreateDriverRequest createDriverRequest) {
        driverService.createDriver(createDriverRequest);
    }

    @GetMapping("/{id}")
    public GetDriverResponse getDriverById(@PathVariable Long id) throws Exception {
        return driverService.getDriverById(id);
    }

    @GetMapping("/all")
    public List<GetDriverResponse> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable Long id) throws Exception {
        driverService.deleteById(id);
    }

    @DeleteMapping("/all")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllDrivers() {
        driverService.deleteAllDrivers();
    }

    @PatchMapping("/{id}")
    public UpdateDriverResponse updateDriver(@PathVariable Long id, @Valid @RequestBody UpdateDriverRequest request) throws Exception {
        return driverService.updateDriver(id, request);
    }

    @PatchMapping("/coordinates/{driverId}")
    public SetDriverCoordinatesResponse setCoordinatesForDriver(@PathVariable Long driverId, @Valid @RequestBody SetDriverCoordinatesRequest request) throws Exception {
        return driverService.setCoordinatesForDriver(driverId, request);
    }


}
