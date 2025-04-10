package com.mxkoo.transport_management.controller;

import com.mxkoo.transport_management.dto.RoadDTO;
import com.mxkoo.transport_management.service.RoadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roads")
public class RoadController {

    private final RoadService roadService;

    @PostMapping()
    public RoadDTO createRoad(@RequestBody @Valid RoadDTO roadDTO, @RequestParam int capacity) {
        return roadService.createRoad(roadDTO, capacity);
    }

    @GetMapping("/{id}")
    public RoadDTO getRoadById(@PathVariable Long id) {
        return roadService.getRoadById(id);
    }

    @GetMapping("/truck-roads/{truckId}")
    public List<RoadDTO> getAllTruckRoads(@PathVariable Long truckId) {
        return roadService.getAllTruckRoads(truckId);
    }

    @GetMapping("/driver-roads/{driverId}")
    public List<RoadDTO> getAllDriverRoads(@PathVariable Long driverId) {
        return roadService.getAllDriverRoads(driverId);
    }

    @GetMapping("/all")
    public List<RoadDTO> getAllRoads() {
        return roadService.getAllRoads();
    }

    @PatchMapping("/{id}")
    public RoadDTO updateRoad(@PathVariable Long id, @RequestBody RoadDTO toUpdate) {
        return roadService.updateRoad(id, toUpdate);
    }

    @DeleteMapping("/all")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllRoads() {
        roadService.deleteAllRoads();
    }
}
