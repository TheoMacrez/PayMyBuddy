package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Charger un utilisateur par son email.
     *
     * @param email l'email de l'utilisateur à charger
     * @return un objet UserDetails représentant l'utilisateur
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
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

    /**
     * Récupérer un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à récupérer
     * @return un objet Optional contenant l'utilisateur si trouvé, sinon vide
     */
    public Optional<UserModel> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Récupérer un utilisateur par son email et mot de passe.
     *
     * @param email l'email de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @return un objet Optional contenant l'utilisateur si trouvé et le mot de passe est correct, sinon vide
     */
    public Optional<UserModel> getUserByEmailAndPassword(String email, String password) {
        // Récupérer l'utilisateur par email
        Optional<UserModel> user = userRepository.findByEmail(email);
        // Vérifier si l'utilisateur est présent et le mot de passe correspond
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user; // Retourner l'utilisateur si le mot de passe est correct
        }
        return Optional.empty(); // Retourner vide si l'utilisateur n'existe pas ou le mot de passe est incorrect
    }

    /**
     * Trouver un utilisateur par son email dans le repository.
     *
     * @param email l'email de l'utilisateur à rechercher
     * @return un objet Optional contenant l'utilisateur si trouvé, sinon vide
     */
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Supprimer un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à supprimer
     */
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    /**
     * Enregistrer un nouvel utilisateur après validation.
     *
     * @param user l'objet UserModel à sauvegarder
     * @return l'utilisateur enregistré
     * @throws RuntimeException si l'email est déjà utilisé
     */
    @Transactional
    public UserModel saveUser(UserModel user) {
        // Validation pour s'assurer que l'utilisateur n'existe pas déjà
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }
        // Hachage du mot de passe avant de le sauvegarder
        user.setPassword(hashPassword(user.getPassword()));
        user.setBalance(100.0);
        return userRepository.save(user);
    }

    /**
     * Modifier un utilisateur existant.
     *
     * @param user l'objet UserModel à modifier
     */
    @Transactional
    public void editUser(UserModel user) {
        userRepository.save(user);
    }

    /**
     * Hacher le mot de passe de l'utilisateur.
     *
     * @param password le mot de passe à hacher
     * @return le mot de passe haché
     */
    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }


}
