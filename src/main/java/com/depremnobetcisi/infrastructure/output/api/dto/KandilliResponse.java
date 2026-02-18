package com.depremnobetcisi.infrastructure.output.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KandilliResponse(
        boolean status,
        int httpStatus,
        String serverloadms,
        String desc,
        Map<String, Object> metadata,
        List<KandilliEarthquake> result
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KandilliEarthquake(
            @JsonProperty("earthquake_id") String earthquakeId,
            String provider,
            String title,
            @JsonProperty("date_time") String dateTime,
            @JsonProperty("mag") double magnitude,
            double depth,
            @JsonProperty("geojson") GeoJson geojson,
            @JsonProperty("location_properties") LocationProperties locationProperties
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GeoJson(
            String type,
            List<Double> coordinates
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LocationProperties(
            @JsonProperty("closestCity") ClosestCity closestCity,
            @JsonProperty("epiCenter") EpiCenter epiCenter
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ClosestCity(
            String name,
            long distance
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EpiCenter(
            String name
    ) {}
}
