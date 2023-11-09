package am.spacegoat.task.service;

import am.spacegoat.task.dto.TransactionDto;
import am.spacegoat.task.dto.UserDto;

import java.math.BigDecimal;
import java.util.List;

public interface UserAccountService {
    void transferRepeatableRead(long senderId, long receiverId, BigDecimal amount);

    void transferReadCommitted(long senderId, long receiverId, BigDecimal amount);

    void transferReadUncommitted(long senderId, long receiverId, BigDecimal amount);

    void transferReadUncommittedWithSleep(long senderId, long receiverId, BigDecimal amount) throws InterruptedException;

    BigDecimal getBalance(long userId);

    void addFunds(long userId, BigDecimal amount);

    List<TransactionDto> getAllTransactionsByUserId(long id);

    List<TransactionDto> getAllTransactionsByUserIdExampleMatcher(long id);

    UserDto createUser(UserDto dto);
}
