package am.spacegoat.task.service.impl;

import am.spacegoat.task.domain.Transaction;
import am.spacegoat.task.domain.User;
import am.spacegoat.task.dto.TransactionDto;
import am.spacegoat.task.dto.UserDto;
import am.spacegoat.task.exception.InsufficientFundsException;
import am.spacegoat.task.exception.ResourceNotFoundException;
import am.spacegoat.task.repository.TransactionRepository;
import am.spacegoat.task.repository.UserRepository;
import am.spacegoat.task.service.UserAccountService;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class UserAccountServiceImpl implements UserAccountService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public UserAccountServiceImpl(UserRepository userRepository,
                                  TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transfer(long senderId, long receiverId, BigDecimal amount) {
        User sender = userRepository.findById(senderId).
                orElseThrow(() -> new ResourceNotFoundException(User.class, "Id", String.valueOf(senderId)));
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough funds");
        }
        User receiver = userRepository.findById(receiverId).
                orElseThrow(() -> new ResourceNotFoundException(User.class, "Id", String.valueOf(receiverId)));

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setSenderUser(sender);
        transaction.setReceiverUser(receiver);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public BigDecimal getBalance(long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(User.class, "Id", String.valueOf(userId)));
        return user.getBalance();
    }

    @Override
    @Transactional
    public void addFunds(long userId, BigDecimal amount) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(User.class, "Id", String.valueOf(userId)));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<TransactionDto> getAllTransactionsByUserId(long id) {
        List<Transaction> transactionList = transactionRepository.findBySenderUserId(id);
        return transactionList.stream().map(Transaction::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<TransactionDto> getAllTransactionsByUserIdExampleMatcher(long id) {
        Transaction transaction = new Transaction();
        User user = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(User.class, "Id", String.valueOf(id)));
        transaction.setSenderUser(user);
        List<Transaction> transactionList = transactionRepository.findAll(
                Example.of(
                        transaction,
                        ExampleMatcher.matchingAll()));
        return transactionList.stream().map(Transaction::toDto).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public UserDto createUser(UserDto dto) {
        User user = UserDto.toEntity(dto);
        User savedUser = userRepository.save(user);
        return User.toDto(savedUser);
    }
}
