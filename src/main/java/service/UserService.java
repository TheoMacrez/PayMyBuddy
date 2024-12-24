package service;

import config.SpringSecurityConfig;
import model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.*;
import repository.*;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        // Ajouter une vérification de sécurité ici
        return userRepository.findByEmailAndPassword(email, password);
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
        // Implémentation du hachage (par exemple, avec BCrypt)
        return password; // Remplace par le vrai hachage
    }
}

