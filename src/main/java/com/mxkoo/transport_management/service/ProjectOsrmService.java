package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.dto.project_osrm.RouteDriving;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ProjectOsrmService {

    private static final String ROUTE_URL_TEMPLATE = "https://router.project-osrm.org/route/v1/driving/%s;%s?overview=false";

    private final RestClient restClient;

    public RouteDriving getRoute(String origin, String destination) {
        var url = ROUTE_URL_TEMPLATE.formatted(origin, destination);
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(RouteDriving.class);
    }


}
