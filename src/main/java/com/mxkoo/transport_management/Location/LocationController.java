package com.mxkoo.transport_management.Location;

import com.mxkoo.transport_management.Location.LocationDriver.LocationDriverDTO;
import com.mxkoo.transport_management.Location.LocationDriver.LocationDriverService;
import com.mxkoo.transport_management.Location.LocationRoad.LocationRoadDTO;
import com.mxkoo.transport_management.Location.LocationRoad.LocationRoadService;
import com.mxkoo.transport_management.Location.LocationTruck.LocationTruckDTO;
import com.mxkoo.transport_management.Location.LocationTruck.LocationTruckService;
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
    public List<LocationDriverDTO> getDrivers() {
        return locationDriverService.getDriverLocations();
    }

    @GetMapping("/trucks")
    @ResponseBody
    public List<LocationTruckDTO> getTrucks() {
        return locationTruckService.getTruckLocations();
    }

    @GetMapping("/roads")
    @ResponseBody
    public List<LocationRoadDTO> getRoads() {
        return locationRoadService.getRoadLocation();
    }

    @GetMapping("/map")
    public String getMap(Model model) {
        List<LocationRoadDTO> roads = locationRoadService.getRoadLocation();
        model.addAttribute("road", roads);
        return "map";
    }
}
