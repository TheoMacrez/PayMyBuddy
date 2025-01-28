package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ProfileControllerTest {

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

        userService.saveUser(existingUserOne);

        // Simulez le service pour retourner un utilisateur existant
        UserModel existingUserTwo = new UserModel();
        existingUserTwo.setEmail("existingemailtwo@example.com");
        existingUserTwo.setUsername("existingUserTwo");
        existingUserTwo.setPassword("password");

        userService.saveUser(existingUserTwo);

    }

    @AfterEach
    public void cleanAfter()
    {
        // Nettoyer la base de données après les tests
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "existingemailone@example.com")
    public void testProfilePage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("email"));
    }

    @Test
    @WithMockUser(username = "existingemailone@example.com")
    public void testProfileModification() throws Exception {
        // Simulez ici le comportement réussi du service pour modifier les informations d'utilisateur
        mockMvc.perform(post("/profile")
                        .param("emailToModify", "newemail@example.com")
                        .param("usernameToModify", "newusername")
                        .with(csrf())) // Ajoute le token CSRF
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }


    @Test
    @WithMockUser(username = "existingemailone@example.com")
    public void testProfileModificationWithExistingEmail() throws Exception {
        // Simulez ici le comportement du service pour retourner un utilisateur existant avec le même email
        mockMvc.perform(post("/profile")
                        .param("emailToModify", "existingemailtwo@example.com") // Email déjà utilisé
                        .param("usernameToModify", "newusername")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"))
                .andExpect(flash().attribute("errorMessage", "Cet email est déjà utilisé !"));
    }

}

