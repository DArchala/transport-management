package com.mxkoo.transport_management.dto.leave;

import java.time.LocalDate;

public record UpdateLeaveRequest(Long id,
                                 Long driverId,
                                 LocalDate start,
                                 LocalDate end) {
}
