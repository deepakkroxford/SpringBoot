package com.kodnest.best_shop.repository;

import com.kodnest.best_shop.model.Cart;
import com.kodnest.best_shop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
        void deleteAllById(Cart cart);
}
