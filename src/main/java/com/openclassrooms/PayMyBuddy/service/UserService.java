package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<UserModel> userByLogin = userRepository.findByEmail(email);
        if (userByLogin.isEmpty()) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
        }
        return userByLogin.map(userModel -> User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .build()).orElse(null);

    }

    public Optional<UserModel> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<UserModel> getUserByEmailAndPassword(String email, String password) {
        // Récupérer l'utilisateur par email
        Optional<UserModel> user = userRepository.findByEmail(email);
        // Vérifier si l'utilisateur est présent et le mot de passe correspond
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user; // Retourner l'utilisateur si le mot de passe est correct
        }
        return Optional.empty(); // Retourner vide si l'utilisateur n'existe pas ou le mot de passe est incorrect
    }

    public Optional<UserModel> findByEmail(String email) {
        // Récupérer l'utilisateur par email
        return userRepository.findByEmail(email);
    }


    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public UserModel saveUser(UserModel user) {
        // Validation pour s'assurer que l'utilisateur n'existe pas déjà
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }
        // Hachage du mot de passe avant de le sauvegarder
        user.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public UserModel editUser(UserModel user) {
        return userRepository.save(user);
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }


//    public void displayAllUsers() {
//        List<User> users = (List<User>) userRepository.findAll();
//        System.out.println("Liste des utilisateurs :");
//        if (users.isEmpty()) {
//            System.out.println("Aucun utilisateur trouvé.");
//        } else {
//            for (User user : users) {
//                System.out.println("ID: " + user.getId() + ", Nom: " + user.getName() + ", Email: " + user.getEmail());
//            }
//        }
//    }

}

