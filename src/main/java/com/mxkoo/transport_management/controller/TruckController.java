package com.mxkoo.transport_management.controller;

import com.mxkoo.transport_management.dto.Coordinates;
import com.mxkoo.transport_management.dto.TruckDTO;
import com.mxkoo.transport_management.service.TruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trucks")
@RequiredArgsConstructor
public class TruckController {
    private final TruckService truckService;

    @PostMapping()
    public TruckDTO createTruck(@RequestBody @Validated TruckDTO truckDTO) {
        return truckService.createTruck(truckDTO);
    }

    @GetMapping("/{id}")
    public TruckDTO getTruck(@PathVariable Long id) throws Exception {
        return truckService.getTruckById(id);
    }

    @GetMapping("/all")
    public List<TruckDTO> getAllTrucks() {
        return truckService.getAllTrucks();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTruck(@PathVariable Long id) throws Exception {
        truckService.deleteById(id);
    }

    @DeleteMapping("/all")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllTrucks() {
        truckService.deleteAllTrucks();
    }

    @PatchMapping("/{id}")
    public TruckDTO updateTruck(@PathVariable Long id, @RequestBody TruckDTO truckDTO) throws Exception {
        return truckService.updateTruck(id, truckDTO);
    }

    @PatchMapping("/coordinates/{truckId}")
    public TruckDTO setCoordinatesForTruck(@PathVariable Long truckId, @RequestBody Coordinates coordinates) throws Exception {
        return truckService.setCoordinatesForTruck(truckId, coordinates);
    }
}
