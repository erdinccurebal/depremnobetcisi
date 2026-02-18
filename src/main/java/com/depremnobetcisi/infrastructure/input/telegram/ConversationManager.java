package com.depremnobetcisi.infrastructure.input.telegram;

import com.depremnobetcisi.domain.model.ConversationState;
import com.depremnobetcisi.domain.model.HelpRequest;
import com.depremnobetcisi.domain.model.Location;
import com.depremnobetcisi.domain.model.User;
import com.depremnobetcisi.domain.port.input.HelpRequestUseCase;
import com.depremnobetcisi.domain.port.input.UserSubscriptionUseCase;
import com.depremnobetcisi.infrastructure.input.telegram.keyboard.KeyboardFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConversationManager {

    private static final Logger log = LoggerFactory.getLogger(ConversationManager.class);

    private final UserSubscriptionUseCase userSubscription;
    private final HelpRequestUseCase helpRequestUseCase;
    private final Map<Long, Integer> wrongInputCount = new ConcurrentHashMap<>();

    public ConversationManager(UserSubscriptionUseCase userSubscription,
                                HelpRequestUseCase helpRequestUseCase) {
        this.userSubscription = userSubscription;
        this.helpRequestUseCase = helpRequestUseCase;
    }

    public List<SendMessage> processUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            return handleCallback(update.getCallbackQuery());
        }
        if (update.hasMessage()) {
            return handleMessage(update.getMessage());
        }
        return null;
    }

    private List<SendMessage> handleMessage(Message message) {
        Long chatId = message.getChatId();
        String username = message.getFrom() != null ? message.getFrom().getUserName() : null;

        // /iptal
        if (message.hasText() && "/iptal".equals(message.getText().trim())) {
            userSubscription.updateConversationState(chatId, ConversationState.IDLE);
            return List.of(reply(chatId, "❌ İşlem iptal edildi.", KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId))));
        }

        // /beniunut
        if (message.hasText() && "/beniunut".equals(message.getText().trim())) {
            return List.of(handleDeleteAccount(chatId));
        }

        // "Bilgilerimi Güncelle" button
        if (message.hasText() && message.getText().contains("Bilgilerimi Güncelle")) {
            userSubscription.getOrCreateUser(chatId, username);
            userSubscription.updateConversationState(chatId, ConversationState.UPDATE_AWAITING_LOCATION);
            return List.of(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("📋 *Bilgi Güncelleme*\n\n📍 Önce konumunuzu gönderin:")
                    .parseMode("Markdown")
                    .replyMarkup(KeyboardFactory.locationRequestKeyboard())
                    .build());
        }

        // "Bilgilerimi Göster" button
        if (message.hasText() && message.getText().contains("Bilgilerimi Göster")) {
            return List.of(handleShowProfile(chatId, username));
        }

        // "Yardım İsteğini Sil" button
        if (message.hasText() && message.getText().contains("Yardım İsteğini Sil")) {
            return List.of(handleDeleteHelpRequest(chatId));
        }

        // "Yardım İste" button
        if (message.hasText() && message.getText().contains("Yardım İste")) {
            return List.of(handleHelpRequest(chatId, username));
        }

        // Location message
        if (message.hasLocation()) {
            return handleLocation(message);
        }

        // Text in conversation flow
        User user = userSubscription.findByChatId(chatId).orElse(null);
        if (user != null && user.getConversationState() != ConversationState.IDLE) {
            return handleConversationText(chatId, message.getText(), user);
        }

        // Default: show welcome + profile warning
        return List.of(buildWelcomeMessage(chatId, username));
    }

    private SendMessage buildWelcomeMessage(Long chatId, String username) {
        User user = userSubscription.getOrCreateUser(chatId, username);

        StringBuilder sb = new StringBuilder();
        sb.append("🏠 *Deprem Nöbetçisi'ne Hoş Geldiniz!*\n");
        sb.append("_Deprem anında bildirim alın, tek tuşla yardım talebi oluşturun._\n\n");
        sb.append("📋 *Bilgilerimi Güncelle* – Ad, telefon, konum ve KVKK onayınızı kaydedin\n");
        sb.append("🆘 *Yardım İste* – Kayıtlı bilgilerinizle yardım talebi oluşturun");

        if (user.hasCompleteProfile()) {
            sb.append("\n\n👤 *Kayıtlı Bilgileriniz:*\n");
            sb.append(String.format("Konum: %.4f, %.4f\n", user.getLatitude(), user.getLongitude()));
            if (user.getFullName() != null) sb.append("Ad: ").append(user.getFullName()).append("\n");
            if (user.getPhoneNumber() != null) sb.append("Telefon: ").append(user.getPhoneNumber()).append("\n");
            if (user.getAddressText() != null) sb.append("Adres: ").append(user.getAddressText()).append("\n");
            sb.append("KVKK: ✅ Onaylı");
            sb.append("\n\n_Bilgilerinizi silmemizi istiyorsanız aşağıdaki butona basınız._");
        } else {
            sb.append("\n\n⚠️ _Bilgileriniz henüz tamamlanmamış. Yardım talebi oluşturabilmek için lütfen önce_ \"📋 Bilgilerimi Güncelle\" _butonunu kullanın._");
        }

        if (user.hasCompleteProfile()) {
            return SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(sb.toString())
                    .parseMode("Markdown")
                    .replyMarkup(KeyboardFactory.forgetMeKeyboard())
                    .build();
        }
        return reply(chatId, sb.toString(), KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId)));
    }

    private SendMessage handleShowProfile(Long chatId, String username) {
        User user = userSubscription.getOrCreateUser(chatId, username);

        if (!user.hasCompleteProfile()) {
            return reply(chatId, """
                    ⚠️ Henüz kayıtlı bilginiz bulunmuyor.

                    Lütfen "📋 Bilgilerimi Güncelle" butonunu kullanarak bilgilerinizi kaydedin.""",
                    KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId)));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("👤 *Kayıtlı Bilgileriniz:*\n\n");
        sb.append(String.format("📍 Konum: %.4f, %.4f\n", user.getLatitude(), user.getLongitude()));
        if (user.getFullName() != null) sb.append("👤 Ad: ").append(user.getFullName()).append("\n");
        if (user.getPhoneNumber() != null) sb.append("📞 Telefon: ").append(user.getPhoneNumber()).append("\n");
        if (user.getAddressText() != null) sb.append("🏠 Adres: ").append(user.getAddressText()).append("\n");
        sb.append("📋 KVKK: ✅ Onaylı");
        sb.append("\n\n_Bilgilerinizi silmemizi istiyorsanız aşağıdaki butona basınız._");

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(sb.toString())
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.forgetMeKeyboard())
                .build();
    }

    private SendMessage handleHelpRequest(Long chatId, String username) {
        User user = userSubscription.getOrCreateUser(chatId, username);

        if (!user.hasCompleteProfile()) {
            return reply(chatId, """
                    ⚠️ Yardım talebi oluşturabilmek için önce bilgilerinizi kaydetmeniz gerekiyor.

                    Lütfen "📋 Bilgilerimi Güncelle" butonunu kullanarak konum ve KVKK onayınızı tamamlayın.""",
                    KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId)));
        }

        userSubscription.updateConversationState(chatId, ConversationState.HELP_AWAITING_CONFIRMATION);

        StringBuilder summary = new StringBuilder();
        summary.append("🆘 *Yardım Talebi*\n\n");
        summary.append("Aşağıdaki bilgileriniz herkese açık haritada paylaşılacaktır:\n\n");
        summary.append(String.format("📍 Konum: %.4f, %.4f\n", user.getLatitude(), user.getLongitude()));
        if (user.getFullName() != null) summary.append("👤 Ad: ").append(user.getFullName()).append("\n");
        if (user.getPhoneNumber() != null) summary.append("📞 Telefon: ").append(user.getPhoneNumber()).append("\n");
        if (user.getAddressText() != null) summary.append("🏠 Adres: ").append(user.getAddressText()).append("\n");
        summary.append("\nBu bilgilerin paylaşılmasını onaylıyor musunuz?");

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(summary.toString())
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.helpConfirmKeyboard())
                .build();
    }

    private List<SendMessage> handleLocation(Message message) {
        Long chatId = message.getChatId();
        double lat = message.getLocation().getLatitude();
        double lon = message.getLocation().getLongitude();

        User user = userSubscription.findByChatId(chatId).orElse(null);
        if (user != null && user.getConversationState() == ConversationState.UPDATE_AWAITING_LOCATION) {
            user.setLocation(new Location(lat, lon));
            userSubscription.subscribeUser(chatId, user.getTelegramUsername(), new Location(lat, lon));
            userSubscription.updateConversationState(chatId, ConversationState.UPDATE_AWAITING_NAME);

            SendMessage confirmMsg = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(String.format("✅ Konum kaydedildi: %.4f, %.4f", lat, lon))
                    .replyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build())
                    .build();
            SendMessage nameMsg = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("📋 *Bilgi Güncelleme*\n\n👤 Ad ve soyadınızı girin:")
                    .parseMode("Markdown")
                    .replyMarkup(KeyboardFactory.skipKeyboard("skip_name"))
                    .build();
            return List.of(confirmMsg, nameMsg);
        }

        // Default: just save location as subscription
        String username = message.getFrom() != null ? message.getFrom().getUserName() : null;
        userSubscription.subscribeUser(chatId, username, new Location(lat, lon));
        return List.of(reply(chatId, String.format("✅ Konum kaydedildi: %.4f, %.4f", lat, lon), KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId))));
    }

    private List<SendMessage> handleConversationText(Long chatId, String text, User user) {
        ConversationState state = user.getConversationState();

        return switch (state) {
            case UPDATE_AWAITING_NAME -> {
                if (text == null || text.trim().length() < 3 || text.trim().length() > 60
                        || !text.trim().matches("[a-zA-ZçğıöşüÇĞİÖŞÜ ]+")) {
                    yield List.of(reply(chatId, "⚠️ Ad Soyad yalnızca harflerden oluşmalı, en az 3, en fazla 60 karakter olabilir."),
                            SendMessage.builder()
                                    .chatId(chatId.toString())
                                    .text("📋 *Bilgi Güncelleme*\n\n👤 Ad ve soyadınızı tekrar girin:")
                                    .parseMode("Markdown")
                                    .replyMarkup(KeyboardFactory.skipKeyboard("skip_name"))
                                    .build());
                }
                user.setFullName(text.trim());
                user.setConversationState(ConversationState.UPDATE_AWAITING_PHONE);
                saveUserProfile(user);
                userSubscription.updateConversationState(chatId, ConversationState.UPDATE_AWAITING_PHONE);

                SendMessage confirmMsg = reply(chatId, "✅ Ad Soyad: " + text.trim());
                SendMessage phoneMsg = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("📋 *Bilgi Güncelleme*\n\n📞 Telefon numaranızı girin:")
                        .parseMode("Markdown")
                        .replyMarkup(KeyboardFactory.skipKeyboard("skip_phone"))
                        .build();
                yield List.of(confirmMsg, phoneMsg);
            }
            case UPDATE_AWAITING_PHONE -> {
                String phone = text.trim().replaceAll("[\\s\\-()]", "");
                if (!phone.matches("^(\\+?90|0)?[5][0-9]{9}$")) {
                    yield List.of(reply(chatId, "⚠️ Geçerli bir telefon numarası girin. Örnek: 05XX XXX XX XX"),
                            SendMessage.builder()
                                    .chatId(chatId.toString())
                                    .text("📋 *Bilgi Güncelleme*\n\n📞 Telefon numaranızı tekrar girin:")
                                    .parseMode("Markdown")
                                    .replyMarkup(KeyboardFactory.skipKeyboard("skip_phone"))
                                    .build());
                }
                user.setPhoneNumber(phone);
                user.setConversationState(ConversationState.UPDATE_AWAITING_ADDRESS);
                saveUserProfile(user);
                userSubscription.updateConversationState(chatId, ConversationState.UPDATE_AWAITING_ADDRESS);

                SendMessage confirmMsg = reply(chatId, "✅ Telefon: " + text);
                SendMessage addressMsg = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("📋 *Bilgi Güncelleme*\n\n🏠 Yazılı adresinizi girin:")
                        .parseMode("Markdown")
                        .replyMarkup(KeyboardFactory.skipKeyboard("skip_address"))
                        .build();
                yield List.of(confirmMsg, addressMsg);
            }
            case UPDATE_AWAITING_ADDRESS -> {
                if (text == null || text.trim().length() < 10 || text.trim().length() > 500) {
                    yield List.of(reply(chatId, "⚠️ Adres en az 10, en fazla 500 karakter olmalıdır."),
                            SendMessage.builder()
                                    .chatId(chatId.toString())
                                    .text("📋 *Bilgi Güncelleme*\n\n🏠 Yazılı adresinizi tekrar girin:")
                                    .parseMode("Markdown")
                                    .replyMarkup(KeyboardFactory.skipKeyboard("skip_address"))
                                    .build());
                }
                user.setAddressText(text.trim());
                user.setConversationState(ConversationState.UPDATE_AWAITING_KVKK);
                saveUserProfile(user);
                userSubscription.updateConversationState(chatId, ConversationState.UPDATE_AWAITING_KVKK);

                SendMessage confirmMsg = reply(chatId, "✅ Adres: " + text);
                SendMessage kvkkMsg = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("""
                                📋 *Bilgi Güncelleme*

                                ⚠️ *KVKK Aydınlatma Metni:*
                                Yardım talebinde bulunduğunuzda bilgileriniz herkese açık harita üzerinde görüntülenecektir. Yardım sağlandıktan sonra bilgileriniz sistemden silinecektir.

                                Onaylıyor musunuz?""")
                        .parseMode("Markdown")
                        .replyMarkup(KeyboardFactory.kvkkConsentKeyboard())
                        .build();
                yield List.of(confirmMsg, kvkkMsg);
            }
            default -> {
                int count = wrongInputCount.merge(chatId, 1, Integer::sum);
                if (count <= 2) {
                    userSubscription.updateConversationState(chatId, ConversationState.UPDATE_AWAITING_LOCATION);
                    yield List.of(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("📋 *Bilgi Güncelleme*\n\n📍 Önce konumunuzu gönderin:")
                            .parseMode("Markdown")
                            .replyMarkup(KeyboardFactory.locationRequestKeyboard())
                            .build());
                } else {
                    yield List.of(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Defalarca beklenmeyen girdi yaptınız. İşlemi iptal etmek için butona basınız.")
                            .replyMarkup(KeyboardFactory.cancelKeyboard())
                            .build());
                }
            }
        };
    }

    private List<SendMessage> handleCallback(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();

        if ("cancel_operation".equals(data)) {
            wrongInputCount.remove(chatId);
            userSubscription.updateConversationState(chatId, ConversationState.IDLE);
            return List.of(reply(chatId, "❌ İşlem iptal edildi.", KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId))));
        }

        if ("forget_me".equals(data)) {
            return List.of(handleDeleteAccount(chatId));
        }

        if (data.startsWith("skip_")) {
            return handleSkip(chatId, data);
        }

        if ("kvkk_accept".equals(data)) {
            User user = userSubscription.findByChatId(chatId).orElse(null);
            if (user != null) {
                user.setKvkkConsent(true);
                user.setConversationState(ConversationState.IDLE);
                saveUserProfile(user);
            }
            userSubscription.updateConversationState(chatId, ConversationState.IDLE);
            return List.of(reply(chatId, """
                    ✅ *Bilgileriniz başarıyla kaydedildi!*

                    Artık "🆘 Yardım İste" butonuyla yardım talebinde bulunabilirsiniz.
                    Yakınlarınızda deprem olduğunda size bildirim göndereceğiz.""",
                    KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId))));
        }

        if ("kvkk_reject".equals(data)) {
            userSubscription.updateConversationState(chatId, ConversationState.IDLE);
            return List.of(reply(chatId, "❌ KVKK onayı reddedildi. Bilgi güncelleme iptal edildi.", KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId))));
        }

        if ("help_delete_confirm".equals(data)) {
            User user = userSubscription.findByChatId(chatId).orElse(null);
            if (user != null) {
                helpRequestUseCase.deleteActiveByUserId(user.getId());
            }
            return List.of(reply(chatId, "✅ Yardım talebiniz silindi.", KeyboardFactory.mainMenuKeyboard(false)));
        }

        if ("help_delete_cancel".equals(data)) {
            return List.of(reply(chatId, "👍 Yardım talebiniz korunmaya devam ediyor.", KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId))));
        }

        if ("help_confirm".equals(data)) {
            User user = userSubscription.findByChatId(chatId).orElse(null);
            if (user != null && user.hasCompleteProfile()) {
                HelpRequest hr = new HelpRequest();
                hr.setUserId(user.getId());
                hr.setFullName(user.getFullName());
                hr.setPhoneNumber(user.getPhoneNumber());
                hr.setLatitude(user.getLatitude());
                hr.setLongitude(user.getLongitude());
                hr.setAddressText(user.getAddressText());
                hr.setNeedTypes("GENEL");
                hr.setKvkkConsent(true);
                helpRequestUseCase.createHelpRequest(hr);
            }
            userSubscription.updateConversationState(chatId, ConversationState.IDLE);
            return List.of(reply(chatId, "✅ *Yardım talebiniz oluşturuldu!*\n\nTalebiniz harita üzerinde yayınlandı. En kısa sürede yardım ulaşmasını diliyoruz.",
                    KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId))));
        }

        if ("help_cancel".equals(data)) {
            userSubscription.updateConversationState(chatId, ConversationState.IDLE);
            return List.of(reply(chatId, "❌ Yardım talebi iptal edildi.", KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId))));
        }

        if ("delete_confirm".equals(data)) {
            User user = userSubscription.findByChatId(chatId).orElse(null);
            if (user != null) {
                helpRequestUseCase.deleteActiveByUserId(user.getId());
            }
            userSubscription.deleteUser(chatId);
            return List.of(reply(chatId, "✅ Tüm kayıtlarınız ve yardım talepleriniz silindi.\n\nTekrar kayıt olmak isterseniz herhangi bir mesaj gönderin.", KeyboardFactory.mainMenuKeyboard(false)));
        }

        if ("delete_cancel".equals(data)) {
            return List.of(reply(chatId, "👍 Silme işlemi iptal edildi. Kayıtlarınız korunmaya devam ediyor.", KeyboardFactory.mainMenuKeyboard(hasActiveHelpRequest(chatId))));
        }

        return List.of(reply(chatId, "Bilinmeyen işlem."));
    }

    private List<SendMessage> handleSkip(Long chatId, String skipData) {
        return switch (skipData) {
            case "skip_name" -> {
                userSubscription.updateConversationState(chatId, ConversationState.UPDATE_AWAITING_PHONE);
                SendMessage skipMsg = reply(chatId, "⏭ Ad atlandı.");
                SendMessage phoneMsg = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("📋 *Bilgi Güncelleme*\n\n📞 Telefon numaranızı girin:")
                        .parseMode("Markdown")
                        .replyMarkup(KeyboardFactory.skipKeyboard("skip_phone"))
                        .build();
                yield List.of(skipMsg, phoneMsg);
            }
            case "skip_phone" -> {
                userSubscription.updateConversationState(chatId, ConversationState.UPDATE_AWAITING_ADDRESS);
                SendMessage skipMsg = reply(chatId, "⏭ Telefon atlandı.");
                SendMessage addressMsg = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("📋 *Bilgi Güncelleme*\n\n🏠 Yazılı adresinizi girin:")
                        .parseMode("Markdown")
                        .replyMarkup(KeyboardFactory.skipKeyboard("skip_address"))
                        .build();
                yield List.of(skipMsg, addressMsg);
            }
            case "skip_address" -> {
                userSubscription.updateConversationState(chatId, ConversationState.UPDATE_AWAITING_KVKK);
                SendMessage skipMsg = reply(chatId, "⏭ Adres atlandı.");
                SendMessage kvkkMsg = SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("""
                                📋 *Bilgi Güncelleme*

                                ⚠️ *KVKK Aydınlatma Metni:*
                                Yardım talebinde bulunduğunuzda bilgileriniz herkese açık harita üzerinde görüntülenecektir. Yardım sağlandıktan sonra bilgileriniz sistemden silinecektir.

                                Onaylıyor musunuz?""")
                        .parseMode("Markdown")
                        .replyMarkup(KeyboardFactory.kvkkConsentKeyboard())
                        .build();
                yield List.of(skipMsg, kvkkMsg);
            }
            default -> List.of(reply(chatId, "Bilinmeyen işlem."));
        };
    }

    private SendMessage handleDeleteHelpRequest(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("⚠️ *Dikkat!*\n\nAktif yardım talebiniz haritadan kaldırılacaktır.\n\nDevam etmek istiyor musunuz?")
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.helpDeleteConfirmKeyboard())
                .build();
    }

    private SendMessage handleDeleteAccount(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("⚠️ *Dikkat!*\n\nTüm kayıtlarınız (ad, telefon, konum, KVKK onayı) veritabanından kalıcı olarak silinecektir.\n\n📢 Mevcut yardım talebiniz varsa haritadan kaldırılacaktır.\n\n🚨 Silme işleminden sonra deprem olduğunda sizi uyaramayacağız. Bildirim alabilmek için tekrar kayıt olmanız gerekecektir.\n\nDevam etmek istiyor musunuz?")
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.deleteConfirmKeyboard())
                .build();
    }

    private void saveUserProfile(User user) {
        User dbUser = userSubscription.findByChatId(user.getTelegramChatId()).orElse(user);
        if (user.getFullName() != null) dbUser.setFullName(user.getFullName());
        if (user.getPhoneNumber() != null) dbUser.setPhoneNumber(user.getPhoneNumber());
        if (user.getAddressText() != null) dbUser.setAddressText(user.getAddressText());
        dbUser.setKvkkConsent(user.isKvkkConsent());
        dbUser.setConversationState(user.getConversationState());
        userSubscription.saveUser(dbUser);
    }

    private boolean hasActiveHelpRequest(Long chatId) {
        return userSubscription.findByChatId(chatId)
                .map(u -> helpRequestUseCase.hasActiveRequest(u.getId()))
                .orElse(false);
    }

    private SendMessage reply(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("Markdown")
                .build();
    }

    private SendMessage reply(Long chatId, String text, Object replyMarkup) {
        var builder = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .parseMode("Markdown");
        if (replyMarkup instanceof org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup kb) {
            builder.replyMarkup(kb);
        } else if (replyMarkup instanceof org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup ik) {
            builder.replyMarkup(ik);
        }
        return builder.build();
    }
}
