package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testGetUserByEmailAndPassword_Success() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setPassword("$2a$10$EIX/4lF5y7P8yZ5QfG2i0O5n1F5Zg3YwYgXy5E1p5Y5u5Y5u5Y5u5"); // Mot de passe haché

        String rawPassword = "password"; // Mot de passe en clair

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(true);

        Optional<User> result = userService.getUserByEmailAndPassword("test@example.com", rawPassword);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testGetUserByEmailAndPassword_UserNotFound() {
        String rawPassword = "password";

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmailAndPassword("test@example.com", rawPassword);

        assertThat(result).isNotPresent();
    }

    @Test
    public void testGetUserByEmailAndPassword_IncorrectPassword() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setPassword("$2a$10$EIX/4lF5y7P8yZ5QfG2i0O5n1F5Zg3YwYgXy5E1p5Y5u5Y5u5Y5u5"); // Mot de passe haché

        String rawPassword = "wrongPassword";

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(false);

        Optional<User> result = userService.getUserByEmailAndPassword("test@example.com", rawPassword);

        assertThat(result).isNotPresent();
    }

    @Test
    public void testDeleteUserById() {
        doNothing().when(userRepository).deleteById(1);

        userService.deleteUserById(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertThat(savedUser).isEqualTo(user);
        verify(userRepository, times(1)).save(user);
    }
}
