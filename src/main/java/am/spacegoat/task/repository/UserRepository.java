package am.spacegoat.task.repository;

import am.spacegoat.task.domain.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u.balance FROM UserEntity u WHERE u.id = :userId")
    BigDecimal getBalanceByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UserEntity u where u.id = :id")
    Optional<UserEntity> findOnePessimistic(Long id);

    //("update user balance set balance += amount ")
    //updateUserBalance

    //add for subtraction, check balance > amount


}
