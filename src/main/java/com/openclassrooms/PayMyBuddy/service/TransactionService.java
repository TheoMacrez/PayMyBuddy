package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import jakarta.transaction.*;

import com.openclassrooms.PayMyBuddy.model.TransactionModel;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.openclassrooms.PayMyBuddy.util.InsufficientFundsException;

import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository; // Pour vérifier les soldes

    public Optional<TransactionModel> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    public void deleteTransactionById(int id) {
        transactionRepository.deleteById(id);
    }

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

    public List<TransactionModel> getAllTransactionsForUser(UserModel user) {
        List<TransactionModel> transactions = new ArrayList<>();
        transactions.addAll(transactionRepository.findBySender(user));
        transactions.addAll(transactionRepository.findByReceiver(user));
        return transactions;
    }
}

