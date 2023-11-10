package am.spacegoat.task.service.impl;

import am.spacegoat.task.domain.Transaction;
import am.spacegoat.task.domain.UserEntity;
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
import java.util.Optional;
import java.util.function.Function;
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
    public void transferRepeatableRead(long senderId, long receiverId, BigDecimal amount) {
        transfer(senderId, receiverId, amount);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transferReadCommitted(long senderId, long receiverId, BigDecimal amount) {
        transfer(senderId, receiverId, amount);
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void transferReadUncommitted(long senderId, long receiverId, BigDecimal amount) {
        transfer(senderId, receiverId, amount);
    }

    private void transferWithPotentialDeadlock(long senderId, long receiverId, BigDecimal amount) {
        UserEntity sender = userRepository.findById(senderId).
                orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(senderId)));
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough funds");
        }
        UserEntity receiver = userRepository.findById(receiverId).
                orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(receiverId)));

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setSenderUserEntity(sender);
        transaction.setReceiverUserEntity(receiver);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void transferReadUncommittedWithSleep(long senderId, long receiverId, BigDecimal amount) throws InterruptedException {
        UserEntity sender = userRepository.findById(senderId).
                orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(senderId)));
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough funds");
        }
        UserEntity receiver = userRepository.findById(receiverId).
                orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(receiverId)));

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        userRepository.saveAndFlush(sender);
        userRepository.saveAndFlush(receiver);

        Thread.sleep(2000);

        Transaction transaction = new Transaction();
        transaction.setSenderUserEntity(sender);
        transaction.setReceiverUserEntity(receiver);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    /**
     * Make deterministic updates order by flushing Hibernate session after each update.
     * Also take a look on <i>hibernate.order_updates</i> which would resolve it automatically, but I'd suggest to be more explicit in these cases.
     *
     * @param senderId
     * @param receiverId
     * @param amount
     */
    private void transfer(long senderId, long receiverId, BigDecimal amount) {
        UserEntity sender = userRepository.findById(senderId).
                orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(senderId)));
        UserEntity receiver = userRepository.findById(receiverId).
                orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(receiverId)));
        if (senderId < receiverId) {
            receiver.setBalance(receiver.getBalance().add(amount));
            //flush needed for deterministic locking
            userRepository.flush();
            sender.setBalance(sender.getBalance().subtract(amount));
        } else {
            sender.setBalance(sender.getBalance().subtract(amount));
            userRepository.flush();
            receiver.setBalance(receiver.getBalance().add(amount));
        }

        Transaction transaction = new Transaction();
        transaction.setSenderUserEntity(sender);
        transaction.setReceiverUserEntity(receiver);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    /**
     * Hibernate sorts updates queries by entity arriving in the context(e.g by finding). Too implicit and hard to rely on.
     *
     * @param senderId
     * @param receiverId
     * @param amount
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transferUpdateOrderBySelection(long senderId, long receiverId, BigDecimal amount) {
        UserEntity sender;
        UserEntity receiver;

        if (senderId < receiverId) {
            sender = userRepository.findById(senderId).
                    orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(senderId)));
            receiver = userRepository.findById(receiverId).
                    orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(receiverId)));
        } else {
            receiver = userRepository.findById(receiverId).
                    orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(receiverId)));
            sender = userRepository.findById(senderId).
                    orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(senderId)));
        }
        receiver.setBalance(receiver.getBalance().add(amount));
        sender.setBalance(sender.getBalance().subtract(amount));


        Transaction transaction = new Transaction();
        transaction.setSenderUserEntity(sender);
        transaction.setReceiverUserEntity(receiver);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    /**
     * Use pessimistic lock to avoid deadlocks
     *
     * @param senderId
     * @param receiverId
     * @param amount
     */

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transferMakePessimisticLockDeterministically(long senderId, long receiverId, BigDecimal amount) {
        Function<Long, Optional<UserEntity>> f1 = senderId > receiverId ? userRepository::findOnePessimistic : userRepository::findById;
        Function<Long, Optional<UserEntity>> f2 = senderId > receiverId ? userRepository::findById : userRepository::findOnePessimistic;
        UserEntity sender = f1.apply(senderId).orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(senderId)));
        UserEntity receiver = f2.apply(receiverId).orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(receiverId)));
        receiver.setBalance(receiver.getBalance().add(amount));
        sender.setBalance(sender.getBalance().subtract(amount));

        Transaction transaction = new Transaction();
        transaction.setSenderUserEntity(sender);
        transaction.setReceiverUserEntity(receiver);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public BigDecimal getBalance(long userId) {
        return userRepository.getBalanceByUserId(userId);
    }

    @Override
    @Transactional
    public void addFunds(long userId, BigDecimal amount) {
        UserEntity userEntity = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(userId)));
        userEntity.setBalance(userEntity.getBalance().add(amount));
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public List<TransactionDto> getAllTransactionsByUserId(long id) {
        userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(id)));
        List<Transaction> transactionList = transactionRepository.findBySenderUserId(id);
        return transactionList.stream().map(Transaction::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<TransactionDto> getAllTransactionsByUserIdExampleMatcher(long id) {
        Transaction transaction = new Transaction();
        UserEntity userEntity = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(UserEntity.class, "Id", String.valueOf(id)));
        transaction.setSenderUserEntity(userEntity);
        List<Transaction> transactionList = transactionRepository.findAll(
                Example.of(
                        transaction,
                        ExampleMatcher.matchingAll()));
        return transactionList.stream().map(Transaction::toDto).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public UserDto createUser(UserDto dto) {
        UserEntity userEntity = UserDto.toEntity(dto);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return UserEntity.toDto(savedUserEntity);
    }
}
