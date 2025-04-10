package com.mxkoo.transport_management.dto.leave;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetLeaveResponse(Long id,
                               LocalDate start,
                               LocalDate end) {
}
