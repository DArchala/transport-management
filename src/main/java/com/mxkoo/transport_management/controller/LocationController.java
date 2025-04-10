package com.mxkoo.transport_management.controller;

import com.mxkoo.transport_management.dto.driver.GetDriverLocationResponse;
import com.mxkoo.transport_management.dto.road.GetRoadLocationResponse;
import com.mxkoo.transport_management.dto.truck.GetTruckLocationResponse;
import com.mxkoo.transport_management.service.LocationDriverService;
import com.mxkoo.transport_management.service.LocationRoadService;
import com.mxkoo.transport_management.service.LocationTruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationDriverService locationDriverService;
    private final LocationTruckService locationTruckService;
    private final LocationRoadService locationRoadService;

    @GetMapping("/drivers")
    @ResponseBody
    public List<GetDriverLocationResponse> getDrivers() {
        return locationDriverService.getDriverLocations();
    }

    @GetMapping("/trucks")
    @ResponseBody
    public List<GetTruckLocationResponse> getTrucks() {
        return locationTruckService.getTruckLocations();
    }

    @GetMapping("/roads")
    @ResponseBody
    public List<GetRoadLocationResponse> getRoads() {
        return locationRoadService.getRoadLocation();
    }

    @GetMapping("/map")
    public String getMap(Model model) {
        List<GetRoadLocationResponse> roads = locationRoadService.getRoadLocation();
        model.addAttribute("road", roads);
        return "map";
    }
}
