package com.kodnest.best_shop.repository;

import com.kodnest.best_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
