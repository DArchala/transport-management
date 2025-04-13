package com.mxkoo.transport_management.dto.nominatim;

import java.util.Map;

public record SearchCityQueryParams(String query, String format, String limit) {

    public static SearchCityQueryParams withQuery(String query) {
        return new SearchCityQueryParams(query, "json", "1");
    }

    public Map<String, String> toMap() {
        return Map.of("q", query, "format", format, "limit", limit);
    }
}
