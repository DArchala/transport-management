package com.mxkoo.transport_management.service;

import com.mxkoo.transport_management.dto.nominatim.SearchCity;
import com.mxkoo.transport_management.dto.nominatim.SearchCityQueryParams;
import com.mxkoo.transport_management.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NominatimOpenStreetMapService {

    private static final String SEARCH_COORDINATES_URL = "https://nominatim.openstreetmap.org/search.php";

    private final RestClient restClient;

    public String findCoordinatesByCity(String city) {
        var searchCityQueryParams = SearchCityQueryParams.withQuery(city);

        var citiesArray = restClient.get()
                                    .uri(SEARCH_COORDINATES_URL, searchCityQueryParams.toMap())
                                    .retrieve()
                                    .body(SearchCity[].class);

        var citiesList = Optional.ofNullable(citiesArray)
                                 .map(Arrays::asList)
                                 .orElseThrow(() -> ApplicationException.notFound("City: %s, not found".formatted(city)));

        return citiesList.getFirst().getFormattedCoordinates();

    }
}
