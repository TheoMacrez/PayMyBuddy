package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
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
    private List<Transaction> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "receiver",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Transaction> receivedTransactions = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "connection",
            joinColumns = @JoinColumn(name = "user_id_1"),
            inverseJoinColumns = @JoinColumn(name = "user_id_2")
    )
    private List<User> connections = new ArrayList<>();

    // Méthodes pour ajouter/retirer des connexions
    public void addConnection(User user) {
        connections.add(user);
        user.getConnections().add(this);
    }

    public void removeConnection(User user) {
        connections.remove(user);
        user.getConnections().remove(this);
    }
}
