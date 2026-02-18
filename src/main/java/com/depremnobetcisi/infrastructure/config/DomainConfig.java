package com.depremnobetcisi.infrastructure.config;

import com.depremnobetcisi.domain.port.input.EarthquakeMonitoringUseCase;
import com.depremnobetcisi.domain.port.input.HelpRequestUseCase;
import com.depremnobetcisi.domain.port.input.NotificationDispatchUseCase;
import com.depremnobetcisi.domain.port.input.UserSubscriptionUseCase;
import com.depremnobetcisi.domain.port.output.*;
import com.depremnobetcisi.domain.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public GeoCalculationService geoCalculationService() {
        return new GeoCalculationService();
    }

    @Bean
    public NotificationDispatchUseCase notificationDispatchUseCase(
            UserRepository userRepository,
            NotificationLogRepository notificationLogRepository,
            NotificationSender notificationSender,
            GeoCalculationService geoCalculationService) {
        return new NotificationDispatchService(userRepository, notificationLogRepository, notificationSender, geoCalculationService);
    }

    @Bean
    public EarthquakeMonitoringUseCase earthquakeMonitoringService(
            EarthquakeApiClient apiClient,
            EarthquakeRepository earthquakeRepository,
            NotificationDispatchUseCase notificationDispatch) {
        return new EarthquakeMonitoringService(apiClient, earthquakeRepository, notificationDispatch);
    }

    @Bean
    public UserSubscriptionUseCase userSubscriptionService(UserRepository userRepository) {
        return new UserSubscriptionService(userRepository);
    }

    @Bean
    public HelpRequestUseCase helpRequestUseCase(
            HelpRequestRepository helpRequestRepository,
            GeoCalculationService geoCalculationService) {
        return new HelpRequestService(helpRequestRepository, geoCalculationService);
    }
}
