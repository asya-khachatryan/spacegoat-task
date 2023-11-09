package am.spacegoat.task.repository;

import am.spacegoat.task.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.senderUserEntity.id = :senderId")
    List<Transaction> findBySenderUserId(@Param("senderId") long senderId);


}
