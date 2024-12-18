package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int userId;

    @Column(name="name")
    private String userName;

    @Column(name="email")
    private String userEmail;

    @Column(name="password")
    private String userPassword;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> receivedTransactions;

    // Relation many-to-many avec l'entité User (table d'association connection)
    @ManyToMany
            (
                    fetch = FetchType.LAZY,
                    cascade = {
                            CascadeType.PERSIST,
                            CascadeType.MERGE
                    }
            )
    @JoinTable(
            name = "connection",  // Nom de la table d'association
            joinColumns = @JoinColumn(name = "user_id_1"),  // Colonne de jointure dans connection pour l'utilisateur 1
            inverseJoinColumns = @JoinColumn(name = "user_id_2") // Colonne de jointure dans connection pour l'utilisateur 2
    )
    private List<User> connections;  // Liste des utilisateurs connectés à cet utilisateur


}
