package com.mxkoo.transport_management.entity;

import com.mxkoo.transport_management.constant.RoadStatus;
import com.mxkoo.transport_management.constant.TruckStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Road {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String from;

    private String[] via;

    @NotBlank
    private String to;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private Double distance;

    private Double price;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private RoadStatus roadStatus;

    public static Road create(String from,
                              String[] via,
                              String to,
                              LocalDate departureDate,
                              LocalDate arrivalDate,
                              Double roundDistance,
                              Double price,
                              Truck truck,
                              Driver driver,
                              LocalDate today) {
        var road = new Road(null,
                            from,
                            via,
                            to,
                            departureDate,
                            arrivalDate,
                            roundDistance,
                            price,
                            truck,
                            driver,
                            null);
        road.applyResolvedStatus(road.resolveRoadStatus(today));
        return road;
    }

    public boolean isDriverOnTheWay(LocalDate today) {
        return !today.isBefore(departureDate) && !today.isAfter(arrivalDate);
    }

    public TruckStatus resolveTruckStatus(LocalDate today) {
        if (!today.isBefore(departureDate) && !today.isAfter(arrivalDate)) {
            return TruckStatus.ON_THE_WAY;
        }
        return TruckStatus.WAITING_FOR_ROAD;
    }

    public RoadStatus resolveRoadStatus(LocalDate today) {
        if (!today.isBefore(departureDate) && !today.isAfter(arrivalDate)) {
            return RoadStatus.IN_PROGRESS;
        }
        else if (today.isBefore(departureDate)) {
            return RoadStatus.IN_FUTURE;
        }
        return RoadStatus.DONE;
    }

    public void applyResolvedStatus(RoadStatus roadStatus) {
        this.roadStatus = roadStatus;
    }

    public void update(String from, String[] via, String to, LocalDate departureDate, LocalDate arrivalDate, RoadStatus roadStatus) {
        if (from != null) {
            this.from = from;
        }
        if (via != null) {
            this.via = via;
        }
        if (this.to != null) {
            this.to = to;
        }
        if (this.departureDate != null) {
            this.departureDate = departureDate;
        }
        if (this.arrivalDate != null) {
            this.arrivalDate = arrivalDate;
        }
        if (roadStatus != null) {
            this.roadStatus = roadStatus;
        }
    }
}
