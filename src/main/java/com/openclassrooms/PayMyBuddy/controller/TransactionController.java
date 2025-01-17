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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
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

            // Trier les transactions de la plus récente à la plus ancienne
            transactions.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));

            List<TransactionFrontData> transactionFrontDataList  = new ArrayList<>();
            for (TransactionModel transactionModel : transactions)
            {
                TransactionFrontData newData = TransactionFrontData.getTransactionFrontData(transactionModel, user);
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
    public String createTransaction(@ModelAttribute TransactionModel transaction, @AuthenticationPrincipal UserDetails userDetails,  RedirectAttributes redirectAttributes) {

        Optional<UserModel> userOpt = userService.findByEmail(userDetails.getUsername());
        if(userOpt.isPresent())
        {
            UserModel user = userOpt.get();
            transaction.setSender(user);

//            // Récupérer le receiver à partir de l'ID
//            Optional<UserModel> receiverOpt = userService.findByEmail(transaction.getReceiver().getEmail()); // Assurez-vous que cette méthode existe
//            if (receiverOpt.isPresent()) {
//                transaction.setReceiver(receiverOpt.get());
//            } else {
//                redirectAttributes.addFlashAttribute("errorMessage", "Le destinataire sélectionné n'existe pas !");
//                return "redirect:/transactions";
//            }

            try {
                transactionService.saveTransaction(transaction);
                redirectAttributes.addFlashAttribute("successMessage", "Transfert réussi !");// Afficher une page d'erreur (à créer)
                // Rediriger vers la liste des transactions
            } catch (InsufficientFundsException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Vos fonds sont insuffisants pour cette transaction !");// Afficher une page d'erreur (à créer)
            }
        }

        return "redirect:/transactions";
    }
}
