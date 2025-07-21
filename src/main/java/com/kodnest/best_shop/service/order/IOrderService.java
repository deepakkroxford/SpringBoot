package com.kodnest.best_shop.service.order;

import com.kodnest.best_shop.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderId);

    List<Order> getUserOrders(Long orderId);
}
