package com.mxkoo.transport_management.controller;

import com.mxkoo.transport_management.dto.road.*;
import com.mxkoo.transport_management.service.RoadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/roads")
@RestController
public class RoadController {

    private final RoadService roadService;

    @PostMapping
    public CreateRoadResponse createRoad(@Valid @RequestBody CreateRoadRequest createRoadRequest, @RequestParam Integer capacity) {
        return roadService.createRoad(createRoadRequest, capacity);
    }

    @GetMapping("/{id}")
    public GetRoadResponse getRoadById(@PathVariable Long id) {
        return roadService.getRoadById(id);
    }

    @GetMapping("/truck-roads/{truckId}")
    public List<GetRoadResponse> getAllTruckRoads(@PathVariable Long truckId) {
        return roadService.getAllTruckRoads(truckId);
    }

    @GetMapping("/driver-roads/{driverId}")
    public List<GetRoadResponse> getAllDriverRoads(@PathVariable Long driverId) {
        return roadService.getAllDriverRoads(driverId);
    }

    @GetMapping("/all")
    public List<GetRoadResponse> getAllRoads() {
        return roadService.getAllRoads();
    }

    @PatchMapping("/{id}")
    public UpdateRoadResponse updateRoad(@PathVariable Long id, @Valid @RequestBody UpdateRoadRequest updateRoadRequest) {
        return roadService.updateRoad(id, updateRoadRequest);
    }

    @DeleteMapping("/all")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllRoads() {
        roadService.deleteAllRoads();
    }
}
