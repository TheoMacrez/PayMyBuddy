package com.openclassrooms.PayMyBuddy.repository;

import com.openclassrooms.PayMyBuddy.model.UserModel;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends CrudRepository<UserModel,Integer> {

    // Méthode personnalisée pour récupérer un utilisateur par email
    Optional<UserModel> findByEmail(String email);
}
