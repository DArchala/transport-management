package com.mxkoo.transport_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.dto.coordinates.CoordinatesDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @Embedded
    private Coordinates coordinates;

    @NotBlank
    @Email
    private String email;

    private Long contactNumber;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Road> roads = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    private Integer daysOffLeft;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Leave> leaves;

    public static Driver create(String name,
                                String lastName,
                                CoordinatesDto coordinatesDto,
                                String email,
                                Long contactNumber) {
        return new Driver(null,
                          name,
                          lastName,
                          Coordinates.from(coordinatesDto),
                          email,
                          contactNumber,
                          null,
                          DriverStatus.PENDING,
                          25,
                          null);
    }

    public boolean isNotOnRoad(LocalDate arrivalDate, LocalDate departureDate) {
        return roads.stream()
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
                              );
    }

    public void update(String name, String lastName, String email, Long contactNumber, DriverStatus driverStatus) {
        if (name != null) {
            this.name = name;
        }
        if (lastName != null) {
            this.lastName = lastName;
        }
        if (email != null) {
            this.email = email;
        }
        if (contactNumber != null) {
            this.contactNumber = contactNumber;
        }
        if (driverStatus != null) {
            this.driverStatus = driverStatus;
        }
    }

    public void updateCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void applyResolvedStatus(DriverStatus status) {
        this.driverStatus = status;
    }

    public void subtractFromDaysOff(int leaveDays) {
        if (leaveDays > daysOffLeft) {
            daysOffLeft = 0;
        }
        daysOffLeft = daysOffLeft - leaveDays;
    }

    public void addToDaysOffLeft(int leaveDays) {
        daysOffLeft = daysOffLeft + leaveDays + 1;
    }
}
