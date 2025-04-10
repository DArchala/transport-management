package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.dto.driver.*;
import com.mxkoo.transport_management.entity.Coordinates;
import com.mxkoo.transport_management.entity.Driver;
import com.mxkoo.transport_management.exception.ApplicationException;
import com.mxkoo.transport_management.mapper.DriverMapper;
import com.mxkoo.transport_management.repository.DriverRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository repository;
    private final DriverMapper mapper;

    @Transactional
    public void createDriver(CreateDriverRequest createDriverRequest) {
        repository.save(Driver.create(createDriverRequest.name(),
                                      createDriverRequest.lastName(),
                                      createDriverRequest.coordinates(),
                                      createDriverRequest.email(),
                                      createDriverRequest.contactNumber()));
    }

    public GetDriverResponse findById(Long id) {
        return mapper.toGetDriverResponse(repository.findById(id)
                                                    .orElseThrow(() -> ApplicationException.notFound("Driver with id %s does not exists".formatted(id))));
    }

    public List<GetDriverResponse> getAllDrivers() {
        return repository.findAll()
                         .stream()
                         .map(mapper::toGetDriverResponse)
                         .toList();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteAllDrivers() {
        var drivers = repository.findAll();
        repository.deleteAll(drivers);
    }

    public UpdateDriverResponse updateDriver(Long id, UpdateDriverRequest request) {
        Driver driver = repository.findById(id)
                                  .orElseThrow(() -> ApplicationException.notFound("Driver with id %s does not exists".formatted(id)));
        driver.update(request.name(),
                      request.lastName(),
                      request.email(),
                      request.contactNumber(),
                      request.driverStatus());
        return mapper.toUpdateDriverResponse(driver);
    }

    public SetDriverCoordinatesResponse setCoordinatesForDriver(Long driverId, SetDriverCoordinatesRequest coordinates) {
        var driver = repository.findById(driverId)
                               .orElseThrow(() -> ApplicationException.notFound("Driver with id %s does not exists"));
        driver.applyNewCoordinates(new Coordinates(coordinates.x(), coordinates.y()));
        return mapper.toSetDriverCoordinatesResponse(repository.save(driver));
    }

    @Transactional
    public Driver getAvailableDriverNotOnRoad(LocalDate arrivalDate, LocalDate departureDate) {
        return repository.findDriverByDriverStatus(DriverStatus.WAITING_FOR_ROAD)
                         .stream()
                         .filter(driver -> driver.isNotOnRoad(arrivalDate, departureDate))
                         .findFirst()
                         .orElseThrow(() -> ApplicationException.notFound("Not found any free driver"));
    }

}
