package am.spacegoat.task.repository;

import am.spacegoat.task.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
