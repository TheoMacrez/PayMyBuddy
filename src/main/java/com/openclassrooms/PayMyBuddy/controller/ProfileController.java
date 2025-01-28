package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Afficher la page de profil de l'utilisateur authentifié.
     *
     * @param model le modèle utilisé pour passer des attributs à la vue
     * @param userDetails les détails de l'utilisateur authentifié
     * @param request l'objet HttpServletRequest pour obtenir l'URI actuelle
     * @return le nom de la vue Thymeleaf à afficher
     */
    @GetMapping("/profile")
    public String profilePage(Model model,
                              @AuthenticationPrincipal UserDetails userDetails,
                              HttpServletRequest request) {
        Optional<UserModel> userModel = userService.findByEmail(userDetails.getUsername());
        if (userModel.isPresent()) {
            model.addAttribute("username", userModel.get().getRawUsername());
            model.addAttribute("email", userModel.get().getEmail());
        }
        model.addAttribute("currentUri", request.getRequestURI());

        return "profile"; // Nom du fichier profile.html dans le dossier templates
    }

    /**
     * Modifier les informations de profil de l'utilisateur authentifié.
     *
     * @param emailToModify l'email à modifier
     * @param usernameToModify le nom d'utilisateur à modifier
     * @param userDetails les détails de l'utilisateur authentifié
     * @param redirectAttributes les attributs pour rediriger avec des messages flash
     * @return une redirection vers la page de profil
     */
    @PostMapping("/profile")
    public String profileModification(@RequestParam String emailToModify,
                                      @RequestParam String usernameToModify,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      RedirectAttributes redirectAttributes) {
        Optional<UserModel> userModelOptional = userService.findByEmail(userDetails.getUsername());
        if (userModelOptional.isPresent()) {
            UserModel userModel = userModelOptional.get();

            if (emailToModify.equals(userModel.getEmail()) && usernameToModify.equals(userModel.getRawUsername())) {
                return "redirect:/profile";
            }

            // Vérification si l'email est déjà utilisé par un autre utilisateur
            if (!emailToModify.equals(userModel.getEmail()) && userService.findByEmail(emailToModify).isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cet email est déjà utilisé !");
                return "redirect:/profile"; // Renvoyer à la page de profil avec l'erreur
            }

            userModel.setEmail(emailToModify);
            userModel.setUsername(usernameToModify);
            userService.editUser(userModel);

            redirectAttributes.addFlashAttribute("successMessage", "Modification réussie !");

            // Mise à jour de l'authentification
            UserDetails updatedUserDetails = User.builder()
                    .username(userModel.getUsername())
                    .password(userModel.getPassword()) // Gardez le même mot de passe
                    .build();

            // Mettre à jour le contexte de sécurité
            Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUserDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return "redirect:/profile";
    }
}
