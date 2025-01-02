package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;


import java.util.*;

@Service
public class ConnectionService {

    @Autowired
    private UserRepository userRepository;

    public void addConnection(User user1, User user2) {
        if (!user1.equals(user2) && !user1.getConnections().contains(user2)) {
            user1.getConnections().add(user2);
            user2.getConnections().add(user1);
            userRepository.saveAll(Arrays.asList(user1, user2)); // Sauvegarde en une seule opération
        }
    }

    public List<User> getFriends(User user) {
        return user.getConnections();
    }

    public void removeConnection(User user1, User user2) {
        if (user1.getConnections().contains(user2)) {
            user1.getConnections().remove(user2);
            user2.getConnections().remove(user1);
            userRepository.saveAll(Arrays.asList(user1, user2)); // Sauvegarde en une seule opération
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email); // Assurez-vous que cette méthode est définie dans UserRepository
    }
}

