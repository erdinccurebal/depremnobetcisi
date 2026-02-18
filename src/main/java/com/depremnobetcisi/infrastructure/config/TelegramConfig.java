package com.depremnobetcisi.infrastructure.config;

import com.depremnobetcisi.domain.port.input.HelpRequestUseCase;
import com.depremnobetcisi.domain.port.input.UserSubscriptionUseCase;
import com.depremnobetcisi.domain.port.output.NotificationSender;
import com.depremnobetcisi.infrastructure.input.telegram.ConversationManager;
import com.depremnobetcisi.infrastructure.input.telegram.DepremNobetcisiBot;
import com.depremnobetcisi.infrastructure.output.notification.TelegramNotificationSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Configuration
@ConditionalOnProperty(name = "app.telegram.enabled", havingValue = "true", matchIfMissing = false)
public class TelegramConfig {

    @Value("${app.telegram.bot-token}")
    private String botToken;

    @Bean
    public ConversationManager conversationManager(
            UserSubscriptionUseCase userSubscription,
            HelpRequestUseCase helpRequestUseCase) {
        return new ConversationManager(userSubscription, helpRequestUseCase);
    }

    @Bean
    public DepremNobetcisiBot depremNobetcisiBot(ConversationManager conversationManager) {
        return new DepremNobetcisiBot(botToken, conversationManager);
    }

    @Bean
    public NotificationSender notificationSender(DepremNobetcisiBot bot) {
        return new TelegramNotificationSender(bot.getTelegramClient());
    }

    @Bean(destroyMethod = "close")
    public TelegramBotsLongPollingApplication telegramBotsApplication(DepremNobetcisiBot bot) throws Exception {
        TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();
        application.registerBot(botToken, bot);
        return application;
    }
}
