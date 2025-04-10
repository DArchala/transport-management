package com.mxkoo.transport_management.dto;

import jakarta.persistence.Embeddable;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Coordinates {

    private double x;
    private double y;

}
