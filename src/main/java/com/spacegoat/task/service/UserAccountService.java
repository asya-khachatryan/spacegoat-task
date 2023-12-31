package com.spacegoat.task.service;

import com.spacegoat.task.domain.Transaction;
import com.spacegoat.task.dto.TransactionResponseDto;

import java.util.List;

public interface UserAccountService {
    void transfer(long senderId, long receiverId, double amount);

    double getBalance(long userId);

    void addFunds(long userId, double amount);

    List<Transaction> getAllTransactionsForUserId(long id);
}
