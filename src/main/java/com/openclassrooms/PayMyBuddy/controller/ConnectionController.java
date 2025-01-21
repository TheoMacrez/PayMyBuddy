package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import com.openclassrooms.PayMyBuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    /**
     * Afficher le formulaire pour ajouter une nouvelle connexion.
     *
     * @param model le modèle utilisé pour passer des attributs à la vue
     * @param request l'objet HttpServletRequest pour obtenir l'URI actuelle
     * @return le nom de la vue Thymeleaf à afficher
     */
    @GetMapping
    public String showAddConnectionForm(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "connections"; // Nom de la vue Thymeleaf
    }

    /**
     * Ajouter une nouvelle connexion entre l'utilisateur authentifié et un autre utilisateur.
     *
     * @param email l'email de l'utilisateur à connecter
     * @param userDetails les détails de l'utilisateur authentifié
     * @param redirectAttributes les attributs pour rediriger avec des messages flash
     * @return une redirection vers le formulaire de connexions
     */
    @PostMapping
    public String addConnection(@RequestParam String email,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        Optional<UserModel> principalUserOpt = connectionService.findByEmail(userDetails.getUsername());
        Optional<UserModel> userToConnectOpt = connectionService.findByEmail(email);

        if (userToConnectOpt.isPresent() && principalUserOpt.isPresent() && !Objects.equals(email, userDetails.getUsername())) {
            UserModel principalUser = principalUserOpt.get();
            UserModel userToConnect = userToConnectOpt.get();

            if (!principalUser.getConnections().contains(userToConnect)) {
                connectionService.addConnection(principalUser, userToConnect);
                redirectAttributes.addFlashAttribute("successMessage", "Connexion ajoutée avec succès !");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Vous êtes déjà connecté à cet utilisateur.");
            }
        } else {
            if (Objects.equals(email, userDetails.getUsername())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Vous ne pouvez pas vous ajouter vous-même");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Aucun utilisateur trouvé avec cet email.");
            }
        }

        return "redirect:/connections"; // Rediriger vers le formulaire avec un message
    }
}
