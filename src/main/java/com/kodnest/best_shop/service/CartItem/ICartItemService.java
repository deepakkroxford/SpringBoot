package com.kodnest.best_shop.service.CartItem;

import com.kodnest.best_shop.model.Cart;
import com.kodnest.best_shop.model.Product;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Cart cartId, Product productId);
    void updateItemQuantity(Cart cartId, Product productId, int quantity);
}
