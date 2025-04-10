package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.constant.DriverStatus;
import com.mxkoo.transport_management.dto.leave.CreateLeaveRequest;
import com.mxkoo.transport_management.dto.leave.GetLeaveResponse;
import com.mxkoo.transport_management.dto.leave.UpdateLeaveRequest;
import com.mxkoo.transport_management.dto.leave.UpdateLeaveResponse;
import com.mxkoo.transport_management.entity.Driver;
import com.mxkoo.transport_management.entity.Leave;
import com.mxkoo.transport_management.mapper.LeaveMapper;
import com.mxkoo.transport_management.repository.DriverRepository;
import com.mxkoo.transport_management.repository.LeaveRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final DriverRepository driverRepository;
    private final LeaveRepository leaveRepository;

    @Transactional
    public void createLeave(Long driverId, CreateLeaveRequest createLeaveRequest) throws Exception {
        if (createLeaveRequest.start()
                              .isAfter(createLeaveRequest.end())
            && createLeaveRequest.start()
                                 .isBefore(LocalDate.now())
            && createLeaveRequest.end()
                                 .isBefore(LocalDate.now())) {
            throw new IllegalArgumentException();
        }

        int leaveDays = (int) ChronoUnit.DAYS.between(createLeaveRequest.start(), createLeaveRequest.end()) + 1;

        Driver driver = driverRepository.findById(driverId)
                                        .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono kierowcy"));

        if (driver.getRoads()
                  .stream()
                  .anyMatch(road -> road.getDepartureDate()
                                        .isBefore(createLeaveRequest.end()) && road.getArrivalDate()
                                                                                   .isAfter(createLeaveRequest.start()))) {
            throw new Exception("Masz trasę w tym terminie");
        }

        if (driver.getDaysOffLeft() < leaveDays) {
            throw new IllegalStateException("Zbyt malo wolnych dni dostepnych");
        }
        List<Leave> leaves = driver.getLeaves();
        for (Leave leave : leaves) {
            if (!(leave.getEnd()
                       .isBefore(createLeaveRequest.start()) || leave.getStart()
                                                                     .isAfter(createLeaveRequest.end()))) {
                throw new Exception("Masz juz w tym czasie urlop");
            }
        }

        for (LocalDate date = createLeaveRequest.start(); !date.isAfter(createLeaveRequest.end()); date = date.plusDays(1)) {
            int driversOnLeave = leaveRepository.countActiveLeavesOnDate(date);
            if (driversOnLeave >= 2) {
                throw new IllegalStateException("Za duzo kierowcow na urlopie w dniu: " + date);
            }
        }

        Leave leave = new Leave();
        leave.setDriver(driver);
        leave.setStart(createLeaveRequest.start());
        leave.setEnd(createLeaveRequest.end());
        leaveRepository.save(leave);
        scheduleDriverStatusUpdate(driverId, createLeaveRequest.start(), DriverStatus.ON_VACATION);
        driver.setDaysOffLeft(driver.getDaysOffLeft() - leaveDays);
        driverRepository.save(driver);

        scheduleDriverStatusUpdate(driverId,
                                   createLeaveRequest.end()
                                                     .plusDays(1),
                                   DriverStatus.WAITING_FOR_ROAD);
    }

    @Transactional
    public List<GetLeaveResponse> getAllLeaves() {
        return leaveRepository.findAll()
                              .stream()
                              .map(LeaveMapper::mapToGetLeaveResponse)
                              .toList();
    }

    public GetLeaveResponse getLeaveById(Long id) throws Exception {
        Leave leave = leaveRepository.findById(id)
                                     .orElseThrow(Exception::new);
        return LeaveMapper.mapToGetLeaveResponse(leave);
    }

    @Transactional
    public UpdateLeaveResponse updateLeave(Long leaveId, UpdateLeaveRequest updateLeaveRequest) {
        Leave leave = leaveRepository.findById(leaveId)
                                     .orElseThrow();
        if (ChronoUnit.DAYS.between(LocalDate.now(), leave.getStart()) < 7) {
            throw new IllegalArgumentException("Można edytować urlop do 7 dni przed wyjazdem");
        }
        Driver driver = driverRepository.findById(updateLeaveRequest.driverId())
                                        .orElseThrow();
        leave.setDriver(driver);

        if (updateLeaveRequest.start() != null) {
            leave.setStart(updateLeaveRequest.start());
        }
        if (updateLeaveRequest.end() != null) {
            leave.setEnd(updateLeaveRequest.end());
        }
        return LeaveMapper.mapToUpdateLeaveResponse(leaveRepository.save(leave));
    }

    @Transactional
    public void cancelLeave(Long leaveId) throws Exception {
        Leave leave = leaveRepository.findById(leaveId)
                                     .orElseThrow();
        if ((ChronoUnit.DAYS.between(LocalDate.now(), leave.getStart())) < 7) {
            throw new Exception("Możesz odwołać urlop do 7 dni przed datą jego startu.");
        }
        int leaveDays = (int) ChronoUnit.DAYS.between(leave.getStart(), leave.getEnd());
        leaveRepository.deleteById(leaveId);
        Driver driver = driverRepository.findById(leave.getDriver()
                                                       .getId())
                                        .orElseThrow();
        driver.setDaysOffLeft(driver.getDaysOffLeft() + leaveDays + 1);
        driverRepository.save(driver);
    }

    private void scheduleDriverStatusUpdate(Long driverId, LocalDate updateDate, DriverStatus status) {
        Runnable task = () -> {
            Driver driver = driverRepository.findById(driverId)
                                            .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono kierowcy"));
            driver.setDriverStatus(status);
            driverRepository.save(driver);
        };

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long delay = ChronoUnit.DAYS.between(LocalDate.now(), updateDate) * 24 * 60 * 60;
        scheduler.schedule(task, delay, TimeUnit.SECONDS);
    }


}
