package com.mxkoo.transport_management.controller;

import com.mxkoo.transport_management.dto.Coordinates;
import com.mxkoo.transport_management.dto.DriverDTO;
import com.mxkoo.transport_management.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/add")
    public DriverDTO createDriver(@RequestBody @Validated DriverDTO driverDTO) {
        return driverService.createDriver(driverDTO);
    }

    @GetMapping("/{id}")
    public DriverDTO getDriverById(@PathVariable Long id) throws Exception {
        return driverService.getDriverById(id);
    }

    @GetMapping("/all")
    public List<DriverDTO> getAllDrivers() {
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
    public DriverDTO updateDriver(@PathVariable Long id, @RequestBody DriverDTO toUpdate) throws Exception {
        return driverService.updateDriver(id, toUpdate);
    }

    @PatchMapping("/coordinates" + "/{driverId}")
    public DriverDTO setCoordinatesForDriver(@PathVariable Long driverId, @RequestBody Coordinates coordinates) throws Exception {
        return driverService.setCoordinatesForDriver(driverId, coordinates);
    }


}
