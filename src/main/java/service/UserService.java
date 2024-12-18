package service;

import model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import repository.*;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // Récupérer un utilisateur par email et mot de passe
    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    // Supprimer un utilisateur par ID
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
