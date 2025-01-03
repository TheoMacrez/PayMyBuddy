package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import com.openclassrooms.PayMyBuddy.util.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Afficher toutes les transactions pour l'utilisateur connecté
    @GetMapping
    public String getTransactionsForUser(@AuthenticationPrincipal User user, Model model) {
        List<Transaction> transactions = transactionService.getAllTransactionsForUser(user);
        model.addAttribute("transactions", transactions);
        return "transactions"; // Nom de la vue Thymeleaf
    }

    // Créer une nouvelle transaction
    @PostMapping
    public String createTransaction(@ModelAttribute Transaction transaction, @AuthenticationPrincipal User user) {
        transaction.setSender(user); // L'utilisateur connecté est l'expéditeur
        try {
            transactionService.saveTransaction(transaction);
            return "redirect:/transactions"; // Rediriger vers la liste des transactions
        } catch (InsufficientFundsException e) {
            return "error"; // Afficher une page d'erreur (à créer)
        }
    }
}
