package com.kodnest.best_shop.service.CartItem;

import com.kodnest.best_shop.model.Cart;
import com.kodnest.best_shop.model.CartItem;
import com.kodnest.best_shop.model.Product;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);

    void removeItemFromCart(Long cartId, Long productId);

    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
