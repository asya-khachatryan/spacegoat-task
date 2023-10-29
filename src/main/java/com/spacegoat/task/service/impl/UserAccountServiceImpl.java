package com.spacegoat.task.service.impl;

import com.spacegoat.task.dao.UserDao;
import com.spacegoat.task.domain.Transaction;
import com.spacegoat.task.dto.TransactionResponseDto;
import com.spacegoat.task.mapper.TransactionMapper;
import com.spacegoat.task.service.UserAccountService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserDao userDao;

    public UserAccountServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public void transfer(long senderId, long receiverId, double amount) {
        userDao.transfer(senderId, receiverId, amount);
    }

    public double getBalance(long userId) {
        return userDao.getBalance(userId);
    }

    public void addFunds(long userId, double amount) {
        userDao.addFunds(userId, amount);
    }

    @Override
    public List<Transaction> getAllTransactionsForUserId(long id) {
        List<Transaction> allTransactions = userDao.getAllTransactionsForUserId(id);
//        List<TransactionResponseDto> dtoList = new ArrayList<>();
//        for (Transaction transaction :
//                allTransactions) {
//            TransactionResponseDto dto = TransactionMapper.INSTANCE.transactionToResponseDto(transaction);
//            dtoList.add(dto);
//        }
        return allTransactions;
    }
}
