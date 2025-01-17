package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.TransactionModel;
import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import com.openclassrooms.PayMyBuddy.service.UserService;
import com.openclassrooms.PayMyBuddy.util.InsufficientFundsException;
import com.openclassrooms.PayMyBuddy.util.TransactionFrontData;
import com.openclassrooms.PayMyBuddy.util.TransactionUserState;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionService connectionService;

    // Afficher toutes les transactions pour l'utilisateur connecté
    @GetMapping
    public String getTransactionsForUser(@AuthenticationPrincipal UserDetails userDetails, Model model, HttpServletRequest request) {

        Optional<UserModel> userOpt = userService.findByEmail(userDetails.getUsername());

        if(userOpt.isPresent())
        {
            UserModel user = userOpt.get();
            List<TransactionModel> transactions = transactionService.getAllTransactionsForUser(user);
            List<TransactionFrontData> transactionFrontDataList  = new ArrayList<>();
            for (TransactionModel transactionModel : transactions)
            {
                TransactionFrontData newData = new TransactionFrontData();
                newData.setDescription(transactionModel.getDescription());
                newData.setAmount(transactionModel.getAmount());
                newData.setSender(transactionModel.getSender());
                newData.setReceiver(transactionModel.getReceiver());
                if(transactionModel.getSender().equals(user))
                {
                    newData.setState(TransactionUserState.SENDER);
                }
                else {
                    newData.setState(TransactionUserState.RECEIVER);
                }
                transactionFrontDataList.add(newData);
            }
            model.addAttribute("transactions", transactionFrontDataList);

            List<UserModel> connections = connectionService.getFriends(user);
            model.addAttribute("connections", connections);
        }
        model.addAttribute("currentUri",request.getRequestURI());
        return "transactions"; // Nom de la vue Thymeleaf
    }

    // Créer une nouvelle transaction
    @PostMapping
    public String createTransaction(@ModelAttribute TransactionModel transaction, @AuthenticationPrincipal UserModel user) {
        transaction.setSender(user); // L'utilisateur connecté est l'expéditeur
        try {
            transactionService.saveTransaction(transaction);
            return "redirect:/transactions"; // Rediriger vers la liste des transactions
        } catch (InsufficientFundsException e) {
            return "error"; // Afficher une page d'erreur (à créer)
        }
    }
}
