package com.mxkoo.transport_management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonBackReference
    private Driver driver;

    private LocalDate start;

    private LocalDate end;

    public static Leave create(Driver driver, LocalDate start, LocalDate end) {
        return new Leave(null,
                         driver,
                         start,
                         end);
    }

    public boolean endsWith(LocalDate today) {
        return today.equals(end);
    }

    public void update(@NotNull Driver driver, LocalDate start, LocalDate end) {
        this.driver = driver;
        if (start != null) {
            this.start = start;
        }
        if (end != null) {
            this.end = end;
        }
    }
}
