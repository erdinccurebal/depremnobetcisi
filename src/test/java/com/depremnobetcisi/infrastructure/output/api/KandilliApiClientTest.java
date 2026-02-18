package com.depremnobetcisi.infrastructure.output.api;

import com.depremnobetcisi.domain.model.Earthquake;
import com.depremnobetcisi.domain.exception.EarthquakeApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class KandilliApiClientTest {

    private WireMockServer wireMock;
    private KandilliApiClientAdapter client;

    @BeforeEach
    void setUp() {
        wireMock = new WireMockServer(0);
        wireMock.start();
        WireMock.configureFor("localhost", wireMock.port());

        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:" + wireMock.port())
                .build();

        client = new KandilliApiClientAdapter(restClient, new ObjectMapper());
    }

    @AfterEach
    void tearDown() {
        wireMock.stop();
    }

    @Test
    void shouldParseKandilliResponse() {
        String response = """
                {
                    "status": true,
                    "httpStatus": 200,
                    "serverloadms": "50",
                    "desc": "success",
                    "metadata": {"date_starts": "2024.01.15 00:00:00", "date_ends": "2024.01.16 00:00:00", "count": 1},
                    "result": [
                        {
                            "earthquake_id": "test-001",
                            "provider": "kandilli",
                            "title": "AKYAZI-SAKARYA",
                            "date_time": "2024-01-15 14:30:00",
                            "mag": 4.2,
                            "depth": 7.0,
                            "geojson": {
                                "type": "Point",
                                "coordinates": [30.6, 40.7]
                            },
                            "location_properties": {
                                "closestCity": {
                                    "name": "Sakarya",
                                    "distance": 15000
                                },
                                "epipiCenter": {
                                    "name": "Akyazi"
                                }
                            }
                        }
                    ]
                }
                """;

        stubFor(get(urlEqualTo("/deprem/kandilli/live"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(response)));

        List<Earthquake> result = client.fetchLatestEarthquakes();

        assertEquals(1, result.size());
        Earthquake eq = result.get(0);
        assertEquals("test-001", eq.getEarthquakeId());
        assertEquals("AKYAZI-SAKARYA", eq.getTitle());
        assertEquals(4.2, eq.getMagnitude(), 0.01);
        assertEquals(7.0, eq.getDepthKm(), 0.01);
        // GeoJSON: [longitude, latitude]
        assertEquals(30.6, eq.getLongitude(), 0.01);
        assertEquals(40.7, eq.getLatitude(), 0.01);
        assertEquals("Sakarya", eq.getClosestCity());
    }

    @Test
    void shouldHandleApiError() {
        stubFor(get(urlEqualTo("/deprem/kandilli/live"))
                .willReturn(aResponse()
                        .withStatus(500)));

        assertThrows(EarthquakeApiException.class, () -> client.fetchLatestEarthquakes());
    }
}
