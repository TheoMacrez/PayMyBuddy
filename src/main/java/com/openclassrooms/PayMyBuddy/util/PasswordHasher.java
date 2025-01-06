package com.openclassrooms.PayMyBuddy.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String[] rawPasswords = {"password123", "password456", "password789", "password321", "password654"};

        for (String rawPassword : rawPasswords) {
            String hashedPassword = passwordEncoder.encode(rawPassword);
            System.out.println(hashedPassword); // Affiche le mot de passe haché
        }
    }
}

