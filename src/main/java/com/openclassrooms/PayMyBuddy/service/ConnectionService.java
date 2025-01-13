package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;


import java.util.*;

@Service
public class ConnectionService {

    @Autowired
    private UserRepository userRepository;

    public void addConnection(UserModel user1, UserModel user2) {
        if (!user1.equals(user2) && !user1.getConnections().contains(user2)) {
            user1.getConnections().add(user2);
            user2.getConnections().add(user1);
            userRepository.saveAll(Arrays.asList(user1, user2)); // Sauvegarde en une seule opération
        }
    }

    public List<UserModel> getFriends(UserModel user) {
        return user.getConnections();
    }

    public void removeConnection(UserModel user1, UserModel user2) {
        if (user1.getConnections().contains(user2)) {
            user1.getConnections().remove(user2);
            user2.getConnections().remove(user1);
            userRepository.saveAll(Arrays.asList(user1, user2)); // Sauvegarde en une seule opération
        }
    }

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email); // Assurez-vous que cette méthode est définie dans UserRepository
    }
}

