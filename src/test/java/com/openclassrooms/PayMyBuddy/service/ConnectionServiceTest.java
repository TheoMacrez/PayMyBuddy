package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ConnectionServiceTest {

    @InjectMocks
    private ConnectionService connectionService;

    @Mock
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User();
        user1.setId(1);
        user1.setConnections(new ArrayList<>());

        user2 = new User();
        user2.setId(2);
        user2.setConnections(new ArrayList<>());
    }

    @Test
    public void testAddConnection() {
        connectionService.addConnection(user1, user2);

        assertThat(user1.getConnections()).contains(user2);
        assertThat(user2.getConnections()).contains(user1);
        verify(userRepository, times(1)).saveAll(Arrays.asList(user1, user2));
    }

    @Test
    public void testAddConnection_SameUser() {
        connectionService.addConnection(user1, user1);

        assertThat(user1.getConnections()).isEmpty();
        verify(userRepository, never()).saveAll(any());
    }

    @Test
    public void testAddConnection_AlreadyConnected() {
        user1.getConnections().add(user2);// Ajout manuel pour simuler une connexion existante
        connectionService.addConnection(user1, user2); // N'est pas censé ajouter les connextion

        assertThat(user1.getConnections()).contains(user2);
        assertThat(user2.getConnections()).doesNotContain(user1);
        verify(userRepository, never()).saveAll(any());
    }

    @Test
    public void testGetFriends() {
        user1.getConnections().add(user2);

        List<User> friends = connectionService.getFriends(user1);

        assertThat(friends).contains(user2);
    }

    @Test
    public void testRemoveConnection() {
        user1.getConnections().add(user2); // Ajout manuel pour simuler une connexion existante
        user2.getConnections().add(user1);

        connectionService.removeConnection(user1, user2);

        assertThat(user1.getConnections()).doesNotContain(user2);
        assertThat(user2.getConnections()).doesNotContain(user1);
        verify(userRepository, times(1)).saveAll(Arrays.asList(user1, user2));
    }

    @Test
    public void testRemoveConnection_NotConnected() {
        connectionService.removeConnection(user1, user2);

        assertThat(user1.getConnections()).isEmpty();
        assertThat(user2.getConnections()).isEmpty();
        verify(userRepository, never()).saveAll(any());
    }
}

