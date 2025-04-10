package com.mxkoo.transport_management.mapper;

import com.mxkoo.transport_management.dto.LeaveDTO;
import com.mxkoo.transport_management.entity.Driver;
import com.mxkoo.transport_management.dto.DriverDTO;
import com.mxkoo.transport_management.entity.Leave;

import java.util.Optional;

public class LeaveMapper {

    public static Leave mapToEntity(LeaveDTO leaveDTO){
        Driver driver = Optional.ofNullable(leaveDTO.driverDTO())
                .map(DriverMapper::mapToEntityWithRoad)
                .orElse(null);
        return Leave.builder()
                .id(leaveDTO.id())
                .driver(driver)
                .start(leaveDTO.start())
                .end(leaveDTO.end())
                .build();
    }
    public static LeaveDTO mapToDTO(Leave leave){
        DriverDTO driverDTO = Optional.ofNullable(leave.getDriver())
                .map(DriverMapper::mapToDTOWithRoad)
                .orElse(null);
        return LeaveDTO.builder()
                .id(leave.getId())
                .driverDTO(driverDTO)
                .start(leave.getStart())
                .end(leave.getEnd())
                .build();
    }
}
