package com.mxkoo.transport_management.dto.nominatim;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record SearchCity(String lat, String lon) {

    @JsonIgnore
    public String getFormattedCoordinates() {
        return String.format("%s,%s", lat, lon);
    }
}
