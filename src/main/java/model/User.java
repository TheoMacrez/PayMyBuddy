package model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Connection;
import java.time.*;
import java.util.*;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Transaction> getSentTransactions() {
        return sentTransactions;
    }

    public void setSentTransactions(List<Transaction> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setReceivedTransactions(List<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }
    public List<User> getConnections() {
        return connections;
    }

    public void setConnections(List<User> connections) {
        this.connections = connections;
    }
}
