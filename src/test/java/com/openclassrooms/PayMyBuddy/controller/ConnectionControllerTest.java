package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        // Créer des utilisateurs pour les tests
        UserModel existingUser = new UserModel();
        existingUser.setEmail("existingemail@example.com");
        existingUser.setUsername("existingUser");
        existingUser.setPassword("password");
        userService.saveUser(existingUser);

        UserModel userToConnect = new UserModel();
        userToConnect.setEmail("friend@example.com");
        userToConnect.setUsername("Friend");
        userToConnect.setPassword("password");
        userService.saveUser(userToConnect);
    }

    @AfterEach
    public void cleanAfter() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "existingemail@example.com")
    public void testShowAddConnectionForm() throws Exception {
        mockMvc.perform(get("/connections"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "existingemail@example.com")
    public void testAddConnection() throws Exception {
        mockMvc.perform(post("/connections")
                        .param("email", "friend@example.com")
                        .with(csrf())) // Ajoute le token CSRF
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connections"))
                .andExpect(flash().attribute("successMessage", "Connexion ajoutée avec succès !"));
    }

    @Test
    @WithMockUser(username = "existingemail@example.com")
    public void testAddConnectionWithSelf() throws Exception {
        mockMvc.perform(post("/connections")
                        .param("email", "existingemail@example.com") // Tentative d'ajouter soi-même
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connections"))
                .andExpect(flash().attribute("errorMessage", "Vous ne pouvez pas vous ajouter vous-même"));
    }

    @Test
    @WithMockUser(username = "existingemail@example.com")
    public void testAddConnectionWithNonExistentUser() throws Exception {
        mockMvc.perform(post("/connections")
                        .param("email", "nonexistent@example.com") // Utilisateur inexistant
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connections"))
                .andExpect(flash().attribute("errorMessage", "Aucun utilisateur trouvé avec cet email."));
    }
}
