package com.depremnobetcisi.domain.port.output;

public interface NotificationSender {
    boolean sendMessage(Long chatId, String message);
}
