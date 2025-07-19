package com.kodnest.best_shop.service.CartItem;

import com.kodnest.best_shop.model.Cart;
import com.kodnest.best_shop.model.CartItem;
import com.kodnest.best_shop.model.Product;
import com.kodnest.best_shop.repository.CartItemRepository;
import com.kodnest.best_shop.repository.CartRepository;
import com.kodnest.best_shop.service.cart.CartService;
import com.kodnest.best_shop.service.cart.ICartService;
import com.kodnest.best_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{

    private  final CartItemRepository cartItemRepository;
    private  final CartRepository cartRepository;
    private final ICartService cartService;
    private final IProductService productService;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        /**
         * Get the cart.
         * Get the product.
         * Check if the Product already in the cart.
         * If Yes then increase the quantity with the requested quantity.
         * If No then initiate the new CartItem.
         */
        Cart cart = cartService.getCart(cartId);
        Product product  = productService.getProductById(productId);
        CartItem cartItem = cart.
                getCartItem().stream().
                filter(item->item.getProduct().getId().equals(productId)).
                findFirst().orElse(new CartItem());

        if(cartItem.getId() == null) {
           cartItem.setCart(cart);
           cartItem.setProduct(product);
           cartItem.setQuantity(quantity);
           cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Cart cartId, Product productId) {

    }

    @Override
    public void updateItemQuantity(Cart cartId, Product productId, int quantity) {

    }
}
