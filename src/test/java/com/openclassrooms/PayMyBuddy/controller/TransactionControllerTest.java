package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
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
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
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
    public void cleanAfter() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "existingemailone@example.com")
    public void testGetTransactions() throws Exception {
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attributeExists("connections"));
    }

    @Test
    @WithMockUser(username = "existingemailone@example.com")
    public void testCreateTransaction() throws Exception {

        UserModel receiver = userService.findByEmail("existingemailtwo@example.com").orElseThrow();

        // Définir les valeurs pour le modèle
        String amount = "75"; // Montant de la transaction
        String description = "Test transaction"; // Description de la transaction

        // Effectuer la requête POST avec les paramètres requis
        mockMvc.perform(post("/transactions")
                        .param("amount", amount) // Montant
                        .param("description", description) // Description
                        .param("receiver", String.valueOf(receiver.getId())) // Email du destinataire
                        .with(csrf())) // Ajoute le token CSRF
                .andExpect(status().is3xxRedirection()) // Vérifie la redirection
                .andExpect(redirectedUrl("/transactions")) // Vérifie l'URL de redirection
                .andExpect(flash().attribute("successMessage", "Transfert réussi !")); // Vérifie le message de succès
    }

    @Test
    @WithMockUser(username = "existingemailone@example.com")
    public void testCreateTransactionWithInsufficientFunds() throws Exception {

        UserModel receiver = userService.findByEmail("existingemailtwo@example.com").orElseThrow();
        // Simuler le comportement pour des fonds insuffisants
        String amount = "1500"; // Montant de la transaction
        String description = "Test transaction Unsufficient"; // Description de la transaction

        // Effectuer la requête POST avec les paramètres requis
        mockMvc.perform(post("/transactions")
                        .param("amount", amount) // Montant
                        .param("description", description) // Description
                        .param("receiver", String.valueOf(receiver.getId())) // Email du destinataire
                        .with(csrf())) // Ajoute le token CSRF
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions"))
                .andExpect(flash().attribute("errorMessage", "Vos fonds sont insuffisants pour cette transaction !"));
    }
}
