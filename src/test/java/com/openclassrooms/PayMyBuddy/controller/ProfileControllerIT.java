package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ProfileControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {

        // Nettoyer la base de données avant chaque test
        userRepository.deleteAll();

        // Simulez le service pour retourner un utilisateur existant
        UserModel existingUserOne = new UserModel();
        existingUserOne.setEmail("existingemailone@example.com");
        existingUserOne.setUsername("existingUserOne");
        existingUserOne.setPassword("password");

        userRepository.save(existingUserOne);

        // Simulez le service pour retourner un utilisateur existant
        UserModel existingUserTwo = new UserModel();
        existingUserTwo.setEmail("existingemailtwo@example.com");
        existingUserTwo.setUsername("existingUserTwo");
        existingUserTwo.setPassword("password");

        userRepository.save(existingUserTwo);


    }

    @Test
    @WithMockUser(username = "existingemail@example.com")
    public void testProfilePage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("email"));
    }

    @Test
    @WithMockUser(username = "existingemail@example.com")
    public void testProfileModification() throws Exception {
        mockMvc.perform(post("/profile")
                        .param("emailToModify", "newemail@example.com")
                        .param("usernameToModify", "newusername")
                        .with(csrf())) // Ajoute le token CSRF
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    @WithMockUser(username = "existingemail@example.com")
    public void testProfileModificationWithExistingEmail() throws Exception {
        // Simulez ici le comportement du service pour retourner un utilisateur existant avec le même email
        mockMvc.perform(post("/profile")
                        .param("emailToModify", "existingemail@example.com") // Email déjà utilisé
                        .param("usernameToModify", "newusername")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(flash().attribute("errorMessage", "Cet email est déjà utilisé !"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class); // Remplacez le bean par un mock explicite
        }
    }
}

