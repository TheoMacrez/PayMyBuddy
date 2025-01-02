package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    // Afficher le formulaire d'ajout de connexion
    @GetMapping
    public String showAddConnectionForm(Model model) {
        model.addAttribute("email", ""); // Pour le champ d'email
        return "addConnection"; // Nom de la vue Thymeleaf
    }

    // Ajouter une nouvelle connexion
    @PostMapping
    public String addConnection(@RequestParam String email, @AuthenticationPrincipal User user, Model model) {
        Optional<User> userToConnect = connectionService.findByEmail(email);

        if (userToConnect.isPresent()) {
            User targetUser = userToConnect.get();
            if (!user.getConnections().contains(targetUser)) {
                connectionService.addConnection(user, targetUser);
                model.addAttribute("message", "Connexion ajoutée avec succès !");
            } else {
                model.addAttribute("error", "Vous êtes déjà connecté à cet utilisateur.");
            }
        } else {
            model.addAttribute("error", "Aucun utilisateur trouvé avec cet email.");
        }

        return "addConnection"; // Rediriger vers le formulaire avec un message
    }
}
