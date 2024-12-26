package repository;

import model.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {



    // Méthode personnalisée pour récupérer un utilisateur par email
    Optional<User> findByEmail(String email);
}
