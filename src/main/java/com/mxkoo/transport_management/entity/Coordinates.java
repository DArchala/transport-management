package com.mxkoo.transport_management.entity;

import com.mxkoo.transport_management.dto.coordinates.CoordinatesDto;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Coordinates {

    private double x;
    private double y;

    public CoordinatesDto toDto() {
        return new CoordinatesDto(x, y);
    }

}
