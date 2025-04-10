package com.mxkoo.transport_management.dto.leave;

import java.time.LocalDate;

public record CreateLeaveRequest(LocalDate start,
                                 LocalDate end) {
}
