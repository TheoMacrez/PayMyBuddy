package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    // Page de connexion
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "logout", required = false) String logout, Model model) {
        if (logout != null) {
            model.addAttribute("logoutMessage", "Déconnexion réussie.");
        }
        return "login"; // Nom du fichier login.html dans le dossier templates
    }

    // Page d'inscription
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // Nom du fichier signup.html
    }

    // Endpoint pour inscrire un nouvel utilisateur
    @PostMapping("/signup")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userService.saveUser(user);
        return "redirect:/users/login?success"; // Redirection vers la page de connexion après inscription
    }


}

