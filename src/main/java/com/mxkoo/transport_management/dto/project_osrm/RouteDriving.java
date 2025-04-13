package com.mxkoo.transport_management.dto.project_osrm;

import java.util.List;

public record RouteDriving(List<Route> routes) {

    public record Route(Double distance) {}

}
