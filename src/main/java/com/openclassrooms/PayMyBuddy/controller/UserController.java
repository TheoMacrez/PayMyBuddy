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

        if (user.isPresent()) {
            // Authentification réussie
            // Ici, tu peux stocker l'utilisateur dans la session si nécessaire
            // Par exemple : session.setAttribute("user", user.get());
            return "redirect:/home"; // Redirection vers la page d'accueil
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

//    // Endpoint pour créer un utilisateur
//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        User createdUser = userService.saveUser(user);
//        return ResponseEntity.ok(createdUser);
//    }
//
//    // Endpoint pour récupérer un utilisateur par ID
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable int id) {
//        Optional<User> user = userService.getUserById(id);
//        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Endpoint pour récupérer un utilisateur par email et mot de passe
//    @PostMapping("/login")
//    public ResponseEntity<User> getUserByEmailAndPassword(@RequestParam String email, @RequestParam String password) {
//        Optional<User> user = userService.getUserByEmailAndPassword(email, password);
//        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Endpoint pour inscrire un nouvel utilisateur
//    @PostMapping("/signup")
//    public ResponseEntity<User> registerUser(@RequestBody User user) {
//        // Ici tu peux ajouter de la logique pour vérifier si l'email est déjà pris avant de créer l'utilisateur
//        User createdUser = userService.saveUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//    }
//
//
//    // Endpoint pour supprimer un utilisateur par ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
//        userService.deleteUserById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // Endpoint pour récupérer le profil d'un utilisateur
//    @GetMapping("/profil/{id}")
//    public ResponseEntity<User> getUserProfile(@PathVariable int id) {
//        Optional<User> user = userService.getUserById(id);
//        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

}

