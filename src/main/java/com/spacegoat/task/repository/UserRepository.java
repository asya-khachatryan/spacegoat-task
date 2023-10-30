package com.spacegoat.task.repository;

import com.spacegoat.task.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
