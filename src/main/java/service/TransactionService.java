package service;

import model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import repository.*;

import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Optional<Transaction> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    // Supprimer un utilisateur par ID
    public void deleteTransactionById(int id) {
        transactionRepository.deleteById(id);
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
