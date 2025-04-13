package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.component.ApplicationTime;
import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.constant.TruckStatus;
import com.mxkoo.transport_management.dto.project_osrm.RouteDriving;
import com.mxkoo.transport_management.dto.road.*;
import com.mxkoo.transport_management.entity.Driver;
import com.mxkoo.transport_management.entity.Road;
import com.mxkoo.transport_management.entity.Truck;
import com.mxkoo.transport_management.exception.ApplicationException;
import com.mxkoo.transport_management.mapper.RoadMapper;
import com.mxkoo.transport_management.repository.RoadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoadService {

    private final RoadRepository roadRepository;
    private final TruckService truckService;
    private final DriverService driverService;
    private final RoadStatusService roadStatusService;
    private final ProjectOsrmService projectOsrmService;
    private final NominatimOpenStreetMapService nominatimOpenStreetMapService;
    private final ApplicationTime applicationTime;

    public List<GetRoadResponse> getAllTruckRoads(Long truckId) {
        return roadRepository.getRoadByTruckId(truckId)
                             .stream()
                             .map(RoadMapper::mapToGetRoadResponse)
                             .toList();
    }

    public List<GetRoadResponse> getAllDriverRoads(Long driverId) {
        return roadRepository.getRoadByDriverId(driverId)
                             .stream()
                             .map(RoadMapper::mapToGetRoadResponse)
                             .toList();
    }

    @Transactional
    public CreateRoadResponse createRoad(CreateRoadRequest createRoadRequest, Integer capacity) {
        Truck truck = truckService.getAvailableTruck(capacity, createRoadRequest.arrivalDate(), createRoadRequest.departureDate());
        Driver driver = driverService.getAvailableDriverNotOnRoad(createRoadRequest.arrivalDate(), createRoadRequest.departureDate());

        if (!truck.getTruckStatus()
                  .equals(TruckStatus.WAITING_FOR_ROAD) || !driver.getDriverStatus()
                                                                  .equals(DriverStatus.WAITING_FOR_ROAD)) {
            throw new IllegalArgumentException("Pojazd lub kierowca nie jest gotowy do drogi");
        }
        validateDate(createRoadRequest.departureDate(), createRoadRequest.arrivalDate());

        Double distance = calculateDistance(createRoadRequest.from(), createRoadRequest.via(), createRoadRequest.to());
        Double roundDistance = (double) (Math.round(distance * 100) / 100);
        Double price = roundDistance * 7;

        Road road = Road.create(createRoadRequest.from(),
                                createRoadRequest.via(),
                                createRoadRequest.to(),
                                createRoadRequest.departureDate(),
                                createRoadRequest.arrivalDate(),
                                roundDistance,
                                price,
                                truck,
                                driver,
                                applicationTime.today());

        return RoadMapper.mapToCreateRoadResponse(roadRepository.save(road));
    }

    @Transactional
    public UpdateRoadResponse updateRoad(Long id, UpdateRoadRequest updateRoadRequest) {
        Road road = roadRepository.findById(id)
                                  .orElseThrow();
        if (ChronoUnit.DAYS.between(applicationTime.today(), road.getDepartureDate()) < 7) {
            throw new IllegalArgumentException("Można edytować trasę do 7 dni przed wyjazdem");
        }

        road.update(updateRoadRequest.from(),
                    updateRoadRequest.via(),
                    updateRoadRequest.to(),
                    updateRoadRequest.departureDate(),
                    updateRoadRequest.arrivalDate(),
                    updateRoadRequest.roadStatus());

        return RoadMapper.mapToUpdateRoadResponse(roadRepository.save(road));
    }

    @Transactional
    public List<GetRoadResponse> getAllRoads() {
        return roadRepository.findAll()
                             .stream()
                             .map(RoadMapper::mapToGetRoadResponse)
                             .toList();
    }

    @Transactional
    public void deleteAllRoads() {
        roadRepository.deleteAll();
    }

    @Transactional
    public GetRoadResponse getRoadById(Long id) {
        return RoadMapper.mapToGetRoadResponse(roadRepository.findById(id)
                                                             .orElseThrow());
    }

    @Transactional
    public double calculateDistance(String from, String[] via, String to) {
        List<String> allCities = new ArrayList<>();
        allCities.add(from);
        allCities.addAll(List.of(via));
        allCities.add(to);

        double totalDistance = 0;

        for (int i = 0; i < allCities.size() - 1; i++) {
            String origin = allCities.get(i);
            String destination = allCities.get(i + 1);

            totalDistance += calculateSegmentDistance(origin, destination);
        }

        return totalDistance / 1000;
    }

    private double calculateSegmentDistance(String from, String to) {
        var fromCoordinates = nominatimOpenStreetMapService.findCoordinatesByCity(from);
        var toCoordinates = nominatimOpenStreetMapService.findCoordinatesByCity(to);
        return Optional.ofNullable(projectOsrmService.getRoute(fromCoordinates, toCoordinates)
                                                     .routes())
                       .map(List::getFirst)
                       .stream()
                       .findFirst()
                       .map(RouteDriving.Route::distance)
                       .orElseThrow(() -> ApplicationException.of("Unable to calculate distance between %s and %s".formatted(from, to),
                                                                  HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private void validateDate(LocalDate departureDate, LocalDate arrivalDate) {
        var today = applicationTime.today();
        if (arrivalDate.isBefore(departureDate)) {
            throw new DateTimeException("Data przyjazdu musi być po dacie wyjazdu");
        }
        if (departureDate.isAfter(arrivalDate)) {
            throw new DateTimeException("Data wyjazdu musi być przed datą przyjazdu");
        }
        if (arrivalDate.isBefore(today) || departureDate.isBefore(today)) {
            throw new DateTimeException("Data wyjazdu lub przyjazdu nie może być przed datą dzisiejszą");
        }
    }
}
