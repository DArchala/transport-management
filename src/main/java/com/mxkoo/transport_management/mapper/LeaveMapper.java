package com.mxkoo.transport_management.mapper;

import com.mxkoo.transport_management.dto.leave.GetLeaveResponse;
import com.mxkoo.transport_management.dto.leave.UpdateLeaveResponse;
import com.mxkoo.transport_management.entity.Leave;

public class LeaveMapper {

    public static GetLeaveResponse mapToGetLeaveResponse(Leave leave) {
        return new GetLeaveResponse(leave.getId(),
                                    leave.getStart(),
                                    leave.getEnd());
    }

    public static UpdateLeaveResponse mapToUpdateLeaveResponse(Leave leave) {
        return new UpdateLeaveResponse(leave.getId(),
                                       leave.getStart(),
                                       leave.getEnd());
    }
}
