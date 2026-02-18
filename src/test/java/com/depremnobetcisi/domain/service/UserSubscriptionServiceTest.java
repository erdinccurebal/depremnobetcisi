package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.ConversationState;
import com.depremnobetcisi.domain.model.Location;
import com.depremnobetcisi.domain.model.User;
import com.depremnobetcisi.domain.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSubscriptionServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserSubscriptionService service;

    @BeforeEach
    void setUp() {
        service = new UserSubscriptionService(userRepository);
    }

    @Test
    void shouldSubscribeNewUser() {
        Location location = new Location(39.0, 35.0);
        when(userRepository.findByTelegramChatId(123L)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User user = service.subscribeUser(123L, "testuser", location);

        assertNotNull(user);
        assertEquals(123L, user.getTelegramChatId());
        assertEquals(39.0, user.getLatitude());
        assertEquals(35.0, user.getLongitude());
        assertTrue(user.isActive());
        verify(userRepository).save(any());
    }

    @Test
    void shouldUpdateExistingUser() {
        User existing = new User();
        existing.setId(1L);
        existing.setTelegramChatId(123L);
        when(userRepository.findByTelegramChatId(123L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Location newLocation = new Location(40.0, 36.0);
        User user = service.subscribeUser(123L, "testuser", newLocation);

        assertEquals(1L, user.getId());
        assertEquals(40.0, user.getLatitude());
        verify(userRepository).save(any());
    }

    @Test
    void shouldUpdateConversationState() {
        User existing = new User();
        existing.setTelegramChatId(123L);
        when(userRepository.findByTelegramChatId(123L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User user = service.updateConversationState(123L, ConversationState.UPDATE_AWAITING_NAME);

        assertEquals(ConversationState.UPDATE_AWAITING_NAME, user.getConversationState());
    }
}
