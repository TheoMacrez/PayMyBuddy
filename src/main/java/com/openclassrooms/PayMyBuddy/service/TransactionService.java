package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import com.openclassrooms.PayMyBuddy.model.TransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.PayMyBuddy.util.InsufficientFundsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository; // Pour vérifier les soldes

    /**
     * Récupérer une transaction par son identifiant.
     *
     * @param id l'identifiant de la transaction à rechercher
     * @return un objet Optional contenant la transaction si trouvée, sinon vide
     */
    public Optional<TransactionModel> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    /**
     * Supprimer une transaction par son identifiant.
     *
     * @param id l'identifiant de la transaction à supprimer
     */
    public void deleteTransactionById(int id) {
        transactionRepository.deleteById(id);
    }

    /**
     * Enregistrer une transaction après vérification des fonds.
     *
     * @param transaction l'objet TransactionModel à sauvegarder
     * @return la transaction enregistrée
     * @throws InsufficientFundsException si le solde de l'expéditeur est insuffisant
     */
    @Transactional
    public TransactionModel saveTransaction(TransactionModel transaction) {
        // Vérification du solde de l'expéditeur
        if (transaction.getSender().getBalance() < transaction.getAmount()) {
            throw new InsufficientFundsException("Fonds insuffisants !");
        }
        // Mettre à jour les soldes
        transaction.getSender().setBalance(transaction.getSender().getBalance() - transaction.getAmount());
        transaction.getReceiver().setBalance(transaction.getReceiver().getBalance() + transaction.getNetAmount());

        userRepository.save(transaction.getSender());
        userRepository.save(transaction.getReceiver());

        return transactionRepository.save(transaction);
    }

    /**
     * Récupérer toutes les transactions pour un utilisateur donné.
     *
     * @param user l'utilisateur dont on veut récupérer les transactions
     * @return la liste des transactions de l'utilisateur
     */
    public List<TransactionModel> getAllTransactionsForUser(UserModel user) {
        List<TransactionModel> transactions = new ArrayList<>();
        transactions.addAll(transactionRepository.findBySender(user));
        transactions.addAll(transactionRepository.findByReceiver(user));
        return transactions;
    }
}
