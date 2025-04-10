package com.mxkoo.transport_management.dto.leave;

import java.time.LocalDate;

public record UpdateLeaveResponse(Long id,
                                  LocalDate start,
                                  LocalDate end) {
}
