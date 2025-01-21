package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.TransactionModel;
import com.openclassrooms.PayMyBuddy.model.UserModel;
import com.openclassrooms.PayMyBuddy.service.ConnectionService;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import com.openclassrooms.PayMyBuddy.service.UserService;
import com.openclassrooms.PayMyBuddy.util.InsufficientFundsException;
import com.openclassrooms.PayMyBuddy.util.TransactionFrontData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     * Afficher toutes les transactions pour l'utilisateur connecté.
     *
     * @param userDetails les détails de l'utilisateur authentifié
     * @param model le modèle utilisé pour passer des attributs à la vue
     * @param request l'objet HttpServletRequest pour obtenir l'URI actuelle
     * @return le nom de la vue Thymeleaf à afficher
     */
    @GetMapping
    public String getTransactionsForUser(@AuthenticationPrincipal UserDetails userDetails,
                                         Model model,
                                         HttpServletRequest request) {
        Optional<UserModel> userOpt = userService.findByEmail(userDetails.getUsername());

        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            List<TransactionModel> transactions = transactionService.getAllTransactionsForUser(user);

            // Trier les transactions de la plus récente à la plus ancienne
            transactions.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));

            List<TransactionFrontData> transactionFrontDataList = new ArrayList<>();
            for (TransactionModel transactionModel : transactions) {
                TransactionFrontData newData = TransactionFrontData.getTransactionFrontData(transactionModel, user);
                transactionFrontDataList.add(newData);
            }
            model.addAttribute("transactions", transactionFrontDataList);

            List<UserModel> connections = connectionService.getFriends(user);
            model.addAttribute("connections", connections);
        }
        model.addAttribute("currentUri", request.getRequestURI());
        return "transactions"; // Nom de la vue Thymeleaf
    }

    /**
     * Créer une nouvelle transaction.
     *
     * @param transaction l'objet TransactionModel contenant les détails de la transaction
     * @param userDetails les détails de l'utilisateur authentifié
     * @param redirectAttributes les attributs pour rediriger avec des messages flash
     * @return une redirection vers la liste des transactions
     */
    @PostMapping
    public String createTransaction(@ModelAttribute TransactionModel transaction,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    RedirectAttributes redirectAttributes) {
        Optional<UserModel> userOpt = userService.findByEmail(userDetails.getUsername());
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            transaction.setSender(user);

            try {
                transactionService.saveTransaction(transaction);
                redirectAttributes.addFlashAttribute("successMessage", "Transfert réussi !");
            } catch (InsufficientFundsException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Vos fonds sont insuffisants pour cette transaction !");
            }
        }

        return "redirect:/transactions"; // Rediriger vers la liste des transactions
    }
}
