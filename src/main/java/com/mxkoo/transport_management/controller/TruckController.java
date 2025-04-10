package com.mxkoo.transport_management.controller;

import com.mxkoo.transport_management.dto.truck.*;
import com.mxkoo.transport_management.service.TruckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trucks")
@RequiredArgsConstructor
public class TruckController {

    private final TruckService truckService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTruck(@Valid @RequestBody CreateTruckRequest createTruckRequest) {
        truckService.createTruck(createTruckRequest);
    }

    @GetMapping("/{id}")
    public GetTruckResponse getTruck(@PathVariable Long id) {
        return truckService.getTruckById(id);
    }

    @GetMapping("/all")
    public List<GetTruckResponse> getAllTrucks() {
        return truckService.getAllTrucks();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTruck(@PathVariable Long id) {
        truckService.deleteById(id);
    }

    @DeleteMapping("/all")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllTrucks() {
        truckService.deleteAllTrucks();
    }

    @PatchMapping("/{id}")
    public UpdateTruckResponse updateTruck(@PathVariable Long id, @Valid @RequestBody UpdateTruckRequest updateTruckRequest) {
        return truckService.updateTruck(id, updateTruckRequest);
    }

    @PatchMapping("/coordinates/{truckId}")
    public SetTruckCoordinatesResponse setCoordinatesForTruck(@PathVariable Long truckId, @RequestBody SetTruckCoordinatesRequest setTruckCoordinatesRequest) {
        return truckService.setCoordinatesForTruck(truckId, setTruckCoordinatesRequest);
    }
}
