package com.depremnobetcisi.infrastructure.input.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

public class DepremNobetcisiBot implements LongPollingSingleThreadUpdateConsumer {

    private static final Logger log = LoggerFactory.getLogger(DepremNobetcisiBot.class);

    private final TelegramClient telegramClient;
    private final ConversationManager conversationManager;

    public DepremNobetcisiBot(String botToken, ConversationManager conversationManager) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.conversationManager = conversationManager;
    }

    public TelegramClient getTelegramClient() {
        return telegramClient;
    }

    @Override
    public void consume(Update update) {
        try {
            List<SendMessage> responses = conversationManager.processUpdate(update);
            if (responses != null) {
                for (SendMessage response : responses) {
                    telegramClient.execute(response);
                }
            }
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram message: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error processing update: {}", e.getMessage(), e);
        }
    }
}
