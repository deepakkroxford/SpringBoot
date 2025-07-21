package com.kodnest.best_shop.service.cart;

import com.kodnest.best_shop.exceptions.ResourceNotFoundException;
import com.kodnest.best_shop.model.Cart;
import com.kodnest.best_shop.repository.CartItemRepository;
import com.kodnest.best_shop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    /**
     * Constructor injection is considered the best practice for Dependency Injection.
     * It ensures that all required dependencies are available before the class is created,
     * preventing runtime exceptions like NullPointerException.
     */
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }


    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteById(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    @Transactional
    public Long initializeNewCart() {
        Cart cart = new Cart(); // brand new cart, not setting ID manually
        cart.setTotalAmount(BigDecimal.ZERO);
        Cart savedCart = cartRepository.save(cart); // no merge, just save
        return savedCart.getId();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
