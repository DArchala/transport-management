package com.mxkoo.transport_management.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LeaveDTO(
        Long id,
        DriverDTO driverDTO,
        LocalDate start,
        LocalDate end
) {
}
