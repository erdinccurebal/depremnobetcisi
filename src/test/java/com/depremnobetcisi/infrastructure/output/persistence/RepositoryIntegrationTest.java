package com.depremnobetcisi.infrastructure.output.persistence;

import com.depremnobetcisi.infrastructure.output.persistence.entity.EarthquakeEntity;
import com.depremnobetcisi.infrastructure.output.persistence.entity.HelpRequestEntity;
import com.depremnobetcisi.infrastructure.output.persistence.entity.UserEntity;
import com.depremnobetcisi.infrastructure.output.persistence.repository.JpaEarthquakeRepository;
import com.depremnobetcisi.infrastructure.output.persistence.repository.JpaHelpRequestRepository;
import com.depremnobetcisi.infrastructure.output.persistence.repository.JpaUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class RepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaEarthquakeRepository earthquakeRepository;

    @Autowired
    private JpaHelpRequestRepository helpRequestRepository;

    @Test
    void shouldSaveAndFindUser() {
        UserEntity user = new UserEntity();
        user.setTelegramChatId(12345L);
        user.setTelegramUsername("testuser");
        user.setLatitude(39.0);
        user.setLongitude(35.0);

        userRepository.save(user);

        var found = userRepository.findByTelegramChatId(12345L);
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getTelegramUsername());
    }

    @Test
    void shouldFindActiveUsersInBoundingBox() {
        UserEntity user = new UserEntity();
        user.setTelegramChatId(99999L);
        user.setLatitude(39.0);
        user.setLongitude(35.0);
        user.setActive(true);
        user.setMinMagnitude(4.0);
        userRepository.save(user);

        List<UserEntity> result = userRepository.findActiveUsersInBoundingBox(
                38.0, 40.0, 34.0, 36.0, 4.0);

        assertFalse(result.isEmpty());
    }

    @Test
    void shouldSaveAndFindEarthquake() {
        EarthquakeEntity eq = new EarthquakeEntity();
        eq.setEarthquakeId("test-123");
        eq.setTitle("Test Earthquake");
        eq.setMagnitude(4.5);
        eq.setDepthKm(10.0);
        eq.setLatitude(39.0);
        eq.setLongitude(35.0);
        eq.setEventTime(Instant.now());

        earthquakeRepository.save(eq);

        assertTrue(earthquakeRepository.existsByEarthquakeId("test-123"));
        assertFalse(earthquakeRepository.existsByEarthquakeId("nonexistent"));
    }

    @Test
    void shouldFindRecentEarthquakes() {
        EarthquakeEntity eq = new EarthquakeEntity();
        eq.setEarthquakeId("recent-1");
        eq.setTitle("Recent Earthquake");
        eq.setMagnitude(5.0);
        eq.setDepthKm(15.0);
        eq.setLatitude(38.0);
        eq.setLongitude(34.0);
        eq.setEventTime(Instant.now());

        earthquakeRepository.save(eq);

        List<EarthquakeEntity> result = earthquakeRepository.findByEventTimeAfterOrderByEventTimeDesc(
                Instant.now().minusSeconds(3600));

        assertFalse(result.isEmpty());
    }

    @Test
    void shouldSaveAndFindActiveHelpRequests() {
        // Need a user first
        UserEntity user = new UserEntity();
        user.setTelegramChatId(77777L);
        user.setLatitude(39.0);
        user.setLongitude(35.0);
        user = userRepository.save(user);

        HelpRequestEntity hr = new HelpRequestEntity();
        hr.setUserId(user.getId());
        hr.setFullName("Test Person");
        hr.setPhoneNumber("5551234567");
        hr.setLatitude(39.0);
        hr.setLongitude(35.0);
        hr.setNeedTypes("TIBBI");
        hr.setKvkkConsent(true);
        hr.setStatus("ACTIVE");

        helpRequestRepository.save(hr);

        List<HelpRequestEntity> result = helpRequestRepository.findByStatus("ACTIVE");
        assertFalse(result.isEmpty());
    }
}
