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

    // Méthode pour formater le montant
    private static String formatAmount(Double amount) {
        DecimalFormat df = new DecimalFormat("#,##0.##"); // Format avec virgule pour les décimales
        return df.format(amount).replace(".", ","); // Remplace le point par une virgule
    }

}




