package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        Optional<UserModel> userModel = userService.findByEmail(userDetails.getUsername());
        if(userModel.isPresent())
        {
            model.addAttribute("username", userModel.get().getUsername());
            model.addAttribute("email", userModel.get().getEmail());
            model.addAttribute("password", userModel.get().getPassword());
        }
        // Pour des raisons de sécurité, évitez d'afficher le mot de passe
        return "profile"; // Nom du fichier profile.html dans le dossier templates
    }
}
