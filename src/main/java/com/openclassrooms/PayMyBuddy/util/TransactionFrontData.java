package com.openclassrooms.PayMyBuddy.util;

import com.openclassrooms.PayMyBuddy.model.TransactionModel;
import com.openclassrooms.PayMyBuddy.model.UserModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionFrontData {


    private TransactionUserState state;

    private UserModel receiver;

    private UserModel sender;

    private String description;

    private double amount;

}




