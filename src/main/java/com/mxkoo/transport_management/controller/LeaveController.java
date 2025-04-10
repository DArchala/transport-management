package com.mxkoo.transport_management.controller;

import com.mxkoo.transport_management.dto.leave.CreateLeaveRequest;
import com.mxkoo.transport_management.dto.leave.GetLeaveResponse;
import com.mxkoo.transport_management.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/leave/{driverId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLeave(@PathVariable Long driverId, @Valid CreateLeaveRequest createLeaveRequest) throws Exception {
        leaveService.createLeave(driverId, createLeaveRequest);
    }

    @GetMapping("/leave/all")
    public List<GetLeaveResponse> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

    @GetMapping("/leave/{id}")
    public GetLeaveResponse getLeaveById(@PathVariable Long id) throws Exception {
        return leaveService.getLeaveById(id);
    }

    @DeleteMapping("/leave/cancel/{leaveId}")
    public void cancelLeave(@PathVariable Long leaveId) throws Exception {
        leaveService.cancelLeave(leaveId);
    }


}
