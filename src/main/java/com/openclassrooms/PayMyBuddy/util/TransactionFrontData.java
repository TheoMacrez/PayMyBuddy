package com.openclassrooms.PayMyBuddy.util;

import com.openclassrooms.PayMyBuddy.model.TransactionModel;
import com.openclassrooms.PayMyBuddy.model.UserModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionFrontData {


    private TransactionUserState state;

    private String receiverUsername;

    private String senderUsername;

    private String description;

    private String amountDraw;

    /**
     * Récupérer toutes les informations d'une transactionModel pour l'afficher dans la vue Thymeleaf.
     *
     * @param transactionModel la transaction dont on doit récupérer les informations
     * @param user le user qui a lancé cette transaction pour pouvoir définir si on doit afficher la transaction comme envoyée ou reçue.
     * @return un objet TransactionFrontData que va lire la vue Thymeleaf
     */
    public static TransactionFrontData getTransactionFrontData(TransactionModel transactionModel, UserModel user) {
        TransactionFrontData newData = new TransactionFrontData();
        newData.setDescription(transactionModel.getDescription());

        newData.setSenderUsername(transactionModel.getSender().getRawUsername());
        newData.setReceiverUsername(transactionModel.getReceiver().getRawUsername());
        if(transactionModel.getSender().equals(user))
        {
            newData.setState(TransactionUserState.SENDER);
            newData.setAmountDraw("-" + formatAmount(transactionModel.getAmount())+"€");
        }
        else {
            newData.setState(TransactionUserState.RECEIVER);
            newData.setAmountDraw("+" + formatAmount(transactionModel.getAmount())+"€");
        }
        return newData;
    }

    /**
     * Afficher le montant de la transaction dans un chiffre bien formaté.
     *
     * @param amount la montant de la transaction
     * @return un string que va lire la vue Thymeleaf
     */
    private static String formatAmount(Double amount) {
        DecimalFormat df = new DecimalFormat("#,##0.##"); // Format avec virgule pour les décimales
        return df.format(amount).replace(".", ","); // Remplace le point par une virgule
    }

}




