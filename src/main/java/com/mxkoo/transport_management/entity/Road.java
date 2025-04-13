package com.mxkoo.transport_management.entity;

import com.mxkoo.transport_management.constant.RoadStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "ROAD")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Road {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "\"from\"")
    private String from;

    @Column(name = "\"via\"")
    private String[] via;

    @NotBlank
    @Column(name = "\"to\"")
    private String to;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "price")
    private Double price;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private RoadStatus roadStatus;

    public boolean isDriverOnTheWay(LocalDate today) {
        return !today.isBefore(departureDate) && !today.isAfter(arrivalDate);
    }

}
