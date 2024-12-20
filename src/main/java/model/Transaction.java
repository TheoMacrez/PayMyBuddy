package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    @Column(nullable = false)
    private double amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // Méthode pour calculer le montant après prélèvement
    public double getNetAmount() {
        return amount * 0.995; // 0.5% de commission
    }
}
