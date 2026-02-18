package com.depremnobetcisi.infrastructure.output.notification;

import com.depremnobetcisi.domain.port.output.NotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramNotificationSender implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(TelegramNotificationSender.class);

    private final TelegramClient telegramClient;

    public TelegramNotificationSender(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public boolean sendMessage(Long chatId, String message) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(message)
                    .parseMode("Markdown")
                    .build();
            telegramClient.execute(sendMessage);
            return true;
        } catch (TelegramApiException e) {
            log.error("Failed to send notification to chatId {}: {}", chatId, e.getMessage(), e);
            return false;
        }
    }
}
