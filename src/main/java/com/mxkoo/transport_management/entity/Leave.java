package com.mxkoo.transport_management.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "LEAVE")
@Entity
@Getter
@Setter
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

    @Column(name = "\"start\"")
    private LocalDate start;

    @Column(name = "\"end\"")
    private LocalDate end;

    public boolean endsWith(LocalDate today) {
        return today.equals(end);
    }

}
