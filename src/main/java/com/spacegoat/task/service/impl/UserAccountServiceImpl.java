package com.spacegoat.task.service.impl;

import com.spacegoat.task.converter.TransactionConverter;
import com.spacegoat.task.converter.UserConverter;
import com.spacegoat.task.domain.Transaction;
import com.spacegoat.task.domain.User;
import com.spacegoat.task.dto.TransactionResponseDto;
import com.spacegoat.task.dto.UserRequestDto;
import com.spacegoat.task.dto.UserResponseDto;
import com.spacegoat.task.exception.InsufficientFundsException;
import com.spacegoat.task.exception.ResourceNotFoundException;
import com.spacegoat.task.repository.TransactionRepository;
import com.spacegoat.task.repository.UserRepository;
import com.spacegoat.task.service.UserAccountService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Primary
@Transactional
public class UserAccountServiceImpl implements UserAccountService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionConverter transactionConverter;
    private final UserConverter userConverter;

    public UserAccountServiceImpl(UserRepository userRepository,
                                  TransactionRepository transactionRepository,
                                  TransactionConverter transactionConverter,
                                  UserConverter userConverter) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionConverter = transactionConverter;
        this.userConverter = userConverter;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transfer(long senderId, long receiverId, double amount) {
        User sender = userRepository.findById(senderId).
                orElseThrow(() -> new ResourceNotFoundException("User", "Id", senderId));
        if (sender.getBalance() < amount) {
            throw new InsufficientFundsException("Not enough funds");
        }
        User receiver = userRepository.findById(receiverId).
                orElseThrow(() -> new ResourceNotFoundException("User", "Id", receiverId));
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        Transaction transaction = new Transaction();
        transaction.setSenderUserId(senderId);
        transaction.setReceiverUserId(receiverId);
        transaction.setAmount(amount);
        transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
        transactionRepository.save(transaction);
    }

    @Override
    public double getBalance(long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return user.getBalance();
    }

    @Override
    public void addFunds(long userId, double amount) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    @Override
    public List<TransactionResponseDto> getAllTransactionsForUserId(long id) {
        List<Transaction> transactionList = transactionRepository.findBySenderUserId(id);
        return transactionConverter.bulkConvertEntityToResponseDto(transactionList);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        User user = userConverter.convertRequestDtoToEntity(dto);
        User savedUser = userRepository.save(user);
        return userConverter.convertEntityToResponseDto(savedUser);
    }
}
