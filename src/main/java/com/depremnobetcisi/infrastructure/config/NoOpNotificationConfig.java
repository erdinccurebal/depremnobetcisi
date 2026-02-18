package com.depremnobetcisi.infrastructure.config;

import com.depremnobetcisi.domain.port.output.NotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoOpNotificationConfig {

    private static final Logger log = LoggerFactory.getLogger(NoOpNotificationConfig.class);

    @Bean
    @ConditionalOnMissingBean(NotificationSender.class)
    public NotificationSender noOpNotificationSender() {
        return (chatId, message) -> {
            log.info("NoOp notification to chatId={}: {}", chatId, message.substring(0, Math.min(50, message.length())));
            return true;
        };
    }
}
