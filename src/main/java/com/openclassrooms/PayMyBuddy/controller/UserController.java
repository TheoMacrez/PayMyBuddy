package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.PayMyBuddy.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Page de connexion
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Nom du fichier login.html dans le dossier templates
    }

    // Page d'inscription
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // Nom du fichier signup.html
    }

    // Endpoint pour connecter un utilisateur
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<User> user = userService.getUserByEmailAndPassword(email, password);
        System.out.println("Tentative de connexion avec Email: " + email + " et Mot de passe: " + password);

        if (user.isPresent()) {
            // Authentification réussie
            // Ici, tu peux stocker l'utilisateur dans la session si nécessaire
            // Par exemple : session.setAttribute("user", user.get());
            return "redirect:/users/profile"; // Redirection vers la page d'accueil
        } else {
            // Authentification échouée
            model.addAttribute("errorMessage", "Email ou mot de passe incorrect.");
            return "login"; // Retour à la page de connexion avec un message d'erreur
        }
    }

    // Endpoint pour inscrire un nouvel utilisateur
    @PostMapping("/signup")
    public String registerUser(User user) {
        userService.saveUser(user);
        return "redirect:/users/login"; // Redirection vers la page de connexion après inscription
    }

    @GetMapping("/profile")
    public String profilePage(Model model, @SessionAttribute("user") User user) {
        model.addAttribute("username", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password", user.getPassword()); // Pour des raisons de sécurité, évitez d'afficher le mot de passe
        return "profile"; // Nom du fichier profile.html dans le dossier templates
    }

}

