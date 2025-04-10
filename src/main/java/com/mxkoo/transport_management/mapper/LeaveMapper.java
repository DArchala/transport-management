package com.mxkoo.transport_management.mapper;

import com.mxkoo.transport_management.dto.leave.GetLeaveResponse;
import com.mxkoo.transport_management.dto.leave.UpdateLeaveResponse;
import com.mxkoo.transport_management.entity.Leave;

public class LeaveMapper {

    public static GetLeaveResponse mapToGetLeaveResponse(Leave leave) {
        return GetLeaveResponse.builder()
                               .id(leave.getId())
                               .start(leave.getStart())
                               .end(leave.getEnd())
                               .build();
    }

    public static UpdateLeaveResponse mapToUpdateLeaveResponse(Leave leave) {
        return UpdateLeaveResponse.builder()
                                  .id(leave.getId())
                                  .start(leave.getStart())
                                  .end(leave.getEnd())
                                  .build();
    }
}
