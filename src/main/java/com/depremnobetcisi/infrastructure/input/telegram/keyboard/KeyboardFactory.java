package com.depremnobetcisi.infrastructure.input.telegram.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class KeyboardFactory {

    public static ReplyKeyboardMarkup mainMenuKeyboard(boolean hasActiveHelpRequest) {
        KeyboardRow row1 = new KeyboardRow(List.of(
                KeyboardButton.builder().text("👤 Bilgilerimi Göster").build()
        ));
        KeyboardRow row2 = new KeyboardRow(List.of(
                KeyboardButton.builder().text("📋 Bilgilerimi Güncelle").build()
        ));
        String helpButtonText = hasActiveHelpRequest ? "🗑 Yardım İsteğini Sil" : "🆘 Yardım İste";
        KeyboardRow row3 = new KeyboardRow(List.of(
                KeyboardButton.builder().text(helpButtonText).build()
        ));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2, row3))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public static InlineKeyboardMarkup kvkkConsentKeyboard() {
        InlineKeyboardButton acceptButton = InlineKeyboardButton.builder()
                .text("✅ Onaylıyorum")
                .callbackData("kvkk_accept")
                .build();
        InlineKeyboardButton rejectButton = InlineKeyboardButton.builder()
                .text("❌ Onaylamıyorum")
                .callbackData("kvkk_reject")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(acceptButton),
                        new InlineKeyboardRow(rejectButton)
                ))
                .build();
    }

    public static InlineKeyboardMarkup helpConfirmKeyboard() {
        InlineKeyboardButton confirmButton = InlineKeyboardButton.builder()
                .text("✅ Onayla ve Yayınla")
                .callbackData("help_confirm")
                .build();
        InlineKeyboardButton cancelButton = InlineKeyboardButton.builder()
                .text("❌ İptal")
                .callbackData("help_cancel")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(confirmButton),
                        new InlineKeyboardRow(cancelButton)
                ))
                .build();
    }

    public static InlineKeyboardMarkup deleteConfirmKeyboard() {
        InlineKeyboardButton confirmButton = InlineKeyboardButton.builder()
                .text("🗑 Evet, Sil")
                .callbackData("delete_confirm")
                .build();
        InlineKeyboardButton cancelButton = InlineKeyboardButton.builder()
                .text("❌ Vazgeç")
                .callbackData("delete_cancel")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(confirmButton),
                        new InlineKeyboardRow(cancelButton)
                ))
                .build();
    }

    public static InlineKeyboardMarkup helpDeleteConfirmKeyboard() {
        InlineKeyboardButton confirmButton = InlineKeyboardButton.builder()
                .text("🗑 Evet, Sil")
                .callbackData("help_delete_confirm")
                .build();
        InlineKeyboardButton cancelButton = InlineKeyboardButton.builder()
                .text("❌ Vazgeç")
                .callbackData("help_delete_cancel")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(confirmButton),
                        new InlineKeyboardRow(cancelButton)
                ))
                .build();
    }

    public static InlineKeyboardMarkup cancelKeyboard() {
        InlineKeyboardButton cancelButton = InlineKeyboardButton.builder()
                .text("❌ İptal")
                .callbackData("cancel_operation")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(new InlineKeyboardRow(cancelButton)))
                .build();
    }

    public static InlineKeyboardMarkup forgetMeKeyboard() {
        InlineKeyboardButton forgetButton = InlineKeyboardButton.builder()
                .text("🗑 Beni Unut")
                .callbackData("forget_me")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(new InlineKeyboardRow(forgetButton)))
                .build();
    }

    public static InlineKeyboardMarkup skipKeyboard(String callbackData) {
        InlineKeyboardButton skipButton = InlineKeyboardButton.builder()
                .text("⏭ Atla")
                .callbackData(callbackData)
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(new InlineKeyboardRow(skipButton)))
                .build();
    }

    public static ReplyKeyboardMarkup locationRequestKeyboard() {
        KeyboardButton locationButton = KeyboardButton.builder()
                .text("📍 Konumumu Gönder")
                .requestLocation(true)
                .build();

        KeyboardRow row = new KeyboardRow(List.of(locationButton));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }
}
