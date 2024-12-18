package service;

import model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import repository.*;

import java.util.*;

@Service
public class ConnectionService {
    @Autowired
    private UserRepository userRepository;

    // Ajouter une connexion entre deux utilisateurs
    public void addConnection(User user1, User user2) {
        if (!user1.getConnections().contains(user2)) {
            user1.getConnections().add(user2);
            user2.getConnections().add(user1);
            userRepository.save(user1);
            userRepository.save(user2);
        }
    }

    // Récupérer les amis d'un utilisateur
    public List<User> getFriends(User user) {
        return user.getConnections();
    }

    // Supprimer une connexion entre deux utilisateurs
    public void removeConnection(User user1, User user2) {
        if (user1.getConnections().contains(user2)) {
            user1.getConnections().remove(user2);
            user2.getConnections().remove(user1);
            userRepository.save(user1);
            userRepository.save(user2);
        }
    }
}
