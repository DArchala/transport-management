package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.constant.TruckStatus;
import com.mxkoo.transport_management.entity.Coordinates;
import com.mxkoo.transport_management.dto.truck.*;
import com.mxkoo.transport_management.entity.Truck;
import com.mxkoo.transport_management.mapper.TruckMapper;
import com.mxkoo.transport_management.repository.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TruckService {

    private final TruckRepository truckRepository;
    private final TruckStatusService truckStatusService;

    @Transactional
    public void createTruck(CreateTruckRequest createTruckRequest) {
        Truck truck = new Truck();
        truck.setLicensePlate(createTruckRequest.licensePlate());
        truck.setCapacity(createTruckRequest.capacity());
        truck.setInspectionDate(createTruckRequest.inspectionDate());
        truckStatusService.setStatusForTruck(truck);
        truckRepository.save(truck);
    }

    public GetTruckResponse getTruckById(Long id) {
        return TruckMapper.mapToGetTruckResponse(truckRepository.findById(id)
                                                                .orElseThrow());
    }

    @Transactional
    public List<GetTruckResponse> getAllTrucks() {
        return truckRepository.findAll()
                              .stream()
                              .map(TruckMapper::mapToGetTruckResponse)
                              .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        truckRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllTrucks() {
        truckRepository.deleteAll();
    }

    @Transactional
    public UpdateTruckResponse updateTruck(Long id, UpdateTruckRequest updateTruckRequest) {
        Truck truck = truckRepository.findById(id)
                                     .orElseThrow();
        if (updateTruckRequest.licensePlate() != null) {
            truck.setLicensePlate(updateTruckRequest.licensePlate());
        }
        if (updateTruckRequest.capacity() != null) {
            truck.setCapacity(updateTruckRequest.capacity());
        }
        if (updateTruckRequest.inspectionDate() != null) {
            truck.setInspectionDate(updateTruckRequest.inspectionDate());
        }
        if (updateTruckRequest.truckStatus() != null) {
            truck.setTruckStatus(updateTruckRequest.truckStatus());
        }
        return TruckMapper.mapToUpdateTruckResponse(truckRepository.save(truck));
    }

    @Transactional
    public SetTruckCoordinatesResponse setCoordinatesForTruck(Long truckId, SetTruckCoordinatesRequest setTruckCoordinatesRequest) {
        Truck truck = truckRepository.findById(truckId)
                                     .orElseThrow(() -> new NoSuchElementException("Truck not found with ID: " + truckId));
        truck.setCoordinates(new Coordinates(setTruckCoordinatesRequest.x(), setTruckCoordinatesRequest.y()));
        return TruckMapper.mapToSetTruckCoordinatesResponse(truck);
    }

    @Transactional
    public Truck getAvailableTruck(Integer capacity, LocalDate arrivalDate, LocalDate departureDate) {
        return truckRepository.findByCapacityAndTruckStatus(capacity, TruckStatus.WAITING_FOR_ROAD)
                              .stream()
                              .filter(truck -> truck.getRoads()
                                                    .stream()
                                                    .noneMatch(eachRoad ->
                                                                       (eachRoad.getArrivalDate()
                                                                                .isBefore(arrivalDate) && eachRoad.getDepartureDate()
                                                                                                                  .isAfter(arrivalDate)) ||
                                                                       (eachRoad.getArrivalDate()
                                                                                .isBefore(departureDate) && eachRoad.getDepartureDate()
                                                                                                                    .isAfter(departureDate)) ||
                                                                       (eachRoad.getArrivalDate()
                                                                                .equals(arrivalDate) || eachRoad.getDepartureDate()
                                                                                                                .equals(departureDate))
                                                              ))
                              .findFirst()
                              .orElseThrow(() -> new NoSuchElementException("Nie znaleziono pojazdu"));
    }

}
