package am.spacegoat.task.repository;

import am.spacegoat.task.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u.balance FROM UserEntity u WHERE u.id = :userId")
    BigDecimal getBalanceByUserId(Long userId);

    //("update user balance set balance += amount ")
    //updateUserBalance

    //add for subtraction, check balance > amount


}
