package com.spacegoat.task.repository;

import com.spacegoat.task.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.senderUserId = :senderId")
    List<Transaction> findBySenderUserId(@Param("senderId") long senderId);
}
