package com.spacegoat.task.service;

import com.spacegoat.task.domain.Transaction;
import com.spacegoat.task.dto.TransactionResponseDto;
import com.spacegoat.task.dto.UserRequestDto;
import com.spacegoat.task.dto.UserResponseDto;

import java.util.List;

public interface UserAccountService {
    void transfer(long senderId, long receiverId, double amount);

    double getBalance(long userId);

    void addFunds(long userId, double amount);

    List<TransactionResponseDto> getAllTransactionsForUserId(long id);

    UserResponseDto createUser(UserRequestDto dto);
}
