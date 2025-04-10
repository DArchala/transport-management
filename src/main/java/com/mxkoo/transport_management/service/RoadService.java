package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.constant.TruckStatus;
import com.mxkoo.transport_management.dto.road.*;
import com.mxkoo.transport_management.entity.Driver;
import com.mxkoo.transport_management.entity.Road;
import com.mxkoo.transport_management.entity.Truck;
import com.mxkoo.transport_management.mapper.RoadMapper;
import com.mxkoo.transport_management.repository.RoadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoadService {

    private final RoadRepository roadRepository;
    private final TruckService truckService;
    private final DriverService driverService;
    private final RoadStatusService roadStatusService;
    private final RestTemplate restTemplate;

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
        Road road = new Road();
        road.setFrom(createRoadRequest.from());
        road.setVia(createRoadRequest.via());
        road.setTo(createRoadRequest.to());
        road.setDepartureDate(createRoadRequest.departureDate());
        road.setArrivalDate(createRoadRequest.arrivalDate());
        Double distance = calculateDistance(createRoadRequest.from(), createRoadRequest.via(), createRoadRequest.to());
        Double roundDistance = (double) (Math.round(distance * 100) / 100);
        Double price = roundDistance * 7;
        road.setDistance(roundDistance);
        road.setPrice(price);
        road.setTruck(truck);
        road.setDriver(driver);
        roadStatusService.setStatusForRoad(road);
        return RoadMapper.mapToCreateRoadResponse(roadRepository.save(road));
    }

    @Transactional
    public UpdateRoadResponse updateRoad(Long id, UpdateRoadRequest updateRoadRequest) {
        Road road = roadRepository.findById(id)
                                  .orElseThrow();
        if (ChronoUnit.DAYS.between(LocalDate.now(), road.getDepartureDate()) < 7) {
            throw new IllegalArgumentException("Można edytować trasę do 7 dni przed wyjazdem");
        }

        if (updateRoadRequest.from() != null) {
            road.setFrom(updateRoadRequest.from());
        }
        if (updateRoadRequest.via() != null) {
            road.setVia(updateRoadRequest.via());
        }
        if (updateRoadRequest.to() != null) {
            road.setTo(updateRoadRequest.to());
        }
        if (updateRoadRequest.departureDate() != null) {
            road.setDepartureDate(updateRoadRequest.departureDate());
        }
        if (updateRoadRequest.arrivalDate() != null) {
            road.setArrivalDate(updateRoadRequest.arrivalDate());
        }
        if (updateRoadRequest.roadStatus() != null) {
            road.setRoadStatus(updateRoadRequest.roadStatus());
        }
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
        String url = String.format(
                "https://router.project-osrm.org/route/v1/driving/%s;%s?overview=false",
                geocodeCity(from), geocodeCity(to)
                                  );

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        if (response.getBody() != null) {
            List<Map<String, Object>> routes = (List<Map<String, Object>>) response.getBody()
                                                                                   .get("routes");
            if (routes != null && !routes.isEmpty()) {
                return ((Number) routes.get(0)
                                       .get("distance")).doubleValue();
            }
        }

        throw new IllegalStateException("Unable to calculate distance between " + from + " and " + to);
    }

    private String geocodeCity(String city) {
        String url = String.format("https://nominatim.openstreetmap.org/search?format=json&q=%s", city);

        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        if (response.getBody() != null && !response.getBody()
                                                   .isEmpty()) {
            Map<String, Object> location = (Map<String, Object>) response.getBody()
                                                                         .get(0);
            double lat = Double.parseDouble(location.get("lat")
                                                    .toString());
            double lon = Double.parseDouble(location.get("lon")
                                                    .toString());
            return lon + "," + lat; // Format: longitude,latitude
        }

        throw new IllegalStateException("Unable to geocode city: " + city);
    }

    private void validateDate(LocalDate departureDate, LocalDate arrivalDate) {
        if (arrivalDate.isBefore(departureDate)) {
            throw new DateTimeException("Data przyjazdu musi być po dacie wyjazdu");
        }
        if (departureDate.isAfter(arrivalDate)) {
            throw new DateTimeException("Data wyjazdu musi być przed datą przyjazdu");
        }
        if (arrivalDate.isBefore(LocalDate.now()) || departureDate.isBefore(LocalDate.now())) {
            throw new DateTimeException("Data wyjazdu lub przyjazdu nie może być przed datą dzisiejszą");
        }
    }
}
