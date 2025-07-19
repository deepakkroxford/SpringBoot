package com.kodnest.best_shop.service.cart;

import com.kodnest.best_shop.exceptions.ResourceNotFoundException;
import com.kodnest.best_shop.model.Cart;
import com.kodnest.best_shop.model.CartItem;
import com.kodnest.best_shop.repository.CartItemRepository;
import com.kodnest.best_shop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    @Override
    public Cart getCart(Long id) {
        Cart cart = null;
        try {
            cart = cartRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Cart not found"));

        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllById(cart);
        cart.getCartItem().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }
}
