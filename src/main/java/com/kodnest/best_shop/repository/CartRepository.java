package com.kodnest.best_shop.repository;

import com.kodnest.best_shop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
