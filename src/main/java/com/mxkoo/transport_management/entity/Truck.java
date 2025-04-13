package com.mxkoo.transport_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mxkoo.transport_management.constant.TruckStatus;
import jakarta.persistence.*;
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
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String licensePlate;

    private Integer capacity;

    @Embedded
    private Coordinates coordinates;

    private LocalDate inspectionDate;

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Road> roads = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TruckStatus truckStatus;

    public static Truck create(String licensePlate, Integer capacity, LocalDate inspectionDate) {
        return new Truck(null,
                         licensePlate,
                         capacity,
                         null,
                         inspectionDate,
                         null,
                         TruckStatus.WAITING_FOR_ROAD);
    }

    public void applyResolvedStatus(TruckStatus truckStatus) {
        this.truckStatus = truckStatus;
    }

    public void update(String licensePlate, Integer capacity, LocalDate inspectionDate, TruckStatus truckStatus) {
        if (licensePlate != null) {
            this.licensePlate = licensePlate;
        }
        if (capacity != null) {
            this.capacity = capacity;
        }
        if (inspectionDate != null) {
            this.inspectionDate = inspectionDate;
        }
        if (truckStatus != null) {
            this.truckStatus = truckStatus;
        }
    }

    public void applyNewCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
