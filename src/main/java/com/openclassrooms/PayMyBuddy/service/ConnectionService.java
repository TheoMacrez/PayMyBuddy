package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Ajouter une connexion entre deux utilisateurs.
     *
     * @param user1 le premier utilisateur
     * @param user2 le deuxième utilisateur
     */
    public void addConnection(UserModel user1, UserModel user2) {
        if (!user1.equals(user2) && !user1.getConnections().contains(user2)) {
            user1.getConnections().add(user2);
            user2.getConnections().add(user1);
            userRepository.saveAll(Arrays.asList(user1, user2)); // Sauvegarde en une seule opération
        }
    }

    /**
     * Obtenir la liste des amis d'un utilisateur.
     *
     * @param user l'utilisateur dont on veut récupérer les amis
     * @return la liste des amis de l'utilisateur
     */
    public List<UserModel> getFriends(UserModel user) {
        return user.getConnections();
    }

    /**
     * Retirer une connexion entre deux utilisateurs.
     *
     * @param user1 le premier utilisateur
     * @param user2 le deuxième utilisateur
     */
    public void removeConnection(UserModel user1, UserModel user2) {
        if (user1.getConnections().contains(user2)) {
            user1.getConnections().remove(user2);
            user2.getConnections().remove(user1);
            userRepository.saveAll(Arrays.asList(user1, user2)); // Sauvegarde en une seule opération
        }
    }

    /**
     * Trouver un utilisateur par son adresse email.
     *
     * @param email l'adresse email de l'utilisateur à rechercher
     * @return un objet Optional contenant l'utilisateur si trouvé, sinon vide
     */
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email); // Assurez-vous que cette méthode est définie dans UserRepository
    }
}
