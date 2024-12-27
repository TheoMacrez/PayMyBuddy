package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;


import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        // Récupérer l'utilisateur par email
        Optional<User> user = userRepository.findByEmail(email);
        // Vérifier si l'utilisateur est présent et le mot de passe correspond
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user; // Retourner l'utilisateur si le mot de passe est correct
        }
        return Optional.empty(); // Retourner vide si l'utilisateur n'existe pas ou le mot de passe est incorrect
    }


    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        // Validation pour s'assurer que l'utilisateur n'existe pas déjà
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }
        // Hachage du mot de passe avant de le sauvegarder
        user.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(user);
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

}

