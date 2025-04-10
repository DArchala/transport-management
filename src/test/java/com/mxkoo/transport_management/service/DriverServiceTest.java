package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.dto.coordinates.CoordinatesDto;
import com.mxkoo.transport_management.entity.Coordinates;
import com.mxkoo.transport_management.dto.driver.CreateDriverRequest;
import com.mxkoo.transport_management.dto.driver.UpdateDriverRequest;
import com.mxkoo.transport_management.dto.driver.UpdateDriverResponse;
import com.mxkoo.transport_management.entity.Driver;
import com.mxkoo.transport_management.mapper.DriverMapper;
import com.mxkoo.transport_management.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceTest {

    private DriverRepository driverRepository;
    private DriverService driverService;
    private DriverMapper driverMapper;

    @BeforeEach
    void prepare(){
        driverRepository = mock(DriverRepository.class);
        driverMapper = mock(DriverMapper.class);
        driverService = new DriverService(driverRepository, driverMapper);
    }

    @Test
    void createDriver() {
        // Given
        CreateDriverRequest createDriverRequest = new CreateDriverRequest("Leo", "Messi", new CoordinatesDto(50, 50),
                                                      "messi@mail.com", 12345663L,
                                                      null, 5);

        Driver created = new Driver();
        created.setId(1L);
        created.setName("Leo");
        created.setLastName("Messi");
        created.setCoordinates(new Coordinates(50, 50));
        created.setEmail("messi@mail.com");
        created.setContactNumber(12345663L);
        created.setRoads(new ArrayList<>());
        created.setDaysOffLeft(5);
        created.setLeaves(null);

        when(driverRepository.save(any(Driver.class))).thenReturn(created);

        // When
        driverService.createDriver(createDriverRequest);

        // Then
        assertDoesNotThrow(() -> driverRepository.findById(1L));
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void updateDriver() throws Exception {
        UpdateDriverRequest updateDriverRequest = new UpdateDriverRequest(1L, "Leo", "Messi", new CoordinatesDto(50, 50),
                                                             "messi@mail.com", 12345663L, null, 25);
        Driver driver = new Driver(1L, "Leo", "Messi", new Coordinates(50, 50),
                                   "messi@mail.com", 12345663L, null, null, 25, null);


        when(driverRepository.findById(eq(1L))).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UpdateDriverResponse updateDriverResponse = driverService.updateDriver(1L, updateDriverRequest);

        // Then
        assertNotNull(updateDriverRequest);
        assertNotNull(updateDriverResponse);
        assertEquals(updateDriverRequest.id(), updateDriverResponse.id());
        assertTrue(driverRepository.findById(1L).isPresent());

        verify(driverRepository, atLeastOnce()).findById(1L);
        verify(driverRepository).save(any(Driver.class));
    }
    @Test
    void getDriver_WhenDoesNotExist(){
        //given
        Long id = 1L;

        //when
        when(driverRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> driverService.findById(id));
        //then
        verify(driverRepository).findById(id);
    }

    @Test
    void getDriver_WhenIsOnTheWay_ShouldThrowException(){
        //given
        Driver driver = new Driver();
        driver.setDriverStatus(DriverStatus.ON_THE_WAY);
        driver.setRoads(new ArrayList<>());

        when(driverRepository.findDriverByDriverStatus(DriverStatus.WAITING_FOR_ROAD))
                .thenReturn(Collections.emptyList());

        //when & then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> driverService.getAvailableDriverNotOnRoad(LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 20)));
        assertEquals("Nie znaleziono kierowcy", exception.getMessage());

    }
}
