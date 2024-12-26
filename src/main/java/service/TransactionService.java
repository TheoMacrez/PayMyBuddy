package service;

import jakarta.transaction.*;
import model.*;
import model.Transaction;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import repository.*;
import util.InsufficientFundsException;

import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository; // Pour vérifier les soldes

    public Optional<Transaction> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    public void deleteTransactionById(int id) {
        transactionRepository.deleteById(id);
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
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

    public List<Transaction> getAllTransactionsForUser(User user) {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(transactionRepository.findBySender(user));
        transactions.addAll(transactionRepository.findByReceiver(user));
        return transactions;
    }
}

