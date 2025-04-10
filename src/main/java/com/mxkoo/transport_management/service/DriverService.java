package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.entity.Coordinates;
import com.mxkoo.transport_management.dto.driver.*;
import com.mxkoo.transport_management.entity.Driver;
import com.mxkoo.transport_management.mapper.DriverMapper;
import com.mxkoo.transport_management.repository.DriverRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository repository;
    private final DriverStatusService driverStatusService;

    @Transactional
    public void createDriver(CreateDriverRequest createDriverRequest) {
        Driver driver = new Driver();
        driver.setName(createDriverRequest.name());
        driver.setLastName(createDriverRequest.lastName());
        driver.setEmail(createDriverRequest.email());
        driver.setContactNumber(createDriverRequest.contactNumber());
        driverStatusService.setStatusForDriver(driver);
        repository.save(driver);
    }

    public GetDriverResponse getDriverById(Long id) throws Exception {
        var driver = repository.findById(id)
                               .orElseThrow(Exception::new);
        return DriverMapper.mapToGetDriverResponse(driver);
    }

    public List<GetDriverResponse> getAllDrivers() {
        List<Driver> drivers = repository.findAll();
        return drivers.stream()
                      .map(DriverMapper::mapToGetDriverResponse)
                      .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAllDrivers() {
        var drivers = repository.findAll();
        repository.deleteAll(drivers);
    }

    @Transactional
    public GetDriverResponse findDriver(Long id) throws Exception {
        return DriverMapper.mapToGetDriverResponse(repository.findById(id)
                                                       .orElseThrow(Exception::new));
    }

    @Transactional
    public UpdateDriverResponse updateDriver(Long id, UpdateDriverRequest request) {
        Driver driver = repository.findById(id)
                                  .orElseThrow();
        if (request.name() != null) {
            driver.setName(request.name());
        }
        if (request.lastName() != null) {
            driver.setLastName(request.lastName());
        }
        if (request.email() != null) {
            driver.setEmail(request.email());
        }
        if (request.contactNumber() != null) {
            driver.setContactNumber(request.contactNumber());
        }
        if (request.driverStatus() != null) {
            driver.setDriverStatus(request.driverStatus());
        }
        return DriverMapper.mapToUpdateDriverResponse(repository.save(driver));
    }

    @Transactional
    public SetDriverCoordinatesResponse setCoordinatesForDriver(Long driverId, SetDriverCoordinatesRequest coordinates) throws Exception {
        Driver driver = repository.findById(driverId)
                                  .orElseThrow(() -> new Exception("Driver not found with ID: " + driverId));
        driver.setCoordinates(new Coordinates(coordinates.x(), coordinates.y()));
        return DriverMapper.mapToSetDriverCoordinatesResponse(driver);
    }

    @Transactional
    public Driver getAvailableDriverNotOnRoad(LocalDate arrivalDate, LocalDate departureDate) {
        return repository.findDriverByDriverStatus(DriverStatus.WAITING_FOR_ROAD)
                         .stream()
                         .filter(driver -> driver.getRoads()
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
                         .orElseThrow(() -> new NoSuchElementException("Nie znaleziono kierowcy"));
    }

    private void checkIfExists(Long id) throws Exception {
        if (!repository.existsById(id)) {
            throw new Exception("Driver doesn't exist");
        }
    }

}
