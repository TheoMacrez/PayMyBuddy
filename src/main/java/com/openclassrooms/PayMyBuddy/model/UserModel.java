package com.openclassrooms.PayMyBuddy.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.*;
import java.util.*;

@Data
@Entity
@Table(name = "user")
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name="balance")
    private double balance = 0.0;

    @Column(name = "create_time",
            nullable = false,
            updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "sender",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TransactionModel> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "receiver",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TransactionModel> receivedTransactions = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "connection",
            joinColumns = @JoinColumn(name = "user_id_1"),
            inverseJoinColumns = @JoinColumn(name = "user_id_2")
    )
    private List<UserModel> connections = new ArrayList<>();


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserModel user = (UserModel) obj;
        return id != 0 && id == user.id; // Comparer par ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retourne les rôles de l'utilisateur ici
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return email; // Utiliser l'email comme nom d'utilisateur
    }


    /**
     * Pouvour avoir accès à l'username de l'utilisateur et non son adresse email.
     *
     * @return le nom de l'utilisateur à afficher
     */
    public String getRawUsername()
    {
        return username;
    }


}
