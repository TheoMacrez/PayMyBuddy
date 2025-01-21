package com.openclassrooms.PayMyBuddy.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.springframework.security.web.authentication.*;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * Configurer le filtre de sécurité pour gérer les autorisations des requêtes HTTP.
     *
     * @param http l'objet HttpSecurity utilisé pour construire la chaîne de filtres de sécurité
     * @return un objet SecurityFilterChain configuré
     * @throws Exception si une erreur se produit lors de la configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/users/signup", "/users/login", "/login-test.html", "/styles/*").permitAll() // Autoriser l'accès aux pages de connexion et d'inscription
                        .anyRequest().authenticated() // Toute autre requête nécessite une authentification
                )
                .formLogin(form -> form
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/transactions", true) // Page de connexion personnalisée
                        .permitAll() // Permet à tous d'accéder à la page de connexion
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/users/login?logout=true") // Redirection après déconnexion
                );

        return http.build();
    }

    /**
     * Créer un gestionnaire d'authentification pour gérer l'authentification des utilisateurs.
     *
     * @param userService le service utilisateur utilisé pour récupérer les détails des utilisateurs
     * @param http l'objet HttpSecurity pour accéder à l'AuthenticationManagerBuilder
     * @param passwordEncoder le mot de passe encodeur utilisé pour le hachage des mots de passe
     * @return un objet AuthenticationManager configuré
     * @throws Exception si une erreur se produit lors de la configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(UserService userService, HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    /**
     * Fournir un encodeur de mot de passe utilisant BCrypt pour le hachage des mots de passe.
     *
     * @return un objet PasswordEncoder configuré pour utiliser BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utilisation de BCrypt pour le hachage des mots de passe
    }
}
