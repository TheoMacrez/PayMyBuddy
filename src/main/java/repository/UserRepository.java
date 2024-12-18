package repository;

import model.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    // Méthode personnalisée pour récupérer un utilisateur par email et mot de passe
    Optional<User> findByEmailAndPassword(String email, String password);
}
