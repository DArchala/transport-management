package com.mxkoo.transport_management.entity;

import com.mxkoo.transport_management.dto.coordinates.CoordinatesDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Coordinates {

    private double x;
    private double y;

    public static Coordinates from(CoordinatesDto coordinatesDto) {
        return new Coordinates(coordinatesDto.x(), coordinatesDto.y());
    }

    public CoordinatesDto toDto() {
        return new CoordinatesDto(x, y);
    }

}
